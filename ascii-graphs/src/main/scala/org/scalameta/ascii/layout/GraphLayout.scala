package org.scalameta.ascii.layout

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.layout.coordAssign._
import org.scalameta.ascii.layout.cycles.CycleRemover
import org.scalameta.ascii.layout.drawing._
import org.scalameta.ascii.layout.layering._
import org.scalameta.ascii.layout.prefs.LayoutPrefs
import org.scalameta.ascii.layout.prefs.LayoutPrefsImpl

object GraphLayout {

  /**
   * Layout a graph as a String using toString() on the vertices
   */
  def renderGraph[V](graph: Graph[V]): String =
    renderGraph(graph, ToStringVertexRenderingStrategy, LayoutPrefsImpl())

  def renderGraph[V](
      graph: Graph[V],
      vertexRenderingStrategy: VertexRenderingStrategy[V] =
        ToStringVertexRenderingStrategy,
      layoutPrefs: LayoutPrefs = LayoutPrefsImpl()
  ): String = {
    val cycleRemovalResult = CycleRemover.removeCycles(graph)
    val (layering, _) = new LayeringCalculator[V]
      .assignLayers(cycleRemovalResult)
    val reorderedLayering = LayerOrderingCalculator.reorder(layering)
    val layouter =
      new Layouter(ToStringVertexRenderingStrategy, layoutPrefs.vertical)
    var drawing = layouter.layout(reorderedLayering)

    if (layoutPrefs.removeKinks)
      drawing = KinkRemover.removeKinks(drawing)
    if (layoutPrefs.elevateEdges)
      drawing = EdgeElevator.elevateEdges(drawing)
    if (layoutPrefs.compactify)
      drawing = RedundantRowRemover.removeRedundantRows(drawing)

    if (!layoutPrefs.vertical)
      drawing = drawing.transpose
    Renderer.render(drawing, layoutPrefs)
  }

}
