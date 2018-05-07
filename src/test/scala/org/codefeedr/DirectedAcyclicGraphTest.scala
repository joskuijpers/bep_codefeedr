package org.codefeedr

import org.scalatest.FunSuite

class DirectedAcyclicGraphTest extends FunSuite {

  val nodeA = "a"
  val nodeB = "b"
  val nodeC = "c"
  val nodeD = "d"

  test("Added nodes are testable") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)

    assert(dag.hasNode(nodeA))
    assert(!dag.hasNode(nodeB))
  }

  test("Added edges are added") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addEdge(nodeA, nodeB)

    assert(dag.hasEdge(nodeA, nodeB))
  }

  test("Adding edges for nodes that do not exist throws") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)

    assertThrows[IllegalArgumentException] {
      dag.addEdge(nodeA, nodeB)
    }

    assertThrows[IllegalArgumentException] {
      dag.addEdge(nodeC, nodeA)
    }
  }

  test("Adding an edge that causes a cycle throws") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addEdge(nodeA, nodeB)
      .addEdge(nodeB, nodeC)

    assertThrows[IllegalStateException] {
      dag.addEdge(nodeC, nodeA)
    }
  }

  test("Parents are retrievable") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addEdge(nodeA, nodeC)
      .addEdge(nodeB, nodeC)

    assert(dag.getParents(nodeC) == Set(nodeA, nodeB))
  }

  test("Children are retrievable") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addEdge(nodeA, nodeC)
      .addEdge(nodeB, nodeC)

    assert(dag.getChildren(nodeA) == Set(nodeC))
    assert(dag.getChildren(nodeB) == Set(nodeC))
  }

  test("Adding an existing edge does not affect the graph") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addEdge(nodeA, nodeB)

    val dag2 = dag.addEdge(nodeA, nodeB)

    assert(dag == dag2)
  }

  test("Sequential DAG should be detected") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addEdge(nodeA, nodeB)
      .addEdge(nodeB, nodeC)

    assert(dag.isSequential)
  }

  test("Non-sequential DAG should be detected") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addNode(nodeD)
      .addEdge(nodeA, nodeB)
      .addEdge(nodeA, nodeC)
      .addEdge(nodeC, nodeD)
      .addEdge(nodeB, nodeD)

    assert(!dag.isSequential)
  }

  test("Any orphans will be removed") {
    val dag = new DirectedAcyclicGraph()
      .addNode(nodeA)
      .addNode(nodeB)
      .addNode(nodeC)
      .addNode(nodeD)
      .addEdge(nodeA, nodeB)
      .addEdge(nodeA, nodeC)

    assert(dag.withoutOrphans.nodes == Set(nodeA, nodeB, nodeC))
  }

  test("Equality operator") {
    val dag = new DirectedAcyclicGraph()
    val dag2 = new DirectedAcyclicGraph()
    val dag3 = new DirectedAcyclicGraph().addNode(nodeA)

    assert(dag == dag2)
    assert(dag2 != dag3)
  }
}
