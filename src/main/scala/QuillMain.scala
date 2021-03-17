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

  CassandraDB.init() match {
    case Left(msg) =>
      logger.error(msg)
      sys.exit(1)
    case Right(_) =>
      logger.info("tally ho, db is good to go")
  }

  val videoList = List(Video("Mark", "1234", "top gun", Instant.now))

  val source = Source(videoList)

  val quillInsertFlow: Flow[Video, Future[Unit], NotUsed] = {
    import quillDB._
    Flow[Video].map(video =>
      quillDB.run(quote {
        query[Video].insert(lift(video))
      })
    )
  }

  val sink = Sink.foreach(println)

  source
    .via(quillInsertFlow)
    .to(sink)
    .run
}
