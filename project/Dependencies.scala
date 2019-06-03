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

  val videoDependencies = Seq(
    "xuggle" % "xuggle-xuggler" % Versions.xuggler
  )

  val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.akka
  )

  val additionalResolvers = Seq(
    Resolver.DefaultMavenRepository,
    "shit" at "http://www.dcm4che.org/maven2/"
  )

  val all: Seq[ModuleID] = Seq(
    akkaDependencies,
    akkaHttpDependencies,
    sprayDependencies,
    videoDependencies,
    loggingDependencies
  ).flatten

}
