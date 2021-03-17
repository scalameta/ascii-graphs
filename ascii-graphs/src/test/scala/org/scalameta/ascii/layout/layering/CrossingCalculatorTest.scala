package org.scalameta.ascii.layout.layering

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.layout.cycles.CycleRemover

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class CrossingCalculatorTest extends FlatSpec with Matchers {

  // Note: layer ordering in test data is done alphabetically

  check(
    """
     +---+ +---+ +---+
     | A | | B | | C |
     +---+ +---+ +---+
       |     |     |  
       v     v     v  
     +---+ +---+ +---+
     | X | | Y | | Z |
     +---+ +---+ +---+
      """,
    expectedCrossings = 0
  )

  check(
    """
     +---+ +---+ +---+
     | A | | B | | C |
     +---+ +---+ +---+
       |     |     |  
     --+------     |  
     | |           |  
     | -------     |  
     ---     |     |  
       |     |     |  
       v     v     v  
     +---+ +---+ +---+
     | X | | Y | | Z |
     +---+ +---+ +---+
      """,
    expectedCrossings = 1
  )

  check(
    """
     +---+ +---+ +---+
     | A | | B | | C |
     +---+ +---+ +---+
       |     |     |  
    ---+-----+------  
    |  |     |        
    |  |     -------  
    |  -------     |  
    ----     |     |  
       |     |     |  
       v     v     v  
     +---+ +---+ +---+
     | X | | Y | | Z |
     +---+ +---+ +---+
      """,
    expectedCrossings = 2
  )

  private def check(diagram: String, expectedCrossings: Int) = {
    val graph = Graph.fromDiagram(diagram)
    "CrossingCalculator"
      .should("find " + expectedCrossings + " in \n" + diagram)
      .in {
        val layering = getLayering(graph)
        val List(layer1, layer2) = layering.layers.map(sortVertices)
        val edges = layering.edgesInto(layer2)
        val crossingCalculator = new CrossingCalculator(layer1, layer2, edges)
        crossingCalculator.numberOfCrossings.should(be(expectedCrossings))
      }
  }

  private def sortVertices(layer: Layer) =
    layer.copy(layer.vertices.sortBy(_.toString))

  private def getLayering(graph: Graph[String]): Layering = {
    val cycleRemovalResult = CycleRemover.removeCycles(graph)
    val (layering, _) = new LayeringCalculator[String]
      .assignLayers(cycleRemovalResult)
    layering
  }

}
