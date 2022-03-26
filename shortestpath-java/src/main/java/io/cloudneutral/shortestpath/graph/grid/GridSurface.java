package io.cloudneutral.shortestpath.graph.grid;

import io.cloudneutral.shortestpath.graph.WeightedGraph;

public class GridSurface {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final GridSurface instance = new GridSurface();

        private Builder() {
        }

        public Builder withRows(int rows) {
            instance.rows = rows;
            return this;
        }

        public Builder withCols(int cols) {
            instance.cols = cols;
            return this;
        }

        public Builder withCellSize(int cellSize) {
            instance.cellSize = cellSize;
            return this;
        }

        public Builder withDrawLabels(boolean drawLabels) {
            instance.drawLabels = drawLabels;
            return this;
        }

        public GridSurface build() {
            return instance;
        }
    }

    private int rows;

    private int cols;

    private int cellSize;

    private boolean drawLabels;

    private WeightedGraph<Cell> graph;

    private GridSurface() {
    }

    public boolean isDrawLabels() {
        return drawLabels;
    }

    public int getWidth() {
        return cols * cellSize;
    }

    public int getHeight() {
        return rows * cellSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public WeightedGraph<Cell> getGraph() {
        if (this.graph == null) {
            this.graph = GridFactory.generateGrid(cols, rows);
        }
        return this.graph;
    }

    public WeightedGraph<Cell> cloneGraph() {
        this.graph = GridFactory.resetGrid(getGraph());
        return graph;
    }

    public Coordinate toCellPosition(int x, int y) {
        int cx = x / cellSize;
        int cy = (getHeight() - y) / cellSize;
        return Coordinate.from(cx, cy);
    }

    public Coordinate toGridPosition(Cell cell) {
        int dx = cellSize * cell.getX();
        int dy = (getHeight() - cellSize) - cellSize * cell.getY();
        return Coordinate.from(dx, dy);
    }
}
