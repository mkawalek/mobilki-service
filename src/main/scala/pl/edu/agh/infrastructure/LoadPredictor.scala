package pl.edu.agh.infrastructure

import java.io.File
import java.lang.management.ManagementFactory
import java.util.Calendar

import com.sun.management.OperatingSystemMXBean
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.data.{DataSet, DataSetRow}
import org.neuroph.core.learning.LearningRule
import org.neuroph.core.transfer.{Sigmoid, Sin}
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.nnet.learning.{BackPropagation, MomentumBackpropagation}
import org.neuroph.util.NeuronProperties

import scala.collection.JavaConversions._

object LoadPredictor {
  var lastUsage = 0L
  var file = new File("network.nnet")
  var network: NeuralNetwork[_ <: LearningRule] = null
  if (file.exists) {
    network = NeuralNetwork.createFromFile(file)
  } else {
    val neuronProperties = new NeuronProperties
    neuronProperties.setProperty("useBias", true)
    neuronProperties.setProperty("transferFunction", classOf[Sin])
    network = new MultiLayerPerceptron(List[Integer](3, 100, 1), neuronProperties)
    network.getLayerAt(2).getNeurons().foreach(n => n.setTransferFunction(new Sigmoid()));
    preTraining
  }

  val learningRule = new BackPropagation()
  learningRule.setLearningRate(0.01)
  learningRule.setMaxIterations(1)
  network.asInstanceOf[MultiLayerPerceptron].setLearningRule(learningRule)

  def measureLoad() = {
    val now = Calendar.getInstance
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)

    val osBean = ManagementFactory.getPlatformMXBean(
      classOf[OperatingSystemMXBean])

    val currentDay = now.get(Calendar.DAY_OF_WEEK)

    val current = osBean.getProcessCpuTime
    train(currentDay, hour, minute, (current - lastUsage) / (1000 * 1000 * 1000 * 60.0 * osBean.getAvailableProcessors))
    lastUsage = current
  }

  def train(dayOfWeek: Int, hour: Int, minute: Int, usage: Double): Unit = {
    println("training: " + dayOfWeek + " " + hour + ":" + minute + " " + usage)
    var data = new DataSet(3, 1)
    data.addRow(new DataSetRow(Array((dayOfWeek - 1) / 6.0, hour / 23.0, minute / 59.0), Array(usage)))
    network.learn(data)
    network.save("network.nnet")
  }

  def predict() = {
    val now = Calendar.getInstance
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)

    var currentDay = now.get(Calendar.DAY_OF_WEEK)
    var currentHour = hour
    var currentMinute = minute
    var result = Array[Double]()
    for (i <- 1 to 60 * 2) {
      if (currentMinute > 59) {
        currentMinute = 0
        currentHour += 1
        if (currentHour > 23) {
          currentHour = 0
          currentDay += 1
          if (currentDay > 6)
            currentDay = 0
        }
      }
      network.setInput((currentDay - 1) / 6.0, currentHour / 23.0, currentMinute / 59.0)
      network.calculate()
      result = result :+ network.getOutput()(0)
      currentMinute += 1
    }
    result.mkString("\n")
  }

  def preTraining = {
    val learningRule = new MomentumBackpropagation()
    learningRule.setMomentum(0.5)
    learningRule.setLearningRate(0.25)
    learningRule.setMaxIterations(2000)
    network.asInstanceOf[MultiLayerPerceptron].setLearningRule(learningRule)

    var data = new DataSet(3, 1)
    for (day <- 0 to 6)
      for (hour <- 0 to 23)
        for (off <- 0 to 2) {
          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 0) / 59.0), Array(0.1)))

          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 1) / 59.0), Array(0.4)))

          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 2) / 59.0), Array(0.6)))

          for (minute <- 3 to 9)
            data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + minute) / 59.0), Array(0.8)))

          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 10) / 59.0), Array(0.55)))

          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 11) / 59.0), Array(0.3)))

          data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + 12) / 59.0), Array(0.08)))

          for (minute <- 13 to 19)
            data.addRow(new DataSetRow(Array(day / 6.0, hour / 23.0, (off * 20 + minute) / 59.0), Array(0.01)))
        }

    println("Started pre-training")
    data.shuffle()
    network.learn(data)
    println("Finished pre-training")
  }
}
