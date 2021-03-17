package org.scalameta.ascii.layout.layering

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.graph.GraphGenerators._
import org.scalameta.ascii.layout.cycles.CycleRemover

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object LayerOrderingSpecification extends Properties("Layer ordering") {

  property("only rearranges vertices in layers") = forAll { g: Graph[String] =>
    val cycleRemovalResult = CycleRemover.removeCycles(g)
    val (layering, _) = new LayeringCalculator[String]
      .assignLayers(cycleRemovalResult)
    val newLayering = LayerOrderingCalculator.reorder(layering)
    layering.edges == newLayering.edges &&
    layering.layers.map(_.vertices.toSet) ==
      newLayering.layers.map(_.vertices.toSet)
  }

}
