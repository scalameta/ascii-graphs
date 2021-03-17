package org.scalameta.ascii.layout.coordAssign

import org.scalameta.ascii.common.Point
import org.scalameta.ascii.layout.layering.Vertex

/**
 * Information about edges that pass between two adjacent layers.
 *
 * @param finishPort
 *   -- column is correct, but not row
 */
case class EdgeInfo(
    startVertex: Vertex,
    finishVertex: Vertex,
    startPort: Point,
    finishPort: Point,
    reversed: Boolean
) {

  def startColumn = startPort.column

  def finishColumn = finishPort.column

  def requiresBend = !isStraight

  def isStraight = startColumn == finishColumn

  def withFinishColumn(column: Int) =
    copy(finishPort = finishPort.withColumn(column))

}
