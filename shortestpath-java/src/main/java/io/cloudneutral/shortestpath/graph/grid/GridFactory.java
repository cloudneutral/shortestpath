package io.cloudneutral.shortestpath.graph.grid;

import java.util.Set;
import java.util.stream.Collectors;

import io.cloudneutral.shortestpath.graph.WeightedGraph;

public abstract class GridFactory {
    protected GridFactory() {
    }

    public static WeightedGraph<Cell> generateGrid(final int w, final int h) {
        WeightedGraph<Cell> graph = new WeightedGraph<>();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Cell cell = new Cell(x, y);
                graph.addNode(cell);

                if (x == 0 && y == 0) {
                    cell.setStatus(Status.start);
                } else if (x == w - 1 && y == h - 1) {
                    cell.setStatus(Status.goal);
                } else {
                    cell.setStatus(Status.empty);
                }
            }
        }

        for (Cell cell : graph) {
            for (Orientation dir : Orientation.values()) {
                Cell neighbor = new Cell(cell.getX() + dir.deltaX, cell.getY() + dir.deltaY);
                if (graph.contains(neighbor)) {
                    neighbor = graph.node(neighbor);
                    graph.addEdge(cell, neighbor, Math.sqrt(Math.abs(dir.deltaX) + Math.abs(dir.deltaY)));
                }
            }
        }

        return graph;
    }

    public static WeightedGraph<Cell> resetGrid(WeightedGraph<Cell> template) {
        final Set<Cell> obstructed = template
                .nodes()
                .stream()
                .filter(node -> node.getStatus() == Status.obstacle)
                .collect(Collectors.toSet());

        WeightedGraph<Cell> graph = new WeightedGraph<>();

        template.nodes().forEach(tCell -> {
            Cell cell = Cell.of(tCell.getX(), tCell.getY());
            if (obstructed.contains(cell)) {
                cell.setStatus(Status.obstacle);
            } else {
                if (tCell.getStatus().isTerminal()) {
                    cell.setStatus(tCell.getStatus());
                }
            }
            graph.addNode(cell);
        });

        graph.nodes().forEach(cell -> {
            for (Orientation dir : Orientation.values()) {
                Cell neighbor = Cell.of(cell.getX() + dir.deltaX, cell.getY() + dir.deltaY);
                if (graph.contains(neighbor) && !obstructed.contains(neighbor)) {
                    neighbor = graph.node(neighbor);
                    graph.addEdge(cell, neighbor, Math.sqrt(Math.abs(dir.deltaX) + Math.abs(dir.deltaY)));
                }
            }
        });

        return graph;
    }

}
