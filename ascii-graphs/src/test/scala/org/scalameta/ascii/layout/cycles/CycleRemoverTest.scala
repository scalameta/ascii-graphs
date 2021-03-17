package org.scalameta.ascii.layout.cycles

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.graph.GraphUtils
import org.scalameta.ascii.util.Utils

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class CycleRemoverTest extends FlatSpec with Matchers {

  check("""
         +-+   +-+ 
         |A|-->|B| 
         +-+   +-+ """)

  check("""
         +-+   +-+ 
         |A|-->|B| 
         +-+   +-+ 
          ^     |  
          |     |  
          -------  """)

  check("""
         +-+   +-+   +-+ 
         |A|-->|B|-->|C| 
         +-+   +-+   +-+ 
          ^           |  
          |           |  
          -------------  """)

  check("""
          +-+   +-+     
       -->|A|-->|C|---  
       |  +-+   +-+  |  
       |   ^     |   |  
       |   |     |   |  
       |   -------   |  
       ---------------  """)

  check("""
               +-+ 
          ---->|A| 
          |    +-+ 
          |     |  
          |     |  
          -------  """)

  check("""
          +-+ 
          |B| 
          +-+ 
           """)

  check("")

  def check(diagram: String): Unit = {
    val graph = Graph.fromDiagram(diagram)
    "Graph"
      .should("not have cycles " + ">>>\n" + graph + "\n<<<")
      .in {
        val result @ CycleRemovalResult(newGraph, reversedEdges, _) =
          CycleRemover.removeCycles(graph)

        GraphUtils.hasCycle(newGraph).should(be(false))
        val revEdgeMap = Utils.mkMultiset(reversedEdges)
        val newEdgeMap = Utils.mkMultiset(newGraph.edges)
        for (reversedEdge <- reversedEdges)
          newEdgeMap
            .getOrElse(reversedEdge, 0)
            .should(be >= (revEdgeMap(reversedEdge)))

        CycleRemoverSpecification.recoverOriginal(result).should(be(graph))
      }
  }

}
