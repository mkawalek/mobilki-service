package pl.edu.agh.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import pl.edu.agh.infrastructure.FactorialCalculator

import scala.concurrent.{ExecutionContext, Future}

class FactorialEndpoint()(implicit mat: ActorMaterializer, dispatcher: ExecutionContext) extends SprayJsonSupport {

  val routing: Route =
    withoutRequestTimeout {
      (path("factorial") & post & parameter('number.as[Long])) { number =>
        onSuccess(Future {
          FactorialCalculator.calculate(number)
        }.map(HttpEntity(_)))(complete(_))
      }
    }

}
