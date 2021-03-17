package org.scalameta.ascii.layout.coordAssign

import org.scalameta.ascii.common.Dimension

/**
 * Control how a vertex box gets sized and populated in a graph drawing.
 */
trait VertexRenderingStrategy[-V] {

  def getPreferredSize(v: V): Dimension

  /**
   * @return
   *   rows of text that should fit within the given allocatedSize
   */
  def getText(v: V, allocatedSize: Dimension): List[String]

}
