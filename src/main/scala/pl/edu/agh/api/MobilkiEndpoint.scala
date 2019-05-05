package pl.edu.agh.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import pl.edu.agh.infrastructure.FileEncryptor

import scala.concurrent.ExecutionContext

class MobilkiEndpoint()(implicit mat: ActorMaterializer, dispatcher: ExecutionContext) extends SprayJsonSupport {

  val routing: Route =
    (path("execute") & post & fileUpload("file") & parameters("password")) { case ((fileInfo, fileSource), password) =>
      onSuccess(fileSource
        .runWith(Sink.fold(ByteString.empty)(_ ++ _))
        .map(_.utf8String)
        .map(FileEncryptor.encrypt(_, password))
        .map(HttpEntity(_)))(complete(_))
    }

}
