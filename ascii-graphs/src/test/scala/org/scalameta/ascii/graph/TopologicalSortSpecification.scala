package org.scalameta.ascii.graph

import org.scalameta.ascii.graph.GraphGenerators._
import org.scalameta.ascii.layout.cycles.CycleRemover

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

object TopologicalSortSpecification extends Properties("Topological sort") {

  property("no out-of-order edges") =
    forAll(dags) { g: Graph[String] =>
      val Some(ordering) = GraphUtils.topologicalSort(g)
      val (_, reversedEdges) = new CycleRemover[String].reflowGraph(g, ordering)
      reversedEdges.isEmpty
    }

  property("must cover all vertices") =
    forAll(dags) { g: Graph[String] =>
      val Some(ordering) = GraphUtils.topologicalSort(g)
      ordering.sorted == g.vertices.toList.sorted
    }

}
