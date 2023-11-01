package examples

import org.apache.flink.api.scala._

object Example4 {

//  example for inner join

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val data_A = env.fromElements(
      ("a", 1),
      ("b", 2),
      ("c", 3),
      ("a", 4),
      ("c", 5),
      ("c", 6)
    )

    val data_B = env.fromElements(
      ("a", 100),
      ("c", 500),
    )

    val res = data_A.join(data_B).where(0).equalTo(0)
    val res_2 = res{ (a, b) => (a._1, a._2, b._2)}

    res.print()
    res_2.print()

  }
}
