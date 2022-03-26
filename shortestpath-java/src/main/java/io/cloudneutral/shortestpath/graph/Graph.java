package io.cloudneutral.shortestpath.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class Graph<N, V> implements Iterable<N>, ImmutableGraph<N, V> {
    private final Map<N, N> nodes = new HashMap<>();

    private final Set<Edge<N, V>> edges = new HashSet<>();

    public Graph<N, V> addNode(N node) {
        this.nodes.put(node, node);
        return this;
    }

    @Override
    public boolean contains(N node) {
        return nodes.containsKey(node);
    }

    public Graph<N, V> addUndirectedEdge(N start, N end, V value) {
        addEdge(start, end, value);
        addEdge(end, start, value);
        return this;
    }

    public Graph<N, V> addEdge(N start, N end, V value) {
        if (!nodes.containsKey(start)) {
            throw new IllegalStateException("No such node: " + start);
        }
        if (!nodes.containsKey(end)) {
            throw new IllegalStateException("No such node: " + end);
        }
        Edge<N, V> edge = new Edge<>(start, end, value);
        this.edges.remove(edge);
        this.edges.add(edge);
        return this;
    }

    @Override
    public Optional<V> edgeValue(N from, N to) {
        return Optional.ofNullable(findEdge(from, to)).map(Edge::getValue);
    }

    public void putEdgeValue(N start, N target, V value) {
        Edge<N, V> edge = findEdge(start, target);
        if (edge == null) {
            throw new IllegalArgumentException("No such edge: " + start + "->" + target);
        }
        edge.setValue(value);
    }

    private Edge<N, V> findEdge(N start, N target) {
        for (Edge<N, V> edge : edges) {
            if (edge.getStart().equals(start) && edge.getEnd().equals(target)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public Set<N> adjacentNodes(N node) {
        Set<N> adjacent = new HashSet<>();
        for (Edge<N, V> edge : edges) {
            if (edge.getStart().equals(node)) {
                adjacent.add(edge.getEnd());
            }
        }
        return adjacent;
    }

    @Override
    public void adjacentNodes(N node, Consumer<N> consumer) {
        for (Edge<N, V> edge : edges) {
            if (edge.getStart().equals(node)) {
                consumer.accept(edge.getEnd());
            }
        }
    }

    @Override
    public Set<N> nodes() {
        return nodes.keySet();
    }

    @Override
    public N node(N node) {
        return nodes.getOrDefault(node, node);
    }

    @Override
    public Set<Edge<N, V>> edges() {
        return edges;
    }

    @Override
    public Iterator<N> iterator() {
        return nodes().iterator();
    }
}