package pl.edu.agh.api

import java.io.File

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import pl.edu.agh.infrastructure.OcrExecutor

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class MobilkiEndpoint()(implicit mat: ActorMaterializer, dispatcher: ExecutionContext) extends SprayJsonSupport {

  val routing: Route =
    withoutRequestTimeout {
      (path("execute") & post & storeUploadedFile("file", _ => new File(s"/tmp/${Random.nextInt(Int.MaxValue)}.png"))) { case (_, file) =>
        onSuccess(
          Future {
            OcrExecutor.execute(file)
          }
          .map(HttpEntity(_)))(complete(_))
      }
    }

}
