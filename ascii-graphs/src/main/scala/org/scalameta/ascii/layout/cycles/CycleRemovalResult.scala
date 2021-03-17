package org.scalameta.ascii.layout.cycles

import org.scalameta.ascii.graph.Graph

case class CycleRemovalResult[V](
    dag: Graph[V],
    reversedEdges: List[(V, V)],
    selfEdges: List[(V, V)]
) {

  def countSelfEdges(v: V): Int = selfEdges.count(_._1 == v)

}
