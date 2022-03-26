package io.cloudneutral.shortestpath.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.cloudneutral.shortestpath.graph.WeightedGraph;
import io.cloudneutral.shortestpath.graph.algorithm.SearchAlgorithm;

public class ShortestPathSearchTest {
    private WeightedGraph<String> weightedGraph() {
        WeightedGraph<String> graph = new WeightedGraph<>();

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");

        graph.addUndirectedEdge("A", "B", 3d);
        graph.addUndirectedEdge("A", "C", 2d);
        graph.addUndirectedEdge("B", "D", 6d);
        graph.addUndirectedEdge("B", "C", 4d);
        graph.addUndirectedEdge("C", "E", 8d);
        graph.addUndirectedEdge("D", "E", 3d);
        graph.addUndirectedEdge("D", "F", 5d);
        graph.addUndirectedEdge("E", "F", 4d);

        return graph;
    }

    @Test
    public void testDijkstraAlgorithm() {
        WeightedGraph<String> graph = weightedGraph();

        SearchAlgorithm<String> algorithm = graph.dijkstrasAlgorithm();

        List<String> path = algorithm.findShortestPath("A", "F");

        System.out.printf("Shortest path A->F is: %s\n", path);

        Assertions.assertEquals(Arrays.asList("A", "B", "D", "F"), path);
    }

    @Test
    public void testAStarAlgorithm() {
        WeightedGraph<String> graph = weightedGraph();

        Map<String, Integer> hValues = new TreeMap<>();
        hValues.put("A", 100);
        hValues.put("B", 100);
        hValues.put("C", 50);
        hValues.put("D", 25);
        hValues.put("E", 25);
        hValues.put("F", 0);

        SearchAlgorithm<String> algorithm = graph.aStarAlgorithm((node, goal) -> hValues.getOrDefault(node, 0));

        List<String> path = algorithm.findShortestPath("A", "F");

        System.out.printf("Shortest path A->F is: %s\n", path);

        Assertions.assertEquals(Arrays.asList("A", "C", "E", "F"), path);
    }

    @Test
    public void testAstarAlgorithm2() {
        WeightedGraph<String> graph = weightedGraph();

        Map<String, Integer> hValues = new TreeMap<>();
        hValues.put("A", 100);
        hValues.put("B", 55);
        hValues.put("C", 50);
        hValues.put("D", 25);
        hValues.put("E", 25);
        hValues.put("F", 0);

        SearchAlgorithm<String> algorithm = graph.aStarAlgorithm((node, goal) -> hValues.getOrDefault(node, 0));

        graph.addUndirectedEdge("A", "B", 2d);
        graph.addUndirectedEdge("A", "C", 3d);

        List<String> path = algorithm.findShortestPath("A", "F");

        System.out.printf("Shortest path A->F is: %s\n", path);

        Assertions.assertEquals(Arrays.asList("A", "C", "E", "F"), path);
    }

    @Test
    public void testBellmanFordAlgorithm() {
        WeightedGraph<String> graph = weightedGraph();

        SearchAlgorithm<String> algorithm = graph.bellmanFordAlgorithm();

        List<String> path = algorithm.findShortestPath("A", "F");

        System.out.printf("Shortest path A->F is: %s\n", path);

        Assertions.assertEquals(Arrays.asList("A", "B", "D", "F"), path);
    }
}