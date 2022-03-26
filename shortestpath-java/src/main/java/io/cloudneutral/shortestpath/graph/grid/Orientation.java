package io.cloudneutral.shortestpath.graph.grid;

public enum Orientation {
    north(0, -1),
    north_east(1, 1),
    east(1, 0),
    south_east(1, -1),
    south(0, 1),
    south_west(-1, 1),
    west(-1, 0),
    north_west(-1, -1);

    public final int deltaX;

    public final int deltaY;

    Orientation(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
}
