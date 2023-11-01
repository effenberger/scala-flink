package examples

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend
import org.apache.flink.runtime.state.storage.FileSystemCheckpointStorage

object Example7 {

  //  example for checkpointing with updated state backend

  def main(args: Array[String]): Unit = {
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    // Set the state backend to HashMapStateBackend
    senv.setStateBackend(new HashMapStateBackend())

    // Set the checkpoint storage to a local filesystem path
    val checkpointPath = "file:///tmp/flink/checkpoints"
    senv.getCheckpointConfig.setCheckpointStorage(new FileSystemCheckpointStorage(checkpointPath))

    // Configure checkpointing
    senv.enableCheckpointing(1000)
    senv.getCheckpointConfig.setCheckpointingMode(org.apache.flink.streaming.api.CheckpointingMode.EXACTLY_ONCE)
    senv.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)

    val text = senv.socketTextStream("127.0.0.1", 9005, '\n')
    //    use "nc -l 9005" and then type words separated by newlines

    val windowCounts = text
      .filter(!_.contains("abc"))
      .flatMap(_.split("\\s"))
      .map(WordWithCount(_, 1))
      .keyBy(_.word)
      .window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      .sum("count")

    windowCounts.print().setParallelism(1)

    senv.execute("Example7")
  }

  case class WordWithCount(word: String, count: Long)
}
