import akka.NotUsed
import akka.stream.scaladsl.{Flow, Sink, Source}
import config.AppConfig._
import model.Video
import setup.CassandraDB
import setup.DBConfig._
import utils.Logging

import java.time.Instant
import scala.concurrent.Future

object QuillMain extends App with Logging {

  import QuillUtils.insert

  CassandraDB.init() match {
    case Left(msg) =>
      logger.error(msg)
      sys.exit(1)
    case Right(_) =>
      logger.info("tally ho, db is good to go")
  }

  val videoList = List(Video("Mark", "1234", "top gun", Instant.now))

  val source = Source(videoList)

  val quillInsertFlow: Flow[Video, Unit, NotUsed] = {
    Flow[Video].mapAsync(10)(video =>
      insert(video)
    )
  }

  val sink = Sink.foreach(println)

  source
    .via(quillInsertFlow)
    .to(sink)
    .run
}

object QuillUtils {
  import quillDB._
  def insert(video: Video): Future[Unit] = quillDB.run(quote {
    query[Video].insert(lift(video))
  })
}
