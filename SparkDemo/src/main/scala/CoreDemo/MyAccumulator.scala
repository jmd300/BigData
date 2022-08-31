package CoreDemo

import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable


/*
  自定义数据累加器：WordCount

  1. 继承AccumulatorV2, 定义泛型
     IN : 累加器输入的数据类型 String
     OUT : 累加器返回的数据类型 mutable.Map[String, Long]

  2. 重写方法
 */
class MyAccumulator extends AccumulatorV2[String, mutable.Map[String, Long]] {

  private val wcMap = mutable.Map[String, Long]()

  // 判断是否初始状态
  override def isZero: Boolean = wcMap.isEmpty

  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = new MyAccumulator()

  override def reset(): Unit = wcMap.clear

  // 累加器add数据
  override def add(word: String): Unit = {
    val newCnt = wcMap.getOrElse(word, 0L) + 1
    wcMap.update(word, newCnt)
  }

  // Driver合并多个累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {

    val map1 = this.wcMap
    val map2 = other.value

    map2.foreach {
      case (word, count) =>
        val newCount = map1.getOrElse(word, 0L) + count
        map1.update(word, newCount)
    }
  }

  // 累加器结果
  override def value: mutable.Map[String, Long] = wcMap
}
