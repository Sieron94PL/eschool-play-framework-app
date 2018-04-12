name := """eschool"""
organization := "pl.dmcs"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  javaJdbc,
  javaForms,
  ws,
  "mysql" % "mysql-connector-java" % "5.1.27",
  "be.objectify" %% "deadbolt-java" % "2.6.3",
  "be.objectify" %% "deadbolt-java-gs" % "2.6.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.typesafe.play" %% "play-json" % "2.6.3",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "requirejs" % "2.2.0",
  "com.nappin" %% "play-recaptcha" % "2.3",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",
  "it.innove" % "play2-pdf" % "1.7.0"
)