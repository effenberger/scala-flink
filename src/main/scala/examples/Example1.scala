package examples

import org.apache.flink.api.scala._

object Example1 {

//  example for reading csv file and counting the lines

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val resourceUrl = getClass.getResource("/tmp.csv")

    val data: org.apache.flink.api.scala.DataSet[String] = env.readTextFile(resourceUrl.getPath)

    println(data.count())
  }
}
