package CoreDemo

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * 面试题：使用spark core统计每小时每个ip的top100访问domain
 */
object Dns {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .setMaster("local")

    val spark = SparkSession.builder.config(conf).getOrCreate

    spark.sparkContext.setLogLevel("ERROR")
    import spark.implicits._

    def extractHour(s: String): String = {
      s.substring(0, 13)
    }

    case class log(hour: String, ip: String, domain: String, count: Long)

    val topN = 100
    val originData = spark.sparkContext.textFile("C:\\Users\\Administrator\\IdeaProjects\\SparkDemo\\txt\\dns.txt")
      .map(_.split(" \\| ")).map(e => (extractHour(e(2)), e(0), e(1))).map((_, 1))

    originData.toDF.show(false)

    // hour ip domain
    val data = originData.reduceByKey(_ + _)
      .map(e=> log(e._1._1, e._1._2, e._1._3, e._2))
      .map(e => ((e.hour, e.ip), Seq((e.domain, e.count))))
      .reduceByKey(_ ++ _)
      .map(e => (e._1, e._2.sortBy(-_._2).take(topN)))

    data.map(e => (e._1._1, e._1._2, e._2))
      .toDF("hour", "ip", "domain_count")
      .show(false)
  }
}
