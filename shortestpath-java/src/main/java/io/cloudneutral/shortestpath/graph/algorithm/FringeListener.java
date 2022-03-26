package io.cloudneutral.shortestpath.graph.algorithm;

import io.cloudneutral.shortestpath.graph.Node;

public interface FringeListener<N> {
    long simulationDelay();

    void nodeVisited(Node<N> node);

    void nodeClosed(Node<N> node);
}
