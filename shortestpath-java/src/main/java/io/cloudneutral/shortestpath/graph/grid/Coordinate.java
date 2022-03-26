package io.cloudneutral.shortestpath.graph.grid;

import java.awt.geom.Point2D;

/**
 * Value object representing a cartesian coordinate, which is an x and y coordinate on a 2D plane.
 * Supports conversion to and from polar coordinates (radius and angle in degrees).
 * <p/>
 * See: http://en.wikipedia.org/wiki/Cartesian_coordinate_system.
 */
public class Coordinate {
    public static Coordinate from(int x, int y) {
        return new Coordinate(x, y);
    }

    private final int x;

    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point2D toPoint2D() {
        return new Point2D.Double(x, y);
    }
}
