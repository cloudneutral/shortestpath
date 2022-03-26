package io.cloudneutral.shortestpath.gui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class CircleMarker extends MarkerBase {
    private Color borderColor = Color.black;

    public CircleMarker setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    @Override
    protected void drawShape(Graphics2D surface) {
        Ellipse2D innerCircle = new Ellipse2D.Double(
                point.getX()+2,
                point.getY()+2,
                dimension.width-4,
                dimension.height-4
        );

        RoundRectangle2D box = new RoundRectangle2D.Double(
                point.getX(),
                point.getY(),
                dimension.width,
                dimension.height,
                5d,
                5d
        );

        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        surface.setStroke(stroke);
        surface.setPaint(borderColor);
        surface.draw(innerCircle);
        surface.setPaint(fillColor);
        surface.fill(innerCircle);
        surface.setColor(Color.black);
        surface.draw(box);
    }
}
