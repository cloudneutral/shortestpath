package io.cloudneutral.shortestpath.graph.algorithm;

import java.util.List;

public interface SearchAlgorithm<N> {
    List<N> findShortestPath(N from, N to);

    void addFringeListener(FringeListener<N> listener);
}
