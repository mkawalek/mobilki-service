package pl.edu.agh.infrastructure

import java.io.File
import com.asprise.ocr.Ocr

object OcrExecutor {

  def execute(file: File): String = {
    println("Executing OCR");
    Ocr.setUp()
    val ocr = new Ocr()
    ocr.startEngine("eng", Ocr.SPEED_FASTEST)
    val result = ocr.recognize(Array(file), Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT)
//    println("Result: " + result)
    ocr.stopEngine()

    result
  }

}
