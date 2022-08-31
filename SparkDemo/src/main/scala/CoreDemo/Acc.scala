package CoreDemo

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 累加器Demo
 */
object Acc {
  def main(args: Array[String]): Unit = {
    val sparConf = new SparkConf().setMaster("local").setAppName("Acc")
    val sc = new SparkContext(sparConf)

    val rdd = sc.makeRDD(List(1, 2, 3, 4))

    // Spark默认就提供了简单数据聚合的累加器
    val sumAcc = sc.longAccumulator("sum")
    print(sumAcc.isZero)

    //sc.doubleAccumulator
    //sc.collectionAccumulator

    rdd.foreach(e => sumAcc.add(e))

    println(sumAcc.value)

    sc.stop()
  }
}
