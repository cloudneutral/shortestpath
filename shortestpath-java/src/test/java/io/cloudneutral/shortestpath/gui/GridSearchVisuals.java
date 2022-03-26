package io.cloudneutral.shortestpath.gui;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.cloudneutral.shortestpath.graph.WeightedGraph;
import io.cloudneutral.shortestpath.graph.grid.Cell;
import io.cloudneutral.shortestpath.graph.grid.Status;
import io.cloudneutral.shortestpath.graph.grid.Orientation;

public class GridSearchVisuals {
    private static WeightedGraph<Cell> createGrid(final int w, final int h, Set<Cell> obstructed) {
        WeightedGraph<Cell> graph = new WeightedGraph<>();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Cell cell = Cell.of(x, y);
                graph.addNode(cell);
            }
        }

        for (Cell cell : graph) {
            if (obstructed.contains(cell)) {
                cell.setStatus(Status.obstacle);
            }
            for (Orientation dir : Orientation.values()) {
                Cell neighbor = Cell.of(cell.getX() + dir.deltaX, cell.getY() + dir.deltaY);
                if (graph.contains(neighbor) && !obstructed.contains(neighbor)) {
                    graph.addEdge(cell, neighbor, Math.sqrt(Math.abs(dir.deltaX) + Math.abs(dir.deltaY)));
                }
            }
        }

        return graph;
    }

    private static void printGrid(int w, int h, Set<Cell> obstructed, List<Cell> path) {
        System.out.print("  ");
        for (int x = 0; x < w; x++) {
            System.out.printf("%3d", x);
        }
        System.out.println();
        System.out.print("    ");
        for (int x = 0; x < w * 3 - 2; x++) {
            System.out.print("_");
        }
        System.out.println();

        for (int y = 0; y < h; y++) {
            System.out.printf("%3d|", y);

            for (int x = 0; x < w; x++) {
                Cell c = Cell.of(x, y);
                if (obstructed.contains(c)) {
                    System.out.print("#  ");
                } else {
                    if (path.contains(c)) {
                        System.out.print(".  ");
                    } else {
                        System.out.print("0  ");
                    }
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private static final Set<Cell> obstructedCells = new HashSet<>();

    @BeforeAll
    public static void setupGrid() {
        obstructedCells.add(Cell.of(1, 1));
        obstructedCells.add(Cell.of(2, 1));
        obstructedCells.add(Cell.of(3, 1));
        obstructedCells.add(Cell.of(4, 1));
        obstructedCells.add(Cell.of(5, 1));
        obstructedCells.add(Cell.of(6, 1));
        obstructedCells.add(Cell.of(7, 1));
        obstructedCells.add(Cell.of(8, 1));
        obstructedCells.add(Cell.of(9, 1));
        obstructedCells.add(Cell.of(1, 2));
        obstructedCells.add(Cell.of(1, 3));
        obstructedCells.add(Cell.of(1, 4));
        obstructedCells.add(Cell.of(1, 5));
        obstructedCells.add(Cell.of(2, 5));
        obstructedCells.add(Cell.of(3, 5));
        obstructedCells.add(Cell.of(4, 5));
        obstructedCells.add(Cell.of(5, 5));
        obstructedCells.add(Cell.of(6, 5));
        obstructedCells.add(Cell.of(7, 5));
        obstructedCells.add(Cell.of(8, 5));
        obstructedCells.add(Cell.of(9, 5));
        obstructedCells.add(Cell.of(10, 5));
        obstructedCells.add(Cell.of(11, 5));
        obstructedCells.add(Cell.of(12, 5));
        obstructedCells.add(Cell.of(13, 5));
        obstructedCells.add(Cell.of(14, 5));
        obstructedCells.add(Cell.of(15, 5));
        obstructedCells.add(Cell.of(16, 5));
//        obstructed.add(Cell.of(17, 5));
    }

    @Test
    public void testDijakstraSearch() {
        final int w = 30;
        final int h = 20;

        WeightedGraph<Cell> graph = createGrid(w, h, obstructedCells);

        final Cell fromStart = Cell.of(0, 0);
        final Cell toGoal = Cell.of(w / 2, h / 2);

        List<Cell> dijkstraPath = graph.dijkstrasAlgorithm().findShortestPath(fromStart, toGoal);
        System.out.printf("Shortest path using Dikstras from %s -> %s\n", fromStart, toGoal);
        printGrid(w, h, obstructedCells, dijkstraPath);
    }

    @Test
    public void testAstarSearch() {
        final int w = 30;
        final int h = 20;

        WeightedGraph<Cell> graph = createGrid(w, h, obstructedCells);

        final Cell fromStart = Cell.of(0, 0);
        final Cell toGoal = Cell.of(w / 2, h / 2);

        List<Cell> aStarPath = graph.aStarAlgorithm(Cell.octileDistance).findShortestPath(fromStart, toGoal);

        System.out.printf("Shortest path using A* from %s -> %s\n", fromStart, toGoal);
        printGrid(w, h, obstructedCells, aStarPath);
    }

    @Test
    public void testAstarSearch2() {
        final int w = 30;
        final int h = 20;

        WeightedGraph<Cell> graph = createGrid(w, h, obstructedCells);

        final Cell fromStart = Cell.of(0, 0);
        final Cell toGoal = Cell.of(w / 2, h / 2);

        List<Cell> aStarPath = graph.aStarAlgorithm(Cell.euclideanDistance).findShortestPath(fromStart, toGoal);

        System.out.printf("Shortest path using A* from %s -> %s\n", fromStart, toGoal);
        printGrid(w, h, obstructedCells, aStarPath);
    }
}
