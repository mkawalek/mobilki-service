package pl.edu.agh.infrastructure

import java.io.File
import java.lang.management.ManagementFactory
import java.util.Calendar

import com.sun.management.OperatingSystemMXBean
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.data.{DataSet, DataSetRow}
import org.neuroph.core.learning.LearningRule
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.util.TransferFunctionType

import scala.collection.JavaConversions._

object LoadPredictor {
  var lastUsage = 0L
  var file = new File("network.nnet")
  var network: NeuralNetwork[_ <: LearningRule] = null
  if (file.exists) {
    network = NeuralNetwork.createFromFile(file)
  } else {
    network = new MultiLayerPerceptron(List[Integer](2, 16, 1), TransferFunctionType.SIGMOID)
  }


  def measureLoad() = {
    val now = Calendar.getInstance
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)

    val osBean = ManagementFactory.getPlatformMXBean(
      classOf[OperatingSystemMXBean])

    val current = osBean.getProcessCpuTime

    val currentDay = now.get(Calendar.DAY_OF_WEEK)
    val currentMinute = (hour * 60) + minute
    train(currentDay, currentMinute, (current - lastUsage) / (1000 * 1000 * 1000 * 60.0 * osBean.getAvailableProcessors))
    lastUsage = current
  }

  def train(dayOfWeek: Int, minuteOfDay: Int, usage: Double): Unit = {
    println(dayOfWeek + " " + minuteOfDay + " " + usage)
    var data = new DataSet(2, 1)
    data.addRow(new DataSetRow(Array((dayOfWeek - 1) / 6.0, minuteOfDay / 1440.0), Array(usage)))
    network.learn(data)
    network.save("network.nnet")
  }

  def predict() = {
    val now = Calendar.getInstance
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)

    var currentDay = now.get(Calendar.DAY_OF_WEEK)
    var currentMinute = (hour * 60) + minute
    var result = Array[Double]()
    for (i <- 1 to 60 * 2) {
      if (currentMinute > 1440) {
        currentMinute = 0
        currentDay += 1
        if (currentDay > 7)
          currentDay = 0
      }
      network.setInput((currentDay - 1) / 6.0, currentMinute / 1440.0)
      network.calculate()
      result = result :+ network.getOutput()(0)
      currentMinute += 1
    }
    result.mkString("\n")
  }
}
