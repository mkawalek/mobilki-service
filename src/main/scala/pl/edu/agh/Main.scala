package pl.edu.agh

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import pl.edu.agh.api.{MobilkiEndpoint, PredictionEndpoint}
import pl.edu.agh.infrastructure.LoadPredictor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Main extends App {

  private implicit val system: ActorSystem = ActorSystem()
  private implicit val ec: ExecutionContext = system.dispatcher
  private implicit val mat: ActorMaterializer = ActorMaterializer()
  private val log = Logging(system, "main-logger")

  private val endpoint = new MobilkiEndpoint()
  private val predictions = new PredictionEndpoint()

  Http().bindAndHandle(endpoint.routing ~ predictions.routing, "0.0.0.0", 8080).onComplete {
    case Success(_) => log.info("API LISTENING ON 0.0.0.0:8080")
    case Failure(exception) => log.error(exception, "FAILURE WHEN STARTING APP !")
  }(system.dispatcher)

  val scheduler = system.scheduler
  implicit val executor = system.dispatcher
  val task = new Runnable {
    def run() {
      LoadPredictor.measureLoad
    }
  }

  scheduler.schedule(
    initialDelay = Duration(30, TimeUnit.SECONDS),
    interval = Duration(1, TimeUnit.MINUTES),
    runnable = task)
}
