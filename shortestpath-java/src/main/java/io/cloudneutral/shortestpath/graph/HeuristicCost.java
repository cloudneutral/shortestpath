package io.cloudneutral.shortestpath.graph;

public interface HeuristicCost<N> {
    double estimateCost(N node, N goal);
}
