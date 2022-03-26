package io.cloudneutral.shortestpath.graph;

import java.util.Objects;

/**
 * Node type used to keep state in shortest path traversal of weighted graphs.
 *
 * @param <T> the underlying node type
 */
public class Node<T> {
    // Unique node #
    private final T value;

    // Cost of travel from start node to this node
    private double g;

    // A heuristic estimates of the cost from this node to the goal
    private double h;

    // Parent from which this node was visited
    private Node<T> parent;

    public Node(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    public Node<T> setG(double g) {
        this.g = g;
        return this;
    }

    public Node<T> setH(double h) {
        this.h = h;
        return this;
    }

    public Node<T> setParent(Node<T> parent) {
        if (Objects.equals(this, parent)) {
            throw new IllegalArgumentException("Node cannot be parent of itself");
        }
        this.parent = parent;
        return this;
    }

    public T getValue() {
        return value;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return g + h;
    }


    public Node<T> getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node<?> node = (Node<?>) o;
        return value.equals(node.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + value +
                '}';
    }
}
