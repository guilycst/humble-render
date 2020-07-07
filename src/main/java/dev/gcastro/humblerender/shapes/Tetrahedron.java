package dev.gcastro.humblerender.shapes;

import java.util.*;
import java.awt.Color;
import java.awt.geom.*;
import java.awt.image.*;

import dev.gcastro.humblerender.models.*;

public class Tetrahedron implements Shape {

        private List<Triangle> triangles;

        public Tetrahedron() {
                this.triangles = new ArrayList<Triangle>();
                triangles.add(new Triangle(//
                                new Vertex(100, 100, 100), //
                                new Vertex(-100, -100, 100), //
                                new Vertex(-100, 100, -100), //
                                Color.WHITE));

                triangles.add(new Triangle(//
                                new Vertex(100, 100, 100), //
                                new Vertex(-100, -100, 100), //
                                new Vertex(100, -100, -100), //
                                Color.RED));

                triangles.add(new Triangle(//
                                new Vertex(-100, 100, -100), //
                                new Vertex(100, -100, -100), //
                                new Vertex(100, 100, 100), //
                                Color.GREEN));

                triangles.add(new Triangle(//
                                new Vertex(-100, 100, -100), //
                                new Vertex(100, -100, -100), //
                                new Vertex(-100, -100, 100), //
                                Color.BLUE));
        }

        @Override
        public List<Path2D> wireframeBlueprint(Matrix3 rotationMatrix) {
                List<Path2D> paths = new ArrayList<>();
                for (final Triangle part : triangles) {

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

        @Override
        public BufferedImage image(int viewportWidth, int viewportHeight, Matrix3 rotationMatrix) {

                BufferedImage img = new BufferedImage(viewportWidth, viewportHeight, BufferedImage.TYPE_INT_ARGB);
                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

                for (final Triangle triangle : triangles) {
                        List<RasterizedPoint> points = triangle.coordinates(viewportWidth, viewportHeight,
                                        img.getWidth(), img.getHeight(), rotationMatrix);

                        for (RasterizedPoint point : points) {
                                if (zBuffer[point.getzIndex()] < point.getDepth()) {
                                        img.setRGB(point.getX(), point.getY(), point.getColor().getRGB());
                                        zBuffer[point.getzIndex()] = point.getDepth();
                                }
                        }
                }

                return img;
        }

        public BufferedImage inflatedImage(int inflateRate, int viewportWidth, int viewportHeight,
                        Matrix3 rotationMatrix) {

                for (int i = 0; i < inflateRate; i++) {
                        triangles = inflate();
                }

                System.out.println("Will render " + triangles.size() + " triangles");
                return image(viewportWidth, viewportHeight, rotationMatrix);
        }

        private List<Triangle> inflate() {
                List<Triangle> result = new ArrayList<>(triangles.size() * 4);

                for (Triangle t : triangles) {

                        Vertex m1 = new Vertex((t.getVertex1().getX() + t.getVertex2().getX()) / 2,
                                        (t.getVertex1().getY() + t.getVertex2().getY()) / 2,
                                        (t.getVertex1().getZ() + t.getVertex2().getZ()) / 2);
                        Vertex m2 = new Vertex((t.getVertex2().getX() + t.getVertex3().getX()) / 2,
                                        (t.getVertex2().getY() + t.getVertex3().getY()) / 2,
                                        (t.getVertex2().getZ() + t.getVertex3().getZ()) / 2);
                        Vertex m3 = new Vertex((t.getVertex1().getX() + t.getVertex3().getX()) / 2,
                                        (t.getVertex1().getY() + t.getVertex3().getY()) / 2,
                                        (t.getVertex1().getZ() + t.getVertex3().getZ()) / 2);
                        result.add(new Triangle(t.getVertex1(), m1, m3, t.getColor()));
                        result.add(new Triangle(t.getVertex2(), m1, m2, t.getColor()));
                        result.add(new Triangle(t.getVertex3(), m2, m3, t.getColor()));
                        result.add(new Triangle(m1, m2, m3, t.getColor()));
                }

                for (Triangle t : result) {
                        for (Vertex v : new Vertex[] { t.getVertex1(), t.getVertex2(), t.getVertex3() }) {
                                double l = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ())
                                                / Math.sqrt(30000);
                                v.normalize(l);
                        }
                }
                return result;

        }

}