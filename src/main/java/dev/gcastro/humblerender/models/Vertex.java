package dev.gcastro.humblerender.models;

public class Vertex {

    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void translate(int containerWidth, int containerHeight){
        this.x += containerWidth / 2;
        this.y += containerHeight / 2;
    }

	public void normalize(double normalLength) {
        this.x /= normalLength;
        this.y /= normalLength;
        this.z /= normalLength;
	}

}