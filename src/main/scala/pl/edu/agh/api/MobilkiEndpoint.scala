package pl.edu.agh.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.JsValue
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class MobilkiEndpoint() extends SprayJsonSupport {

  val routing: Route =
    (path("execute") & post & entity(as[JsValue])) { params =>
      complete(params)
    }

}
