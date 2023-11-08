package examples

import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

import java.time.Duration

object Example6 {

//  example for event time window
//  use and start TimestampWordSimulator before starting this example

  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    val text = senv.socketTextStream("127.0.0.1", 9005, '\n')
    //    use "nc -l 9005" and then type words separated by newlines

    println(s"Job start time: ${System.currentTimeMillis()}")

    val windowCounts = text
      .filter(!_.contains("abc"))
      .map { line =>
        val parts = line.split(",")
        val timestamp = parts(0).toLong
        val word = parts(1)
        println(timestamp)
        WordWithCount(word, 1, timestamp)
      }
      .assignTimestampsAndWatermarks(
        WatermarkStrategy
          .forBoundedOutOfOrderness[WordWithCount](Duration.ofSeconds(1))
          .withTimestampAssigner(new SerializableTimestampAssigner[WordWithCount] {
            override def extractTimestamp(element: WordWithCount, recordTimestamp: Long): Long = element.timestamp
          })
      )
      .keyBy(_.word)
      .window(SlidingEventTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      .sum("count")

    windowCounts.print().setParallelism(1)

    senv.execute("Example6")
  }

  case class WordWithCount(word: String, count: Long, timestamp: Long)
}
