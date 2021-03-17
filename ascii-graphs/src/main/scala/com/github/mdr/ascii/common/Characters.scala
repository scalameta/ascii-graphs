package com.github.mdr.ascii.common

import scala.PartialFunction.cond

import com.github.mdr.ascii.common.Direction._

object Characters {

  def isAheadArrow(c: Char, direction: Direction): Boolean =
    cond(c, direction) {
      case ('^', Up) =>
        true
      case ('v' | 'V', Down) =>
        true
      case ('<', Left) =>
        true
      case ('>', Right) =>
        true
    }

  def isLeftArrow(c: Char, direction: Direction) =
    isAheadArrow(c, direction.turnLeft)

  def isRightArrow(c: Char, direction: Direction) =
    isAheadArrow(c, direction.turnRight)

}
