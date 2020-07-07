package dev.gcastro.humblerender.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Triangle {

    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertex3;
    private Color color;

    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3, Color color) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.color = color;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public Vertex getVertex3() {
        return vertex3;
    }

    public Color getColor() {
        return color;
    }

    // barycentric coordinates, simple but inefficient
    public List<RasterizedPoint> coordinates(int viewportWidth, int viewportHeight, int objWidth, int objHeight,
            Matrix3 rotation) {

        Vertex v1 = rotation.transform(vertex1);
        v1.translate(viewportWidth, viewportHeight);

        Vertex v2 = rotation.transform(vertex2);
        v2.translate(viewportWidth, viewportHeight);

        Vertex v3 = rotation.transform(vertex3);
        v3.translate(viewportWidth, viewportHeight);

        Vertex ab = new Vertex(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ());
        Vertex ac = new Vertex(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ());
        Vertex norm = new Vertex(ab.getY() * ac.getZ() - ab.getZ() * ac.getY(),
                ab.getZ() * ac.getX() - ab.getX() * ac.getZ(), ab.getX() * ac.getY() - ab.getY() * ac.getX());
        double normalLength = Math
                .sqrt(norm.getX() * norm.getX() + norm.getY() * norm.getY() + norm.getZ() * norm.getZ());
        norm.normalize(normalLength);

        double angleCos = Math.abs(norm.getZ());

        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()))));
        int maxX = (int) Math.min(objWidth - 1, Math.floor(Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()))));

        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()))));
        int maxY = (int) Math.min(objHeight - 1, Math.floor(Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()))));

        double triangleArea = (v1.getY() - v3.getY()) * (v2.getX() - v3.getX())
                + (v2.getY() - v3.getY()) * (v3.getX() - v1.getX());

        List<RasterizedPoint> points = new ArrayList<>(minX + minY);
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double b1 = ((y - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - x))
                        / triangleArea;
                double b2 = ((y - v1.getY()) * (v3.getX() - v1.getX()) + (v3.getY() - v1.getY()) * (v1.getX() - x))
                        / triangleArea;
                double b3 = ((y - v2.getY()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v2.getX() - x))
                        / triangleArea;

                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    double depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                    int zIndex = y * objWidth + x;
                    points.add(new RasterizedPoint(x, y, depth, zIndex, getShade(getColor(), angleCos)));
                }
            }
        }

        return points;
    }

    public Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }


}