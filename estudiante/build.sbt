name := """estudiante"""
organization := "com.ubb"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies ++= Seq(
  jdbc
)
libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.7.5"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ubb.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ubb.binders._"
