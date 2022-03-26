package io.cloudneutral.shortestpath.graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class TraversableGraph<N> implements ImmutableGraph<Node<N>, Double>, Iterable<Node<N>> {
    private final Graph<Node<N>, Double> graph = new Graph<>();

    // For index lookups using unwrapped node values as keys
    private final Map<N, Node<N>> index = new HashMap<>();

    protected TraversableGraph(Graph<N, Double> source) {
        for (N node : source.nodes()) {
            Node<N> n = new Node<>(node);
            graph.addNode(n);
            index.put(node, n);
        }

        for (Edge<N, Double> edge : source.edges()) {
            graph.addEdge(
                    index.get(edge.getStart()),
                    index.get(edge.getEnd()),
                    edge.getValue());
        }
    }

    @Override
    public Node<N> node(Node<N> node) {
        return index.getOrDefault(node.getValue(), node);
    }

    public Node<N> wrap(N node) {
        if (!index.containsKey(node)) {
            throw new IllegalStateException();
        }
        return index.get(node);
    }

    @Override
    public boolean contains(Node<N> node) {
        return graph.contains(node);
    }

    @Override
    public Iterator<Node<N>> iterator() {
        return graph.iterator();
    }

    @Override
    public Set<Node<N>> nodes() {
        return graph.nodes();
    }

    @Override
    public Set<Edge<Node<N>, Double>> edges() {
        return graph.edges();
    }

    @Override
    public Optional<Double> edgeValue(Node<N> from, Node<N> to) {
        return graph.edgeValue(from, to);
    }

    @Override
    public Set<Node<N>> adjacentNodes(Node<N> node) {
        return graph.adjacentNodes(node);
    }

    @Override
    public void adjacentNodes(Node<N> node, Consumer<Node<N>> consumer) {
        graph.adjacentNodes(node, consumer);
    }
}
