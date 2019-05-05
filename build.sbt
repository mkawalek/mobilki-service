name := "mobilki-service"
version := "1.0"
scalaVersion := "2.12.4"

dockerBaseImage := "openjdk"
dockerRepository := Some("mkawalek")

lazy val `mobilki-service` = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, GitVersioning, SbtNativePackager, DockerPlugin)
  .settings(libraryDependencies ++= Dependencies.all)
  .settings(resolvers ++= Dependencies.additionalResolvers)
