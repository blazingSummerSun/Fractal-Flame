package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

/**
 * The DiamondTransformation class implements a non-linear transformation that applies a diamond effect
 * to the coordinates. It scales the coordinates based on their polar coordinates.
 */
public class DiamondTransformation implements Transformation {
    private static final double SCALE = 1.25;

    /**
     * Transforms the given coordinates by applying a diamond effect.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [newX, newY]
     */
    @Override
    public double[] transform(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double theta = Math.atan2(y, x);
        double newX = SCALE * Math.sin(theta) * Math.cos(r);
        double newY = SCALE * Math.cos(theta) * Math.sin(r);
        return new double[] {newX, newY};
    }
}
