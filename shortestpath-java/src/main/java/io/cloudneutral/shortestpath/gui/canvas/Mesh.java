package io.cloudneutral.shortestpath.gui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Locale;

import io.cloudneutral.shortestpath.graph.grid.Cell;
import io.cloudneutral.shortestpath.graph.grid.GridSurface;
import io.cloudneutral.shortestpath.graph.grid.Coordinate;

public class Mesh implements Drawable {
    private final GridSurface gs;

    public Mesh(GridSurface gs) {
        this.gs = gs;
    }

    @Override
    public void render(Graphics2D surface) {
        for (Cell cell : gs.getGraph().nodes()) {
            Coordinate pos = gs.toGridPosition(cell);

            MarkerBase cellShape = new CircleMarker()
                    .setPoint(pos)
                    .setDimension(new Dimension(gs.getCellSize(), gs.getCellSize()))
                    .setLabel(gs.isDrawLabels() ? String.format(Locale.US, "f%.1f", cell.getFcost()) : null)
                    .setLabelColor(Color.black)
                    .setFillColor(cell.getStatus().color());

            cellShape.render(surface);

            if (gs.isDrawLabels()) {
                Label gCost = new Label()
                        .setPoint(Coordinate
                                .from(pos.getX(), pos.getY() - gs.getCellSize() / 3))
                        .setDimension(new Dimension(gs.getCellSize(), gs.getCellSize()))
                        .setColor(Color.red)
                        .setLabel(String.format(Locale.US, "g%.1f", cell.getGcost()));

                gCost.render(surface);

                Label hCost = new Label()
                        .setPoint(Coordinate
                                .from(pos.getX(), pos.getY() + gs.getCellSize() / 3))
                        .setDimension(new Dimension(gs.getCellSize(), gs.getCellSize()))
                        .setColor(Color.BLUE)
                        .setLabel(String.format(Locale.US, "h%.1f", cell.getHcost()));

                hCost.render(surface);
            }
        }
    }
}
