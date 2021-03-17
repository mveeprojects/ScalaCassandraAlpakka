import Dependencies._

name := "ScalaCassandraAlpakka"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= (
  cassandraDependencies ++
    loggingDependencies ++
    configDependencies
)

lazy val root = Project("ScalaCassandra", file("."))
