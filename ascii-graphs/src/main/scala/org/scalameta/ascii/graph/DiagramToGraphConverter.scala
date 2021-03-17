package org.scalameta.ascii.graph

import org.scalameta.ascii.diagram.Box
import org.scalameta.ascii.diagram.Diagram
import org.scalameta.ascii.util.Utils.makeMap

object DiagramToGraphConvertor {

  def toGraph(diagram: Diagram): Graph[String] = {
    val boxToVertexMap: Map[Box, String] = makeMap(diagram.childBoxes, _.text)

    val vertices = boxToVertexMap.values.toSet
    val edges =
      for {
        edge <- diagram.allEdges
        vertex1 <- boxToVertexMap.get(edge.box1)
        vertex2 <- boxToVertexMap.get(edge.box2)
      } yield {
        if (edge.hasArrow2)
          vertex1 -> vertex2
        else
          vertex2 -> vertex1
      }
    Graph(vertices, edges)
  }

}
