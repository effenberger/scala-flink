name := "scala-flink"

version := "0.1"

scalaVersion := "2.12.15"

resolvers += "Apache Flink" at "https://repo.maven.apache.org/maven2/org/apache/flink/"


libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % "1.13.2",
  "org.apache.flink" %% "flink-streaming-scala" % "1.13.2",
  "org.apache.flink" % "flink-clients_2.12" % "1.13.2"
)