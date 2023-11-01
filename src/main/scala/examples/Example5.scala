package examples

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object Example5 {

//  example for processing time window

  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    val text = senv.socketTextStream("127.0.0.1", 9005, '\n')
    //    use "nc -l 9000" and then type words separated by newlines

    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy(_.word)
      .window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      .sum("count")

    windowCounts.print().setParallelism(1)

    senv.execute("Example5")
  }

  case class WordWithCount(word: String, count: Long)
}
