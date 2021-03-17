package org.scalameta.ascii.layout.coordAssign

import org.scalameta.ascii.util.Utils._

/**
 * Calculate vertical ordering of row bends
 */
class EdgeBendCalculator(
    edgeInfos: List[EdgeInfo],
    edgeZoneTopRow: Int,
    selfEdgeBuffer: Int
) {

  private val edgeRows: Map[EdgeInfo, Int] = orderEdgeBends(edgeInfos)
  require(
    edgeInfos.forall { edge =>
      edge.isStraight || edgeRows.contains(edge)
    }
  )

  private def bendRow(rowIndex: Int) = edgeZoneTopRow + rowIndex * 1 + 1

  val edgeZoneBottomRow =
    (if (edgeInfos.isEmpty) // No edges
       -1
     else if (edgeRows.isEmpty) // No edges with bends
       edgeZoneTopRow + 2
     else
       bendRow(edgeRows.values.max) + 2) + selfEdgeBuffer

  def bendRow(edgeInfo: EdgeInfo): Int = bendRow(edgeRows(edgeInfo))

  /**
   * @return
   *   a map of the edges that require bends to a unique row number.
   */
  private def orderEdgeBends(edgeInfos: List[EdgeInfo]): Map[EdgeInfo, Int] = {
    // We sort so as to avoid unnecessary crossings of edges in or out of a common vertex:
    //
    //  ╭─────╮                  ╭─────╮
    //  │  a  │                  │  a  │
    //  ╰─┬─┬─╯                  ╰─┬─┬─╯
    //    │ │                      │ │
    //    ╰─┼─╮             vs     │ ╰──────────╮
    //      ╰─┼────────╮           ╰───╮        │
    //        │        │               │        │
    //        v        v               v        v
    //  ╭──────────╮ ╭───╮       ╭──────────╮ ╭───╮
    //  │aaaaaaaaaa│ │ b │       │aaaaaaaaaa│ │ b │
    //  ╰──────────╯ ╰───╯       ╰──────────╯ ╰───╯
    //
    //  ╭──────────╮ ╭───╮       ╭──────────╮ ╭───╮
    //  │aaaaaaaaaa│ │ b │       │aaaaaaaaaa│ │ b │
    //  ╰─────┬────╯ ╰─┬─╯       ╰─────┬────╯ ╰─┬─╯
    //        │        │               │        │
    //      ╭─┼────────╯    vs     ╭───╯        │
    //    ╭─┼─╯                    │ ╭──────────╯
    //    │ │                      │ │
    //    v v                      v v
    //  ╭─────╮                  ╭─────╮
    //  │  a  │                  │  a  │
    //  ╰─────╯                  ╰─────╯

    /**
     * Magic ranking to ensure that the edge bends ordered this way minimise
     * crossings.
     */
    def edgeRank(edgeInfo: EdgeInfo): Int = {
      val startColumn = edgeInfo.startColumn
      val finishColumn = edgeInfo.finishColumn
      signum(startColumn - finishColumn) * finishColumn
    }

    val orderedEdges = edgeInfos.filter(_.requiresBend).sortBy(edgeRank)
    val edgeToRowMap: Map[EdgeInfo, Int] = orderedEdges.zipWithIndex.toMap
    reorderEdgesWithSameStartAndEndColumns(edgeToRowMap)
  }

  /**
   * Reorder edges that share start and end columns as to avoid edges being
   * drawn over each other.
   *
   * NOTE: edge nudging means that this routine isn't needed, for now. However,
   * if we nudge just the vertices required to break edge "cycles", this can be
   * used to remove any other overlaps.
   */
  private def reorderEdgesWithSameStartAndEndColumns(
      edgeToRowMap: Map[EdgeInfo, Int]
  ): Map[EdgeInfo, Int] = {
    var updatedEdgeToRowMap = edgeToRowMap
    var continue = true
    var swappedEdges = Set[(EdgeInfo, EdgeInfo)]()
    while (continue) {
      continue = false
      for {
        edgeInfo1 @ EdgeInfo(_, _, start1, finish1, _) <- edgeToRowMap.keys
        edgeInfo2 @ EdgeInfo(_, _, start2, finish2, _) <- edgeToRowMap.keys
        if edgeInfo1 != edgeInfo2
        if start1.column == finish2.column
        if start2.column !=
          finish1.column // Prevents an infinite loop (issue #3)
        row1 = updatedEdgeToRowMap(edgeInfo1)
        row2 = updatedEdgeToRowMap(edgeInfo2)
        if row1 > row2
        if !swappedEdges.contains(
          (edgeInfo1, edgeInfo2)
        ) // Prevents more involved infinite loops
      } {
        updatedEdgeToRowMap += edgeInfo1 -> row2
        updatedEdgeToRowMap += edgeInfo2 -> row1
        swappedEdges += ((edgeInfo1, edgeInfo2))
        continue = true
      }
    }
    updatedEdgeToRowMap
  }

}
