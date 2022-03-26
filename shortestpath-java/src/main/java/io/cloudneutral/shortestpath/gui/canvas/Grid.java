package io.cloudneutral.shortestpath.gui.canvas;

import java.awt.Graphics2D;

import io.cloudneutral.shortestpath.graph.grid.GridSurface;

public class Grid implements Drawable {
    private final GridSurface gs;

    public Grid(GridSurface gs) {
        this.gs = gs;
    }

    @Override
    public void render(Graphics2D surface) {
        new Mesh(gs).render(surface);
    }
}
