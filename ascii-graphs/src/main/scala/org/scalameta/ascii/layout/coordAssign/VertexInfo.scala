package org.scalameta.ascii.layout.coordAssign

import org.scalameta.ascii.common.Point
import org.scalameta.ascii.common.Region
import org.scalameta.ascii.common.Translatable
import org.scalameta.ascii.layout.layering.Edge
import org.scalameta.ascii.util.Utils

/**
 * Info for rendering a vertex.
 *
 * @param region
 *   - area allocated to the vertex box.
 * @param greaterRegion
 *   - area including the zone for self-edges to be drawn
 * @param inEdgeToPortMap
 *   - map of incoming edges to the points they connect to on the vertex box.
 * @param outEdgeToPortMap
 *   - map of outgoing edges to the points they connect to on the vertex box.
 */
case class VertexInfo(
    boxRegion: Region,
    greaterRegion: Region,
    inEdgeToPortMap: Map[Edge, Point],
    outEdgeToPortMap: Map[Edge, Point],
    selfInPorts: List[Point],
    selfOutPorts: List[Point]
) extends Translatable[VertexInfo] {

  /**
   * Region inside the vertex box
   */
  def contentRegion: Region =
    boxRegion.expandRight(-1).expandLeft(-1).expandDown(-1).expandUp(-1)

  def translate(down: Int = 0, right: Int = 0): VertexInfo =
    VertexInfo(
      boxRegion.translate(down, right),
      greaterRegion.translate(down, right),
      Utils.transformValues(inEdgeToPortMap)(_.translate(down, right)),
      Utils.transformValues(outEdgeToPortMap)(_.translate(down, right)),
      selfInPorts.map(_.translate(down, right)),
      selfOutPorts.map(_.translate(down, right))
    )

  def setLeft(column: Int): VertexInfo =
    translate(right = column - boxRegion.leftColumn)

}
