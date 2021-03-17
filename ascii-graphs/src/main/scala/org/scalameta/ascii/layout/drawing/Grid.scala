package org.scalameta.ascii.layout.drawing

import org.scalameta.ascii.common.Dimension
import org.scalameta.ascii.common.Point
import org.scalameta.ascii.common.Region

/**
 * Array of characters used to render the final diagram
 */
class Grid(dimension: Dimension) {

  private val backgroundChar = ' '

  private val chars: Array[Array[Char]] =
    Array.fill(dimension.height, dimension.width)(backgroundChar)

  def apply(point: Point): Char =
    try chars(point.row)(point.column)
    catch {
      case e: ArrayIndexOutOfBoundsException =>
        throw new ArrayIndexOutOfBoundsException(
          point + " is not in " + dimension
        )
    }

  def update(point: Point, char: Char) = chars(point.row)(point.column) = char

  /**
   * Write a string into the grid starting at the given point.
   */
  def update(point: Point, s: String): Unit = {
    var p = point
    for (c <- s) {
      this(p) = c
      p = p.right
    }
  }

  private def region = Region(Point(0, 0), dimension)

  def contains(point: Point) = region.contains(point)

  override def toString = chars.map(new String(_)).mkString("\n")

}
