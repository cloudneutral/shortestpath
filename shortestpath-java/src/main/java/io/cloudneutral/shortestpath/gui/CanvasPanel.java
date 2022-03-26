package io.cloudneutral.shortestpath.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import io.cloudneutral.shortestpath.graph.grid.GridSurface;
import io.cloudneutral.shortestpath.gui.canvas.Grid;

public class CanvasPanel extends JPanel {
    private GridSurface gridSettings;

    private BufferedImage bufferedImage;

    private Graphics2D surface;

    public void updateCanvas(GridSurface gridSettings) {
        this.gridSettings = gridSettings;
        this.bufferedImage = new BufferedImage(gridSettings.getWidth(), gridSettings.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        this.surface = bufferedImage.createGraphics();
        this.surface.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
        this.surface.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.surface.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        this.surface.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.surface.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.surface.setBackground(Color.white);
        this.surface.setColor(Color.black);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return bufferedImage == null ?
                new Dimension(300,300) :
                new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (surface != null) {
            surface.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            new Grid(gridSettings).render(surface);
            g.drawImage(bufferedImage, 0, 0, null);
            g.dispose();
        }
    }
}