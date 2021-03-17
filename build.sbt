import Dependencies._

name := "ScalaCassandraStreaming"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= (
  cassandraDependencies ++
    loggingDependencies ++
    configDependencies
)

lazy val root = Project("ScalaCassandraStreamibg", file("."))
