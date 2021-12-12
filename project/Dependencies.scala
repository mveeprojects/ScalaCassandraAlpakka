import sbt._

object Dependencies {

  object Versions {
    val logback         = "1.2.7"
    val pureConfig      = "0.17.1"
    val cassandraDriver = "4.9.0"
    val quill           = "3.6.1"
    val alpakka         = "3.0.4"
  }

  import Versions._

  val cassandraDependencies = Seq(
    "com.datastax.oss"    % "java-driver-core"              % cassandraDriver,
    "com.datastax.oss"    % "java-driver-query-builder"     % cassandraDriver,
    "io.getquill"        %% "quill-cassandra"               % quill,
    "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % alpakka
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % logback
  )

  val configDependencies = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureConfig
  )
}
