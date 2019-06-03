package pl.edu.agh.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import pl.edu.agh.infrastructure.FileEncryptor

import scala.concurrent.{ExecutionContext, Future}

class MobilkiEndpoint()(implicit mat: ActorMaterializer, dispatcher: ExecutionContext) extends SprayJsonSupport {

  val routing: Route =
    (path("execute") & post & parameter('number.as[Long])) { number =>
      onSuccess(Future {
        FileEncryptor.encrypt(number)
      }.map(HttpEntity(_)))(complete(_))
    }

}
