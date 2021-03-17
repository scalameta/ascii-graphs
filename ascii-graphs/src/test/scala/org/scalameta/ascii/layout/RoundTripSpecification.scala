package org.scalameta.ascii.layout

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.graph.GraphGenerators._
import org.scalameta.ascii.layout.prefs.LayoutPrefs
import org.scalameta.ascii.layout.prefs.LayoutPrefsImpl

import org.scalacheck.Prop._
import org.scalacheck.Properties

object RoundTripSpecification extends Properties("RoundTrip") {

  val unicodeLayoutPrefs = LayoutPrefsImpl(
    unicode = true,
    removeKinks = true,
    compactify = true,
    vertical = true
  )

  property("unicode round trip") = forAll { g: Graph[String] =>
    checkRoundTrip(g, unicodeLayoutPrefs)
  }

  if (
    false
  ) // Shake out some bugs, probably compacting edges too eagerly in ASCII mode
    property("ascii round trip") = forAll { g: Graph[String] =>
      val layoutPrefs = LayoutPrefsImpl(
        unicode = false,
        removeKinks = true,
        compactify = true,
        vertical = true,
        explicitAsciiBends = true
      )
      checkRoundTrip(g, layoutPrefs)
    }

  def checkRoundTrip(g: Graph[String], layoutPrefs: LayoutPrefs): Boolean = {
    val rendered = GraphLayout.renderGraph(g, layoutPrefs = layoutPrefs)
    val graphAgain = removeWhitespace(Graph.fromDiagram(rendered))
    val originalGraph = removeWhitespace(g)
    val res = graphAgain == originalGraph
    if (!res) {
      println("Original:\n" + originalGraph)
      println("Again:\n" + graphAgain)
    }
    graphAgain == originalGraph
  }

  private def removeWhitespace(g: Graph[String]): Graph[String] =
    g.map(_.filterNot(_.isWhitespace))

}
