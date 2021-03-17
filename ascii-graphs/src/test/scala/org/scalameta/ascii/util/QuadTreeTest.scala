package org.scalameta.ascii.util

import org.scalameta.ascii.common._

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class QuadTreeTest extends FlatSpec with Matchers {

  "A QuadTree"
    .should("work with one element")
    .in {
      val tree = new QuadTree[Region](Dimension(16, 16))
      val region = Region(Point(1, 1), Point(2, 2))
      tree.add(region)
      tree.collides(region).should(be(true))
      tree.collides(Region(Point(4, 4), Point(6, 7))).should(be(false))
      tree.remove(region)
      tree.collides(region).should(be(false))
    }

  it.should("work with multiple elements")
    .in {
      val tree = new QuadTree[Region](Dimension(16, 16))
      tree.add(Region(Point(1, 1), Point(2, 2)))
      tree.add(Region(Point(1, 1), Point(2, 3)))
      tree.add(Region(Point(3, 3), Point(4, 4)))
      tree.collides(Region(Point(2, 2), Point(3, 3))).should(be(true))
    }
}
