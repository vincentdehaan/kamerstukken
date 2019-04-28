import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    fork := true,
    name := "kamerstukken",
    javaOptions ++= Seq( "-Djavax.net.ssl.trustStore=StaatDerNederlanden.jks"), // "-Djavax.net.debug=all", // TODO: if I enable debug=all, the normal output is omitted. Is this a bug?
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"   % "10.1.8",
      "net.ruippeixotog" %% "scala-scraper" % "2.1.0",
      scalaTest % Test
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
