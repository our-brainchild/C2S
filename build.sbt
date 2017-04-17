name := """C2S"""
organization := "C2S"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies ++= Seq(
  jdbc,
  javaJdbc,
  cache,
  ws,
  "org.liquibase" % "liquibase-maven-plugin" % "3.5.3",
  "org.postgresql" % "postgresql" % "42.0.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
  "com.github.aselab" % "squeryl" % "0.9.5"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "C2S.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "C2S.binders._"
