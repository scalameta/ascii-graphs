package org.scalameta.ascii.java;

import org.scalameta.ascii.graph.Graph;

public class JavaApiTest {

  public static void main(String[] args) {
    GraphBuilder<String> graphBuilder = new GraphBuilder<String>();
    graphBuilder.addEdge("Foo", "Bar");
    graphBuilder.addVertex("Baz");
    Graph<String> graph = graphBuilder.build();

    GraphLayouter<String> graphLayouter = new GraphLayouter<String>();
    String diagram = graphLayouter.layout(graph);

    System.out.println(diagram);
  }
}
