package io.cloudneutral.shortestpath.graph.grid;

import java.util.Arrays;
import java.util.Objects;

import io.cloudneutral.shortestpath.graph.HeuristicCost;

public class Cell {
    // Diagonal octile distance (8x)
    // https://en.wikipedia.org/wiki/Chebyshev_distance
    public static final HeuristicCost<Cell> octileDistance = (node, goal) -> {
        final double D = 1;
        final double D2 = Math.sqrt(2);
        double dx = Math.abs(node.x - goal.x);
        double dy = Math.abs(node.y - goal.y);
        return (D * (dx + dy) + (D2 - 2 * D)) * Math.min(dx, dy);
    };

    public static final HeuristicCost<Cell> euclideanDistance = (node, goal) -> {
        final double D = 1;
        double dx = Math.abs(node.x - goal.x);
        double dy = Math.abs(node.y - goal.y);
        return D * Math.sqrt(dx * dx + dy * dy);
    };

    public static final HeuristicCost<Cell> manhattanDistance = (node, goal) -> Math.abs(node.x - goal.x) + Math.abs(
            node.y - goal.y);

    public static Cell of(int x, int y) {
        return new Cell(x, y);
    }

    private final int x;

    private final int y;

    private Status status;

    private double gCost;

    private double hCost;

    protected Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.status = Status.empty;
    }

    public void setCosts(double gCost, double hCost) {
        this.gCost = gCost;
        this.hCost = hCost;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getGcost() {
        return gCost;
    }

    public double getHcost() {
        return hCost;
    }

    public double getFcost() {
        return gCost + hCost;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public boolean anyStatus(Status... statuses) {
        return Arrays.stream(statuses).anyMatch(s -> s.equals(status));
    }
    
    public boolean allStatus(Status... statuses) {
        return Arrays.stream(statuses).allMatch(s -> s.equals(status));
    }

    public boolean noneStatus(Status... statuses) {
        return Arrays.stream(statuses).noneMatch(s -> s.equals(status));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", state=" + status +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                '}';
    }

    public String toShortString() {
        return "x:" + x + ",y:" + y + ",state:" + status + ",g:" + gCost + ",h:" + hCost;
    }
}
