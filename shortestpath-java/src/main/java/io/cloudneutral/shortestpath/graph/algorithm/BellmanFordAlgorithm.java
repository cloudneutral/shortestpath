package io.cloudneutral.shortestpath.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.cloudneutral.shortestpath.graph.Edge;
import io.cloudneutral.shortestpath.graph.Node;
import io.cloudneutral.shortestpath.graph.TraversableGraph;

public class BellmanFordAlgorithm<N> implements SearchAlgorithm<N> {
    private FringeListener<N> fringeListener = new EmptyFringeListener<>();

    private final TraversableGraph<N> graph;

    public BellmanFordAlgorithm(TraversableGraph<N> graph) {
        this.graph = graph;
    }

    @Override
    public void addFringeListener(FringeListener<N> fringeListener) {
        this.fringeListener = fringeListener;
    }

    @Override
    public List<N> findShortestPath(N from, N to) {
        // Step 1: initialize graph by setting all vertices to INFINITE
        for (Node<N> node : graph) {
            node.setG(Integer.MAX_VALUE);
            node.setParent(null);
        }

        graph.wrap(from).setG(0);

        fringeListener.nodeVisited(graph.wrap(from));

        // Step 2: for each vertex, apply relaxation for all the edges
        for (Node<N> n : graph) {
            for (Edge<Node<N>, Double> edge : graph.edges()) {
                double cost = edge.getStart().getG() + edge.getValue();
                if (edge.getStart().getG() != Double.MAX_VALUE
                        && cost < edge.getEnd().getG()) {
                    edge.getEnd().setG(cost);
                    edge.getEnd().setParent(edge.getStart());
                    fringeListener.nodeVisited(edge.getEnd());
                }
            }
        }

        // Step 3: check for negative-weight cycles
        for (Edge<Node<N>, Double> edge : graph.edges()) {
            double w = edge.getStart().getG() + edge.getValue();
            if (w < edge.getEnd().getG()) {
                throw new IllegalStateException("Graph contains negative-weight cycles");
            }
        }

        List<N> trail = new ArrayList<>();
        trail.add(to);

        Node<N> parent = graph.wrap(to).getParent();
        while (parent != null) {
            trail.add(parent.getValue());
            parent = parent.getParent();
        }

        Collections.reverse(trail);

        return trail;
    }
}
