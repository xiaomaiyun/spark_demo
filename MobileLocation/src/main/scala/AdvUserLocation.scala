import org.apache.spark.{SparkConf, SparkContext}

object AdvUserLocation {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("UserLocation").setMaster("local[2]")

    val sc = new SparkContext(conf)

    //(18688888888,16030401EAFB68F1E3CDF819735E1C66,-20160327082400)
    val rdd0 = sc.textFile("C:\\Users\\root\\Desktop\\UserLocation\\19735E1C66.log").map(line => {
      val fields = line.split(",")
      val eventType = fields(3)
      val time = fields(1)
      val timeLong = if (eventType == "1") -time.toLong else time.toLong

      ((fields(0), fields(2)), timeLong)

    })
    println(rdd0.collect().toBuffer)
    val rdd1 = rdd0.reduceByKey(_ + _).map(t=>{
      val mobile=t._1._1
      val lac=t._1._2
      val time=t._2
      (lac,(mobile,time))
    })

    val rdd2=sc.textFile("C:\\Users\\root\\Desktop\\UserLocation\\loc_info.txt").map(line=>{
      val f=line.split(",")
      //(基站ID， （经度，纬度）)
      (f(0),(f(1),f(2)))
    })

    val rdd3=rdd1.join(rdd2).map(t=>{
      val lac=t._1
      val mobile=t._2._1._1
      val time=t._2._1._2
      val x=t._2._2._1
      val y=t._2._2._2
      (mobile,lac,time,x,y)
    })
    println(rdd3.collect().toBuffer)


    //rdd4分组后的
    val rdd4=rdd3.groupBy(_._1)

    val rdd5=rdd4.mapValues(it=>{
      it.toList.sortBy(_._3).reverse.take(2)
    })

    println(rdd5.collect().toBuffer)
    rdd5.saveAsTextFile("C:\\Users\\root\\Desktop\\UserLocation\\out")


  }

}
