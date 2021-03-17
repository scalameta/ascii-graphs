package org.scalameta.ascii.layout.coordAssign

import org.scalameta.ascii.common.Translatable
import org.scalameta.ascii.layout.layering.RealVertex
import org.scalameta.ascii.layout.layering.Vertex
import org.scalameta.ascii.util.Utils._

/**
 * VertexInfo's for all the vertices in a single layer.
 */
case class LayerInfo(vertexInfos: Map[Vertex, VertexInfo])
    extends Translatable[LayerInfo] {

  def vertexInfo(v: Vertex): Option[VertexInfo] = vertexInfos.get(v)

  def isEmpty = vertexInfos.isEmpty

  def maxRow =
    vertexInfos.values.map(_.greaterRegion.bottomRow).fold(0)(_ max _)

  def maxColumn =
    vertexInfos.values.map(_.greaterRegion.rightColumn).fold(0)(_ max _)

  private def getSelfEdgeBuffer(vertexInfo: VertexInfo) =
    if (vertexInfo.selfInPorts.size > 0)
      (vertexInfo.selfInPorts.size + 1)
    else
      0
  def topSelfEdgeBuffer: Int =
    vertexInfos.values.map(getSelfEdgeBuffer).fold(0)(_ max _)

  def translate(down: Int = 0, right: Int = 0) =
    copy(vertexInfos = transformValues(vertexInfos)(_.translate(down, right)))

  def realVertexInfos: List[(RealVertex, VertexInfo)] =
    vertexInfos
      .toList
      .collect { case (vertex: RealVertex, info) =>
        (vertex, info)
      }

}
