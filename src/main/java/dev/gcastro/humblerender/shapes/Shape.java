package dev.gcastro.humblerender.shapes;

import java.awt.geom.*;
import java.util.List;

import dev.gcastro.humblerender.models.Matrix3;

public interface Shape {

    List<Path2D> blueprint(Matrix3 rotationMatrix);
    
}