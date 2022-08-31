package CoreDemo

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .setMaster("local")

    val spark = SparkSession.builder.config(conf).getOrCreate

    spark.sparkContext.setLogLevel("ERROR")
    import spark.implicits._

    val data = spark.sparkContext.textFile("txt/word.txt")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .toDF("word", "count")

    data.show
  }
}
