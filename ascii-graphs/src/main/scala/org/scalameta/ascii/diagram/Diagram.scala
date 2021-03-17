package org.scalameta.ascii.diagram

import org.scalameta.ascii.common._
import org.scalameta.ascii.diagram.parser._

object Diagram {

  @throws(classOf[DiagramParserException])
  def apply(s: String): Diagram = new DiagramParser(s).getDiagram

}

trait Diagram extends Container {

  /**
   * @return
   *   all boxes in diagram, whether top level or nested
   */
  def allBoxes: List[Box]

  def allEdges: List[Edge]

  def parent: Option[Container] = None

  def boxAt(point: Point): Option[Box]

}
