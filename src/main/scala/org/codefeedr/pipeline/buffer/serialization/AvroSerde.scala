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
package org.codefeedr.pipeline.buffer.serialization

import java.io.ByteArrayOutputStream

import com.sksamuel.avro4s.FromRecord
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.{BinaryEncoder, DatumWriter, DecoderFactory, EncoderFactory}
import org.apache.avro.reflect.{ReflectData, ReflectDatumWriter}
import org.apache.avro.specific.{SpecificDatumWriter, SpecificRecordBase}
import org.apache.flink.api.common.serialization.{AbstractDeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.typeutils.TypeExtractor

import scala.reflect.{ClassTag, classTag}

class AvroSerde[T: ClassTag](implicit val recordFrom: FromRecord[T]) extends AbstractDeserializationSchema[T] with SerializationSchema[T] {

  //Get type of the class at run time
  val inputClassType: Class[T] = classTag[T].runtimeClass.asInstanceOf[Class[T]]

  /**
    * Serializes a (generic) element into a binary format using the Avro serializer.
    *
    * @param element the element to serialized.
    * @return a serialized byte array.
    */
  override def serialize(element: T): Array[Byte] = {
    val out: ByteArrayOutputStream = new ByteArrayOutputStream()
    val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
    val writer: DatumWriter[T] = new ReflectDatumWriter[T](inputClassType)

    writer.write(element, encoder)
    encoder.flush()
    out.close()

    out.toByteArray
  }

  /**
    * Deserializes a (Avro binary) message into a (generic) case class
    *
    * @param message the message to deserialized.
    * @return a deserialized case class.
    */
  override def deserialize(message: Array[Byte]): T = {
    val schema: Schema = ReflectData.get().getSchema(inputClassType);
    val datumReader = new GenericDatumReader[GenericRecord](schema)
    val decoder = DecoderFactory.get().binaryDecoder(message, null)

    recordFrom(datumReader.read(null, decoder))
  }

  /**
    * Get type information of (de)serialized clss.
    * @return the typeinformation of the generic class.
    */
  override def getProducedType: TypeInformation[T] =
    TypeExtractor.getForClass(classTag[T].runtimeClass).asInstanceOf[TypeInformation[T]]

}