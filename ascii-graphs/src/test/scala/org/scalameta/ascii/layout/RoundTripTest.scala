package org.scalameta.ascii.layout

import org.scalameta.ascii.graph.Graph
import org.scalameta.ascii.layout.RoundTripSpecification._

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class RoundTripTest extends FlatSpec with Matchers {

  "Round trip"
    .should("not overwrite an arrow")
    .in {
      checkRoundTrip(
        Graph.fromDiagram("""
        ╭────╮   ╭──────╮    
        │aaff│   │afcfad│    
        │ db │   │ ced  │    
        ╰───┬╯   ╰──┬─┬─╯    
            │       │ │      
            │       │ ╰─────╮
            │       ╰──╮    │
            │          │    │
            v          v    │
      ╭───────────╮ ╭─────╮ │
      │ddabcfabcbe│ │eeed │ │
      │   ccfda   │ │ cb  │ │
      ╰────┬──────╯ ╰──┬──╯ │
           │           │ ^  │  <-- was a bug where the edge segment below would be elevated into the arrow
           │   ╭───────┼─┼──╯
           │   │       │ │   
           v   v       │ │   
      ╭────────────╮   v │   
      │     d      │ ╭───┴─╮ 
      │     d      │ │  c  │ 
      │baecababeedc│ ╰─────╯ 
      ╰────────────╯         
  """),
        unicodeLayoutPrefs
      ).should(be(true))
    }

}
