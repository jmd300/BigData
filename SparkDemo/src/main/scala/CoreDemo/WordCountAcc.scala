package CoreDemo

import org.apache.spark.{SparkConf, SparkContext}

object WordCountAcc {
  def main(args: Array[String]): Unit = {
    val sparConf = new SparkConf().setMaster("local").setAppName("Acc")
    val sc = new SparkContext(sparConf)

    val rdd = sc.makeRDD(List("hello", "spark", "hello"))

    // 累加器 : WordCount
    // 创建累加器对象
    val wcAcc = new MyAccumulator()

    // 向Spark进行注册
    sc.register(wcAcc, "wordCountAcc")

    rdd.foreach(e => wcAcc.add(e))

    println(wcAcc.value)
    sc.stop()
  }
}
