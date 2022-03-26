package io.cloudneutral.shortestpath.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import io.cloudneutral.shortestpath.graph.grid.GridSurface;

import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

public class ControlPanel extends JPanel implements ActionListener {
    private final Controller controller;

    private final JLabel statusLabel;

    private final CanvasPanel canvasPanel;

    private final JScrollPane canvasScrollPane;

    private JCheckBox showLabels;

    private JSlider nodeDiameter;

    private JSlider animationDelay;

    private JComboBox costEstimate;

    private JFrame frame;

    public ControlPanel(JFrame frame) {
        this.frame = frame;
        this.canvasPanel = new CanvasPanel();
        this.statusLabel = new JLabel("-");
        this.controller = new Controller(frame, canvasPanel, statusLabel);

        canvasScrollPane = new JScrollPane(canvasPanel);
        canvasScrollPane.setBorder((BorderFactory.createLoweredBevelBorder()));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(createButtonPanel(), BorderLayout.CENTER);
        south.add(statusPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(1024, 768));
        setLayout(new BorderLayout(20, 20));
        add(canvasScrollPane, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    private JComponent createButtonPanel() {
        final JPanel buttonsPane = new JPanel();

        this.showLabels = new JCheckBox("Show Labels");
        showLabels.setSelected(true);

        final JLabel nodeDiameterLabel = new JLabel("Node Diameter:");

        this.nodeDiameter = new JSlider(JSlider.HORIZONTAL, 10, 150, 50);
        nodeDiameter.setMajorTickSpacing(10);
        nodeDiameter.setMinorTickSpacing(5);
        nodeDiameter.setSnapToTicks(true);
        nodeDiameter.setPaintTicks(true);
        nodeDiameter.setPaintLabels(true);

        final JLabel animationDelayLabel = new JLabel("Animation Delay:");

        this.animationDelay = new JSlider(JSlider.HORIZONTAL, 0, 500, 50);
        animationDelay.setMajorTickSpacing(50);
        animationDelay.setMinorTickSpacing(10);
        animationDelay.setSnapToTicks(true);
        animationDelay.setPaintTicks(true);
        animationDelay.setPaintLabels(true);

        final JButton resetScreen = new JButton("Screen Size");
        resetScreen.setActionCommand("resetToScreen");
        resetScreen.addActionListener(this::actionPerformed);

        final JButton reset10x10 = new JButton("10x10");
        reset10x10.setActionCommand("reset10x10");
        reset10x10.addActionListener(this::actionPerformed);

        final JButton reset15x15 = new JButton("15x15");
        reset15x15.setActionCommand("reset15x15");
        reset15x15.addActionListener(this::actionPerformed);

        final JButton reset30x30 = new JButton("30x30");
        reset30x30.setActionCommand("reset30x30");
        reset30x30.addActionListener(this::actionPerformed);

        final JButton dijk = new JButton("Dijkstra's");
        dijk.setActionCommand("findpath_dijk");
        dijk.addActionListener(this::actionPerformed);

        final JButton aStar = new JButton("A*");
        aStar.setActionCommand("findpath_astar");
        aStar.addActionListener(this::actionPerformed);

        this.costEstimate = new JComboBox(
                Arrays.asList("Octile Distance", "Euclidean Distance", "Manhattan Distance").toArray());
        costEstimate.setSelectedIndex(1);
        costEstimate.addActionListener(this::actionPerformed);

        final JButton bellmanFord = new JButton("Bellman-Ford");
        bellmanFord.setActionCommand("findpath_bellman");
        bellmanFord.addActionListener(this::actionPerformed);

        final JButton obstacles = new JButton("Add Obstacles");
        obstacles.setActionCommand("obstacles");
        obstacles.setMnemonic('o');
        obstacles.addActionListener(this::actionPerformed);

        final JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.setMnemonic('c');
        cancel.addActionListener(this::actionPerformed);

        GroupLayout layout = new GroupLayout(buttonsPane);
        buttonsPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(nodeDiameterLabel)
                        .addComponent(nodeDiameter)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(animationDelayLabel)
                        .addComponent(animationDelay)
                )
                .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(showLabels)
                        .addComponent(resetScreen)
                        .addComponent(reset10x10)
                        .addComponent(reset15x15)
                        .addComponent(reset30x30)
                )
                .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dijk)
                        .addComponent(aStar)
                        .addComponent(costEstimate)
                        .addComponent(bellmanFord)
                )
                .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(obstacles)
                        .addComponent(cancel)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(nodeDiameterLabel)
                        .addComponent(nodeDiameter)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(animationDelayLabel)
                        .addComponent(animationDelay)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(showLabels)
                        .addComponent(resetScreen)
                        .addComponent(reset10x10)
                        .addComponent(reset15x15)
                        .addComponent(reset30x30)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(dijk)
                        .addComponent(aStar)
                        .addComponent(costEstimate)
                        .addComponent(bellmanFord)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(obstacles)
                        .addComponent(cancel)
                )
        );

        layout.linkSize(SwingConstants.HORIZONTAL, resetScreen, reset10x10, reset15x15, reset30x30, dijk, aStar,
                costEstimate, bellmanFord, cancel, obstacles);

        return buttonsPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("resetToScreen".equals(e.getActionCommand())) {
            Dimension size = canvasScrollPane.getSize();
            int cellSize = nodeDiameter.getValue();
            controller.updateGridSurface(GridSurface.builder()
                    .withCols(size.width / cellSize)
                    .withRows(size.height / cellSize)
                    .withCellSize(nodeDiameter.getValue())
                    .withDrawLabels(showLabels.isSelected())
                    .build());
        } else if ("reset10x10".equals(e.getActionCommand())) {
            controller.updateGridSurface(GridSurface.builder()
                    .withCols(10)
                    .withRows(10)
                    .withCellSize(nodeDiameter.getValue())
                    .withDrawLabels(showLabels.isSelected())
                    .build());
        } else if ("reset15x15".equals(e.getActionCommand())) {
            controller.updateGridSurface(GridSurface.builder()
                    .withCols(15)
                    .withRows(15)
                    .withCellSize(nodeDiameter.getValue())
                    .withDrawLabels(showLabels.isSelected())
                    .build());
        } else if ("reset30x30".equals(e.getActionCommand())) {
            controller.updateGridSurface(GridSurface.builder()
                    .withCols(30)
                    .withRows(30)
                    .withCellSize(nodeDiameter.getValue())
                    .withDrawLabels(showLabels.isSelected())
                    .build());
        } else if ("findpath_dijk".equals(e.getActionCommand())) {
            if (!controller.hasGridSurface()) {
                JOptionPane.showMessageDialog(
                        frame, "Generate a graph first and add some obstacles.",
                        "Missing Grid Surface", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.findShortestPathDijkstras(animationDelay.getValue());
        } else if ("findpath_astar".equals(e.getActionCommand())) {
            if (!controller.hasGridSurface()) {
                JOptionPane.showMessageDialog(
                        frame, "Generate a graph first and add some obstacles.",
                        "Missing Grid Surface", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.findShortestPathAStar(animationDelay.getValue(), costEstimate.getSelectedIndex());
        } else if ("findpath_bellman".equals(e.getActionCommand())) {
            if (!controller.hasGridSurface()) {
                JOptionPane.showMessageDialog(
                        frame, "Generate a graph first and add some obstacles.",
                        "Missing Grid Surface", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.findShortestPathBellmanFord(animationDelay.getValue());
        } else if ("obstacles".equals(e.getActionCommand())) {
            if (!controller.hasGridSurface()) {
                JOptionPane.showMessageDialog(
                        frame, "Generate a graph first.",
                        "Missing Grid Surface", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            controller.addObstacles();
        } else if ("cancel".equals(e.getActionCommand())) {
            controller.cancelSearch();
        }

        invalidate();
        repaint();
    }
}
