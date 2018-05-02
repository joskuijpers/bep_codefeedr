package org.codefeedr

import org.scalatest.FunSuite

class PropertiesTest extends FunSuite {

  test("An empty properties should have no keys") {
    val props = new Properties()
    assert(props.keys().isEmpty)
  }

  test("An added value should show up in the keys list") {
    val props = new Properties()
    props.set("key", "value")
    assert(props.keys().contains("key"))
  }

  test("Setting multiple keys is supported") {
    val props = new Properties()
    props.set("keyOne", "one")
    props.set("two", "another")

    assert(props.get("keyOne") == "one")
    assert(props.get("two") == "another")
  }

  test("An added value should be retrievable again") {
    val props = new Properties()
    props.set("key", "value")
    assert(props.get("key") == "value")
  }

  test("Getting with a default value should work on empty items") {
    val props = new Properties()
    assert(props.get("doesNotExist", "default") == "default")
  }

  test("Getting with a default value on an existing item should return the value") {
    val props = new Properties()
    props.set("key", "value")
    assert(props.get("key", "default") == "value")
  }

//  test("A Java properties list created from the properties should be comparable in content") {
//    val props = new Properties()
//    props.set("key", "value")
//
//    val jProps = props.toJavaProperties
//
//    assert(props.keys().equals(jProps.keys()))
//  }
//
//  test("An ImmutableProperties created from the properties should be comparable in content") {
//    val props = new Properties()
//    props.set("key", "value")
//
//    val iProps = props.toImmutable
//
//    assert(props.keys().equals(iProps.keys()))
//  }

  test("An ImmutableProperties should not allow setting values") {
    val props = new Properties()
    props.set("key", "value")

    val iProps = props.toImmutable

    assertThrows[NotImplementedError] {
      iProps.set("key", "newValue")
    }
  }

}
