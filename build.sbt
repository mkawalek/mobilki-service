name := "mobilki-service"
version := "1.1"
scalaVersion := "2.12.4"

dockerBaseImage := "openjdk"
dockerRepository := Some("mkawalek")

libraryDependencies += "org.beykery" % "neuroph" % "2.92"

lazy val `mobilki-service` = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, GitVersioning, SbtNativePackager, DockerPlugin)
  .settings(libraryDependencies ++= Dependencies.all)
  .settings(resolvers ++= Dependencies.additionalResolvers)
