package examples

import org.apache.flink.api.scala._

object Example2 {

//  example for reading csv file and map

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val text = env.fromElements("Hello, how are you?", "I am fine, thank you", "Test, test test")

    text
      .map(s => s.split(" "))
      .collect()
      .foreach(array => array.foreach(println))
  }
}
