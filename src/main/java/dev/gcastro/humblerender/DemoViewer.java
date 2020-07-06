package dev.gcastro.humblerender;

import java.awt.*;
import javax.swing.*;

public class DemoViewer {

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        final JSlider hRotationSlider = new JSlider(0, 360, 180);
        pane.add(hRotationSlider, BorderLayout.SOUTH);

        final JSlider vRotationSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(vRotationSlider, BorderLayout.EAST);

        final JPanel renderPanel = new JPanel() {
            public void paintComponent(final Graphics g) {
                final Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(400, 400);
        frame.setVisible(true);

    }
}