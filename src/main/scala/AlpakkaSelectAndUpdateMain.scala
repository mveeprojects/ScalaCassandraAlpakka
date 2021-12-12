import QuillUtils.insert
import akka.NotUsed
import akka.stream.alpakka.cassandra.CassandraWriteSettings
import akka.stream.alpakka.cassandra.scaladsl.{CassandraFlow, CassandraSource}
import akka.stream.scaladsl.{Flow, Sink}
import com.datastax.oss.driver.api.core.cql.{Row, SimpleStatement}
import config.AppConfig._
import model.Video
import setup.{AlpakkaCassandraSetup, CassandraDB}
import utils.Logging

import java.time.Instant

object AlpakkaSelectAndUpdateMain extends App with AlpakkaCassandraSetup with Logging {

  val videoList = List(
    Video("Mark", "1234", "top gun", Instant.now),
    Video("Sally", "1234", "TOP GUN", Instant.now)
  )

  CassandraDB.init() match {
    case Left(msg) =>
      logger.error(msg)
      sys.exit(1)
    case Right(_) =>
      logger.info("Cassandra ready, inserting example data...")
      videoList.foreach(insert)
      logger.info("Example data populated in Cassandra.")
  }

  def transformRowToVideo(row: Row): Video =
    Video(
      row.getString("userid"),
      row.getString("videoid"),
      row.getString("title"),
      row.getInstant("creationdate")
    )

  val selectStmt = SimpleStatement.newInstance(s"SELECT * FROM ${appConfig.cassandra.keyspace}.video").setPageSize(20)

  val alpakkaInsertFlow: Flow[Video, Video, NotUsed] = CassandraFlow.create(
    CassandraWriteSettings.defaults,
    s"INSERT INTO ${appConfig.cassandra.keyspace}.video(userid, videoid, title, creationdate) VALUES(?, ?, ?, ?)",
    insertStatementBinder
  )

  def convertTitleToLowercase(video: Video): Video =
    video.copy(
      title = video.title.toLowerCase
    )

  CassandraSource(selectStmt)
    .map(transformRowToVideo)
    .map(convertTitleToLowercase)
    .via(alpakkaInsertFlow)
    .runWith(Sink.foreach(println))
}
