import akka.stream.alpakka.cassandra.CassandraWriteSettings
import akka.stream.alpakka.cassandra.scaladsl.CassandraFlow
import akka.stream.scaladsl.{Sink, Source}
import config.AppConfig._
import model.Video
import setup.{AlpakkaCassandraSetup, CassandraDB}
import utils.Logging

import java.time.Instant

object AlpakkaMain extends App with AlpakkaCassandraSetup with Logging {

  CassandraDB.init() match {
    case Left(msg) =>
      logger.error(msg)
      sys.exit(1)
    case Right(_) =>
      logger.info("tally ho, db is good to go")
  }

  val videoList = List(Video("Mark", "1234", "top gun", Instant.now))

  val source = Source(videoList)

  val alpakkaInsertFlow = CassandraFlow.create(
    CassandraWriteSettings.defaults,
    s"INSERT INTO testkeyspace.video(userid, videoid, title, creationdate) VALUES(?, ?, ?, ?)",
    insertStatementBinder
  )

  val sink = Sink.foreach(println)

  source
    .via(alpakkaInsertFlow)
    .to(sink)
    .run
}
