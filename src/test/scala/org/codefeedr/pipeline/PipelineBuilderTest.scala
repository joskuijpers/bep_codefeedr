package org.codefeedr.pipeline

import org.apache.flink.streaming.api.scala.DataStream
import org.codefeedr.keymanager.StaticKeyManager
import org.codefeedr.pipeline.buffer.BufferType
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

class PipelineBuilderTest extends FunSuite with BeforeAndAfter with Matchers {

  case class StringType(value: String) extends PipelinedItem

  class EmptySourcePipelineObject extends PipelineObject[NoType, StringType] {
    override def transform(source: DataStream[NoType]): DataStream[StringType] = ???
  }

  class EmptyTransformPipelineObject extends PipelineObject[StringType, StringType] {
    override def transform(source: DataStream[StringType]): DataStream[StringType] = ???
  }

  class EmptySinkPipelineObject extends PipelineObject[StringType, NoType] {
    override def transform(source: DataStream[StringType]): DataStream[NoType] = ???
  }

  var builder: PipelineBuilder = _

  before {
    builder = new PipelineBuilder()
  }

  test("An empty pipeline configuration throws") {
    assertThrows[EmptyPipelineException] {
      val pipeline = builder.build()
    }
  }

  test("Set buffertype should also be retrievable") {
    builder.setBufferType(BufferType.Kafka)

    assert(builder.getBufferType == BufferType.Kafka)
  }

  test("Every pipeline object should appear in the pipeline (1)") {
    val pipeline = builder
      .append(new EmptySourcePipelineObject())
      .build()

    assert(pipeline.graph.nodes.size == 1)

    pipeline.graph.nodes.head shouldBe an[EmptySourcePipelineObject]
  }

  test("Every pipeline object should appear in the pipeline (2)") {
    val pipeline = builder
      .append(new EmptySourcePipelineObject())
      .append(new EmptyTransformPipelineObject())
      .build()

    assert(pipeline.graph.nodes.size == 2)

    pipeline.graph.nodes.head shouldBe an[EmptySourcePipelineObject]
    pipeline.graph.nodes.last shouldBe an[EmptyTransformPipelineObject]
  }

  test("Set properties should be available in pipeline properties") {
    val pipeline = builder
      .append(new EmptySourcePipelineObject())
      .setProperty("key", "value")
      .build()

    assert(pipeline.properties.get("key") == "value")
  }

  test("Set buffer properties should be available in pipeline buffer properties") {
    val pipeline = builder
      .append(new EmptySourcePipelineObject())
      .setBufferProperty("key", "value")
      .build()

    assert(pipeline.bufferProperties.get("key") == "value")
    assert(pipeline.properties.get("key") != "value")
  }

  test("A set keymanager should be forwarded to the pipeline") {
    val km = new StaticKeyManager()

    val pipeline = builder
      .append(new EmptySourcePipelineObject())
      .setKeyManager(km)
      .build()

    assert(pipeline.keyManager == km)
  }
}