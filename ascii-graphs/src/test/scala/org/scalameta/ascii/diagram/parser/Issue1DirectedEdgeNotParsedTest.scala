package org.scalameta.ascii.diagram.parser

import org.scalameta.ascii.diagram.Diagram
import org.scalameta.ascii.graph.Graph

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class Issue1DirectedEdgeNotParsedTest extends FlatSpec with Matchers {

  "Parser"
    .should("detect directed edge")
    .in {

      val diagram = Diagram("""
      +-------+         
      | op1   |         
      +-------+         
        |               
        |               
        ----            
           |            
           v            
        +-----+         
        |gbk1 |         
        +-----+         """)

      {
        val boxes = diagram.allBoxes
        val Some(op1) = boxes.find(_.text.contains("op1"))
        val Some(gbk1) = boxes.find(_.text.contains("gbk1"))
        val List(edge) = op1.edges
        edge.hasArrow1.should(be(false))
        edge.hasArrow2.should(be(true))
      }

      {
        val graph = Graph.fromDiagram(diagram)
        val Some(op1) = graph.vertices.find(_.contains("op1"))
        val Some(gbk1) = graph.vertices.find(_.contains("gbk1"))
        val List(edge) = graph.edges
        edge.should(be((op1, gbk1)))
      }

    }

}
