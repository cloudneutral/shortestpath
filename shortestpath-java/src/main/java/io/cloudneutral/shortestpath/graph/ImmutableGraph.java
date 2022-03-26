package io.cloudneutral.shortestpath.graph;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface ImmutableGraph<N, V> {
    boolean contains(N node);

    Set<N> nodes();

    N node(N node);

    Set<Edge<N, V>> edges();

    Optional<V> edgeValue(N from, N to);

    Set<N> adjacentNodes(N node);

    void adjacentNodes(N node, Consumer<N> consumer);
}
