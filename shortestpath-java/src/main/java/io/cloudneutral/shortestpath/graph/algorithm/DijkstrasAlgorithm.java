package io.cloudneutral.shortestpath.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import io.cloudneutral.shortestpath.graph.Node;
import io.cloudneutral.shortestpath.graph.TraversableGraph;

public class DijkstrasAlgorithm<N> implements SearchAlgorithm<N> {
    private FringeListener<N> fringeListener = new EmptyFringeListener();

    private final TraversableGraph<N> graph;

    public DijkstrasAlgorithm(TraversableGraph<N> graph) {
        this.graph = graph;
    }

    public void addFringeListener(FringeListener<N> fringeListener) {
        this.fringeListener = fringeListener;
    }

    @Override
    public List<N> findShortestPath(N start, N goal) {
        // Set initial cost to infinity and clear path
        for (Node<N> node : graph.nodes()) {
            node.setG(Double.MAX_VALUE);
            node.setH(Double.MAX_VALUE);
            node.setParent(null);
        }

        final Node<N> goalNode = graph.wrap(goal);

        // Distance from source to source is set to 0
        final Node<N> startNode = graph.wrap(start);
        startNode.setG(0);
        startNode.setH(heuristicCostEstimate(startNode, goalNode));

        // Add source vertex to unvisited set, aka fringe or frontier
        Queue<Node<N>> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        openSet.add(startNode);

        fringeListener.nodeVisited(startNode);

        // Holds all visited/settled vertexes
        Set<Node<N>> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            // Find unvisited vertex with smallest known distance from the source vertex.
            // This is initially the source vertex with distance 0.
            Node<N> currentNode = openSet.poll();

            // Early exit
            if (currentNode.equals(goalNode)) {
                break;
            }

            // Add vertex to visited set
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            fringeListener.nodeClosed(currentNode);

            // For the current vertex, calculate the total weight/cost of each neighbor from the source vertex
            for (Node<N> neighborNode : unvisitedNeighbors(currentNode, closedSet)) {
                // Grab edge cost and add it to the movement or G-cost of current node
                double edgeCost = graph.edgeValue(currentNode, neighborNode).get();

                double tentativeCost = currentNode.getG() + edgeCost;

                if (tentativeCost < neighborNode.getG()) {
                    neighborNode.setParent(currentNode);
                    neighborNode.setG(tentativeCost);
                    neighborNode.setH(heuristicCostEstimate(neighborNode, goalNode));

                    openSet.add(neighborNode);

                    fringeListener.nodeVisited(neighborNode);
                }
            }

            if (fringeListener.simulationDelay() > 0) {
                try {
                    Thread.sleep(fringeListener.simulationDelay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Backtrack and we have the path
        List<N> trail = new ArrayList<>();
        trail.add(goalNode.getValue());

        Node<N> parent = goalNode.getParent();
        while (parent != null) {
            trail.add(parent.getValue());
            parent = parent.getParent();
        }

        Collections.reverse(trail);

        return trail;
    }

    protected double heuristicCostEstimate(Node<N> from, Node<N> goal) {
        // Always zero in Dijkstra's, its only thing what separates it from A*
        return 0;
    }

    private Set<Node<N>> unvisitedNeighbors(Node<N> node, Set<Node<N>> visited) {
        Set<Node<N>> neighbours = new HashSet<>();

        graph.adjacentNodes(node, end -> {
            if (!visited.contains(end)) {
                neighbours.add(end);
            }
        });

        return neighbours;
    }
}
