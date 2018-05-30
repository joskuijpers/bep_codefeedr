/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codefeedr.stages

import org.apache.flink.streaming.api.scala.DataStream
import org.codefeedr.buffer.serialization.{ AvroSerde}
import org.codefeedr.pipeline._

import scala.reflect.{ClassTag, Manifest}
import scala.reflect.runtime.universe._

/**
  * The OutputStage class represents the end of a pipeline.
  * It has an input type but no specific output type since it will not be connected to the buffer.
  *
  * @tparam In the input type of the job.
  */
abstract class OutputStage[In <: PipelineItem : ClassTag : TypeTag : AvroSerde](attributes: StageAttributes = StageAttributes()) extends PipelineObject[In, NoType](attributes) {

  override def transform(source: DataStream[In]): DataStream[NoType] = {
    main(source)

    null
  }

  /**
    * Use the given datastream
    *
    * @param source Stream
    */
  def main(source: DataStream[In]): Unit
}

abstract class OutputStage2[In <: PipelineItem : ClassTag : TypeTag : AvroSerde, In2 <: PipelineItem : ClassTag : TypeTag : AvroSerde](attributes: StageAttributes = StageAttributes()) extends PipelineObject2[In, In2, NoType](attributes) {

  override def transform(source: DataStream[In], secondSource: DataStream[In2]): DataStream[NoType] = {
    main(source, secondSource)

    null
  }

  /**
    * Use the given datastreams
    *
    * @param source
    * @param secondSource Stream
    */
  def main(source: DataStream[In], secondSource: DataStream[In2]): Unit
}

abstract class OutputStage3[In <: PipelineItem : ClassTag : TypeTag : AvroSerde, In2 <: PipelineItem : ClassTag : TypeTag : AvroSerde, In3 <: PipelineItem : ClassTag : TypeTag : AvroSerde](attributes: StageAttributes = StageAttributes()) extends PipelineObject3[In, In2, In3, NoType](attributes) {

  override def transform(source: DataStream[In], secondSource: DataStream[In2], thirdSource: DataStream[In3]): DataStream[NoType] = {
    main(source, secondSource, thirdSource)

    null
  }

  /**
    * Use the given datastreams
    *
    * @param source
    * @param secondSource Stream
    * @param thirdSource Stream
    */
  def main(source: DataStream[In], secondSource: DataStream[In2], thirdSource: DataStream[In3]): Unit
}

abstract class OutputStage4[In <: PipelineItem : ClassTag : TypeTag : AvroSerde, In2 <: PipelineItem : ClassTag : TypeTag: AvroSerde, In3 <: PipelineItem : ClassTag : TypeTag : AvroSerde, In4 <: PipelineItem : ClassTag : TypeTag : AvroSerde](attributes: StageAttributes = StageAttributes()) extends PipelineObject4[In, In2, In3, In4, NoType](attributes) {

  override def transform(source: DataStream[In], secondSource: DataStream[In2], thirdSource: DataStream[In3], fourthSource: DataStream[In4]): DataStream[NoType] = {
    main(source, secondSource, thirdSource, fourthSource)

    null
  }

  /**
    * Use the given datastreams
    *
    * @param source Stream
    * @param secondSource Stream
    * @param thirdSource Stream
    * @param fourthSource Stream
    */
  def main(source: DataStream[In], secondSource: DataStream[In2], thirdSource: DataStream[In3], fourthSource: DataStream[In4]): Unit
}