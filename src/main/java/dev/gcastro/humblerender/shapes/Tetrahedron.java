package dev.gcastro.humblerender.shapes;

import java.util.*;
import java.awt.Color;
import java.awt.geom.*;

import dev.gcastro.humblerender.models.*;

public class Tetrahedron implements Shape{

    private final List<Triangle> parts;

    public Tetrahedron() {
        this.parts = new ArrayList<Triangle>();
        parts.add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(-100, 100, -100),
                Color.WHITE));
        parts.add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(100, -100, -100),
                Color.RED));
        parts.add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(100, 100, 100),
                Color.GREEN));
        parts.add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(-100, -100, 100),
                Color.BLUE));
    }


    @Override
    public List<Path2D> blueprint(Matrix3 rotationMatrix) {
        List<Path2D> paths = new ArrayList<>();
        for (final Triangle part : parts) {

            Vertex v1 = rotationMatrix.transform(part.getVertex1());
            Vertex v2 = rotationMatrix.transform(part.getVertex2());
            Vertex v3 = rotationMatrix.transform(part.getVertex3());

            Path2D path = new Path2D.Double();
            path.moveTo(v1.getX(), v1.getY());
            path.lineTo(v2.getX(), v2.getY());
            path.lineTo(v3.getX(), v3.getY());
            path.closePath();
            paths.add(path);
        }
        return paths;
    }
}