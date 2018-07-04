import org.apache.spark.{SparkConf, SparkContext}

object UserLocation {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("UserLocation").setMaster("local[2]")

    val sc=new SparkContext(conf)

    val mobileLocation=sc.textFile("C:\\Users\\root\\Desktop\\UserLocation\\19735E1C66.log").map(line=>{
      val fields=line.split(",")
      val eventType=fields(3)
      var time=fields(1)
      val timeLong= if (eventType=="1") -time.toLong else time.toLong

      (fields(0)+"_"+fields(2),timeLong)

    })

    println(mobileLocation.collect().toBuffer)

    //(18611132889_16030401EAFB68F1E3CDF819735E1C66,97500)
    val tmpResult=mobileLocation.groupBy(_._1).mapValues(_.foldLeft(0L)(_+_._2))

    val rdd2=tmpResult.map(t=>{
      val mobile_bs=t._1
      val mobile=mobile_bs.split("_")(0)
      val lac=mobile_bs.split("_")(1)

      val time=t._2

      (mobile,lac,time)
    })

    println(tmpResult.collect().toBuffer)

    //(18611132889,16030401EAFB68F1E3CDF819735E1C66,97500)
    val rdd3=rdd2.groupBy(_._1)


    println(rdd3.collect().toBuffer)

    //List((18611132889,16030401EAFB68F1E3CDF819735E1C66,97500))
    val rdd4=rdd3.mapValues(it=>{
      it.toList.sortBy(_._3).reverse.take(2)
    })

    println(rdd4.collect().toBuffer)
    sc.stop()

  }

}
