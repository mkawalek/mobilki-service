import sbt._

object Dependencies {

  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % Versions.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream" % Versions.akka
  )

  val akkaHttpDependencies = Seq(
    "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp
  )

  val sprayDependencies = Seq(
    "io.spray" %% "spray-json" % Versions.spray,
    "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttpSprayJson
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.akka
  )

  val additionalResolvers = Seq(
    Resolver.DefaultMavenRepository,
    "Synerise Libs Release" at "http://artifactory.service/sbt-release/",
    "Synerise Libs Dev" at "http://artifactory.service/sbt-dev"
  )

  val all: Seq[ModuleID] = Seq(
    akkaDependencies,
    akkaHttpDependencies,
    sprayDependencies,
    loggingDependencies
  ).flatten

}