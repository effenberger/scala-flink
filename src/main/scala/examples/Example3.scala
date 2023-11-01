package examples

import org.apache.flink.api.scala._

object Example3 {

//  example for reading csv file and filtering out the header

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val resourceUrl = getClass.getResource("/OnlineRetail.csv")

    val data = env.readTextFile(resourceUrl.getPath)
    val data_no_header = data.filter(line => !line.startsWith("InvoiceNo"))

    val data_mapped = data_no_header.filter(line => line.split(",").length == 8)

    println(data_mapped.count())
  }
}
