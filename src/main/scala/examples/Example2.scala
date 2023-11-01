package examples

import org.apache.flink.api.scala._

object Example2 {

//  example for reading csv file and map

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val text = env.fromElements("Sascha arbeitet bei Zalando", "Axel ist Sascha dem sein Chef", "Manchmal regnet es in BErlin")
    println(text.map(s => s.split(" ")))
  }
}
