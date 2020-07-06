package dev.gcastro.humblerender;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

import dev.gcastro.humblerender.models.Matrix3;
import dev.gcastro.humblerender.shapes.Shape;
import dev.gcastro.humblerender.shapes.Tetrahedron;

public class DemoViewer {

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        final JSlider hRotationSlider = new JSlider(0, 360, 180);
        pane.add(hRotationSlider, BorderLayout.SOUTH);

        final JSlider vRotationSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(vRotationSlider, BorderLayout.EAST);

        final JPanel renderPanel = renderShape(new Tetrahedron(), hRotationSlider, vRotationSlider);
        hRotationSlider.addChangeListener(e -> renderPanel.repaint());
        vRotationSlider.addChangeListener(e -> renderPanel.repaint());

        pane.add(renderPanel, BorderLayout.CENTER);


        frame.setSize(400, 400);
        frame.setVisible(true);

    }


    public static JPanel renderShape(final Shape shape, final JSlider hRotationSlider, final JSlider vRotationSlider){
        return new JPanel() {
            public void paintComponent(final Graphics g) {
                
                final Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
               

                double hRotation = Math.toRadians(hRotationSlider.getValue());
                Matrix3 hRotationMatrix = new Matrix3(new double[] {
                Math.cos(hRotation), 0, -Math.sin(hRotation),
                0, 1, 0,
                Math.sin(hRotation), 0, Math.cos(hRotation)
                });

                double vRotation = Math.toRadians(vRotationSlider.getValue());
                Matrix3 vRotationMatrix = new Matrix3(new double[] {
                    1, 0, 0,
                    0, Math.cos(vRotation), Math.sin(vRotation),
                    0, -Math.sin(vRotation), Math.cos(vRotation)
                });

                Matrix3 rotationMatrix = hRotationMatrix.multiply(vRotationMatrix);


                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);
                for(Path2D path: shape.blueprint(rotationMatrix)){
                    g2.draw(path);
                }
            }
        };
    }
}