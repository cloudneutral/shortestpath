package io.cloudneutral.shortestpath.graph.algorithm;

import io.cloudneutral.shortestpath.graph.HeuristicCost;
import io.cloudneutral.shortestpath.graph.Node;
import io.cloudneutral.shortestpath.graph.TraversableGraph;

public class AStarAlgorithm<N> extends DijkstrasAlgorithm<N> {
    private final HeuristicCost<N> heuristicCost;

    public AStarAlgorithm(TraversableGraph<N> graph, HeuristicCost<N> heuristicCost) {
        super(graph);
        this.heuristicCost = heuristicCost;
    }

    @Override
    protected double heuristicCostEstimate(Node<N> from, Node<N> goal) {
        return heuristicCost.estimateCost(from.getValue(), goal.getValue());
    }
}
