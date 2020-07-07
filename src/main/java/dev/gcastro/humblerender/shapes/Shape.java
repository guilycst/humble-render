package dev.gcastro.humblerender.shapes;

import java.awt.geom.*;
import java.awt.image.*;
import java.util.List;

import dev.gcastro.humblerender.models.Matrix3;

public interface Shape {

    List<Path2D> wireframeBlueprint(Matrix3 rotationMatrix);
    BufferedImage image(int containerWidth, int containerHeight, Matrix3 rotationMatrix);
    
}