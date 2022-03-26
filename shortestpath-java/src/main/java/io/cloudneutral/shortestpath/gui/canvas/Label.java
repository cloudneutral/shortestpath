package io.cloudneutral.shortestpath.gui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import io.cloudneutral.shortestpath.graph.grid.Coordinate;

public class Label implements Drawable {
    private Dimension dimension = new Dimension(20, 20);

    private Coordinate point;

    private Color color;

    private String label;

    public Label setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    public Label setPoint(Coordinate point) {
        this.point = point;
        return this;
    }

    public Label setColor(Color color) {
        this.color = color;
        return this;
    }

    public Label setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public void render(Graphics2D surface) {
        AttributedString as = new AttributedString(label);
        Font font = new Font("Consolas", Font.PLAIN, (int)(dimension.width * .2));
        as.addAttribute(TextAttribute.FONT, font);
        as.addAttribute(TextAttribute.FOREGROUND, color, 0, label.length());

        FontMetrics fm = surface.getFontMetrics(font);

        int dx = fm.stringWidth(label) / 2;
        int dy = (fm.getAscent() + fm.getDescent())  / 2;

        surface.drawString(as.getIterator(),
                (float) point.getX() + dimension.width / 2f - dx,
                (float) point.getY() + dimension.height / 2f + dy);
    }
}
