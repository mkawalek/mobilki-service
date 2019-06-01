package pl.edu.agh.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import pl.edu.agh.infrastructure.LoadPredictor

import scala.concurrent.ExecutionContext

class PredictionEndpoint()(implicit mat: ActorMaterializer, dispatcher: ExecutionContext) extends SprayJsonSupport {

  val routing: Route =
    (path("predictions") & get ) {
      complete(LoadPredictor.predict)
    }
}
