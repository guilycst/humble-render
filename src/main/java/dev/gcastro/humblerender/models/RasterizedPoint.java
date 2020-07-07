package dev.gcastro.humblerender.models;

import java.awt.*;

public class RasterizedPoint {
    private int x;
    private int y;
    private double depth;
    private int zIndex;
    private Color color;

    public RasterizedPoint(int x, int y, double depth, int zIndex, Color color) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.zIndex = zIndex;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDepth() {
        return depth;
    }

    public int getzIndex() {
        return zIndex;
    }

    public Color getColor() {
        return color;
    }

}