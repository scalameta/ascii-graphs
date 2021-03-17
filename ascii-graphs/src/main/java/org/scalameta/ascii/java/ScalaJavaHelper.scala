package org.scalameta.ascii.java

import java.{util => ju}

import scala.collection.JavaConverters._

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.layout.GraphLayout
import org.scalameta.ascii.layout.coordAssign.ToStringVertexRenderingStrategy
import org.scalameta.ascii.layout.prefs.LayoutPrefs

object ScalaJavaHelper {

  def asScalaList[V](xs: ju.List[V]): List[V] = xs.asScala.toList

  def asScalaSet[V](xs: ju.Set[V]): Set[V] = xs.asScala.toSet

  def tuple[T](t1: T, t2: T): (T, T) = (t1, t2)

  def renderGraph[V](graph: Graph[V], layoutPrefs: LayoutPrefs): String =
    GraphLayout.renderGraph(graph, ToStringVertexRenderingStrategy, layoutPrefs)

}
