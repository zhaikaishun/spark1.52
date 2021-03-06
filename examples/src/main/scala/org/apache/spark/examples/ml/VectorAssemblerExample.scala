/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package org.apache.spark.examples.ml

// $example on$
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.mllib.linalg.Vectors
// $example off$
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{SQLContext, DataFrame}
/**
*VectorAssembler是一个转换器,它将给定的若干列合并为一列向量
**/
object VectorAssemblerExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("VectorAssemblerExample").setMaster("local[4]")
    val sc = new SparkContext(conf)
  
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._

    // $example on$
    val dataset = sqlContext.createDataFrame(
      Seq((0, 18, 1.0, Vectors.dense(0.0, 10.0, 0.5), 1.0))
    ).toDF("id", "hour", "mobile", "userFeatures", "clicked")
    //VectorAssembler是一个转换器,它将给定的若干列合并为一列向量
    val assembler = new VectorAssembler()
      .setInputCols(Array("hour", "mobile", "userFeatures"))
      .setOutputCol("features")
    //transform()方法将DataFrame转化为另外一个DataFrame的算法
    val output = assembler.transform(dataset)
    /**
      +---+----+------+--------------+-------+--------------------+
      | id|hour|mobile|  userFeatures|clicked|            features|
      +---+----+------+--------------+-------+--------------------+
      |  0|  18|   1.0|[0.0,10.0,0.5]|    1.0|[18.0,1.0,0.0,10....|
      +---+----+------+--------------+-------+--------------------+*/
    output.show()
    /*
     * [[18.0,1.0,0.0,10.0,0.5],1.0]
     */
    println(output.select("features", "clicked").first())
    // $example off$

    sc.stop()
  }
}
// scalastyle:on println
