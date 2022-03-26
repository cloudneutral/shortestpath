package io.cloudneutral.shortestpath.gui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import io.cloudneutral.shortestpath.graph.grid.Coordinate;

public abstract class MarkerBase implements Drawable {
    protected Dimension dimension = new Dimension(20,20);

    protected String label;

    protected Color fillColor = Color.lightGray;

    protected Color labelColor = Color.white;

    protected Coordinate point;

    public MarkerBase setLabel(String label) {
        this.label = label;
        return this;
    }

    public MarkerBase setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    public MarkerBase setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public MarkerBase setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
        return this;
    }

    public MarkerBase setPoint(Coordinate point) {
        this.point = point;
        return this;
    }

    @Override
    public final void render(Graphics2D surface) {
        drawShape(surface);
        if (label != null) {
            new Label()
                    .setLabel(label)
                    .setColor(labelColor)
                    .setPoint(point)
                    .setDimension(dimension)
                    .render(surface);
        }
    }

    protected abstract void drawShape(Graphics2D surface);
}
