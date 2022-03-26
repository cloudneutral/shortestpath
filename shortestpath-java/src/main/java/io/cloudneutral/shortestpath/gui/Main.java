package io.cloudneutral.shortestpath.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public abstract class Main {

    private Main() {
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                JFrame frame = new JFrame();
                frame.setTitle("Shortest Path Algorithms");
                frame.add(new ControlPanel(frame));
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                    e.printStackTrace(System.err);
                    JOptionPane.showMessageDialog(frame, e.getMessage(), "Oh Snap!",
                            JOptionPane.WARNING_MESSAGE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
