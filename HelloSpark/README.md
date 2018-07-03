# HelloSpark

## Spark WordCount程序

### 提交任务：

```
spark/spark-2.3.0-bin-hadoop2.7/bin/spark-submit --class spark.WordCount --master spark://master:7077 --executor-memory 1g --total-executor-cores 3 /chenjian/HelloSpark-1.0-SNAPSHOT.jar hdfs://master:9000/wordcount/input/abc.txt hdfs://master:9000/wordcount/xiaomaiyun
```