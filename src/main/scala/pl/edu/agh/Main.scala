package pl.edu.agh

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import pl.edu.agh.api.MobilkiEndpoint

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App {

  private implicit val system: ActorSystem = ActorSystem()
  private implicit val ec: ExecutionContext = system.dispatcher
  private implicit val mat: ActorMaterializer = ActorMaterializer()
  private val log = Logging(system, "main-logger")

  private val endpoint = new MobilkiEndpoint()

  Http().bindAndHandle(endpoint.routing, "0.0.0.0", 8080).onComplete {
    case Success(_) => log.info("API LISTENING ON 0.0.0.0:8080")
    case Failure(exception) => log.error(exception, "FAILURE WHEN STARTING APP !")
  }(system.dispatcher)

}
