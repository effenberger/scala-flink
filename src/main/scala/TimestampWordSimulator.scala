import java.io.PrintStream
import java.net.ServerSocket
import java.util.concurrent.Executors
import scala.util.{Failure, Success, Try}

// this is for Example 6, where we care about event time stamps
// run this simulator in a separate job before starting example 6
//

object TimestampWordSimulator {
  def main(args: Array[String]): Unit = {
    val serverPort = 9005
    val server = new ServerSocket(serverPort)
    println(s"TimestampWordSimulator started on port $serverPort.")
    println("Waiting for a connection...")

    val pool = Executors.newFixedThreadPool(1)

    pool.execute(new Runnable {
      override def run(): Unit = {
        val socket = server.accept()
        println("Accepted connection from " + socket.getInetAddress)

        val out = new PrintStream(socket.getOutputStream)

        try {
          while (true) {
            val timestamp = System.currentTimeMillis()
            val word = "word" + (timestamp % 10) // Just an example of a word
            val message = s"$timestamp,$word\n"
            out.print(message)
            if (out.checkError()) {
              println("Client disconnected.")
              return
            }
            Thread.sleep(1000) // Wait for a second
          }
        } catch {
          case e: InterruptedException => println("Server interrupted.")
          case e: Exception => println("Server exception: " + e.getMessage)
        } finally {
          socket.close()
          println("Connection to client closed.")
        }
      }
    })

    sys.addShutdownHook {
      Try(pool.shutdown()) match {
        case Success(_) => println("Server shut down successfully.")
        case Failure(e) => println("Error shutting down server: " + e.getMessage)
      }
    }
  }
}
