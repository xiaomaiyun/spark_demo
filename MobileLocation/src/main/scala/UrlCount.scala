import java.net.URL

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 取出学科点击的前三
  */
object UrlCount {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("UrlCount").setMaster("local[2]")
    val sc = new SparkContext(conf)

    //rdd1将数据切分，元组中放的是（URL， 1）
    val rdd1 = sc.textFile("C:\\Users\\root\\Desktop\\UserLocation\\itcast.log").map(line => {
      val f = line.split("\t")
      (f(1), 1)
    })

    //(http://php.itcast.cn/php/course.shtml,459), (http://java.itcast.cn/java/course/base.shtml,543)
    val rdd2 = rdd1.reduceByKey(_+_)
    println(rdd2.collect().toBuffer)

    //(php.itcast.cn,http://php.itcast.cn/php/course.shtml,459), (java.itcast.cn,http://java.itcast.cn/java/course/base.shtml,543)
    val rdd3 = rdd2.map(t => {
      val url = t._1
      val host = new URL(url).getHost
      (host, url, t._2)
    })
    println(rdd3.collect().toBuffer)

    val rdd4 = rdd3.groupBy(_._1).mapValues(it => {
      it.toList.sortBy(_._3).reverse.take(3)
    })


   println(rdd4.collect().toBuffer)
    sc.stop()

  }
}
