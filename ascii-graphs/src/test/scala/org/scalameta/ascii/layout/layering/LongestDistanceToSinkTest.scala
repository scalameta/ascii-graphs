package org.scalameta.ascii.layout.layering

import org.scalameta.ascii.graph.Graph

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class LongestDistanceToSinkTest extends FlatSpec with Matchers {

  distancesToSinks("""
         +-+   +-+   +-+ 
         |A|-->|B|-->|C| 
         +-+   +-+   +-+ """).shouldBe("A" -> 2, "B" -> 1, "C" -> 0)

  distancesToSinks("""
         +-+   +-+   +-+ 
         |A|   |B|-->|C| 
         +-+   +-+   +-+ 
          |           ^  
          |           |  
          -------------  """).shouldBe("A" -> 1, "B" -> 1, "C" -> 0)

  distancesToSinks("""
         +-+   +-+   +-+ 
         |A|-->|B|-->|C| 
         +-+   +-+   +-+ 
          |           ^  
          |           |  
          -------------  """).shouldBe("A" -> 2, "B" -> 1, "C" -> 0)

  distancesToSinks("""
          +-+ 
          |B| 
          +-+ 
           """).shouldBe("B" -> 0)

  distancesToSinks("").shouldBe()

  def distancesToSinks(diagram: String) =
    new {
      val graph = Graph.fromDiagram(diagram)
      def shouldBe(expectedDistances: (String, Int)*): Unit = {
        "Distances to sinks"
          .should("be correct for " + ">>>\n" + graph + "\n<<<")
          .in {
            val actualDistances = LongestDistancesToSinkCalculator
              .longestDistancesToSink(graph)
            actualDistances.should(be(expectedDistances.toMap))
          }
      }
    }

  def paths[V](graph: Graph[V], v: V): List[List[V]] = {
    graph.outVertices(v) match {
      case Nil =>
        List(List[V]())
      case vs =>
        vs.flatMap(v2 => paths(graph, v2).map(v2 :: _))
    }
  }

}
