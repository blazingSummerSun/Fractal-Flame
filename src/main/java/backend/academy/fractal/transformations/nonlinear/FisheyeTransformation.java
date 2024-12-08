package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

/**
 * The FisheyeTransformation class implements a non-linear transformation that applies a fisheye effect
 * to the coordinates. It scales the coordinates based on their distance from the origin.
 */
public class FisheyeTransformation implements Transformation {
    private static final double SCALE = 1;

    /**
     * Transforms the given coordinates by applying a fisheye effect.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [newX, newY]
     */
    @Override
    public double[] transform(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double factor = 2 / (r + 1);
        double newX = SCALE * factor * y;
        double newY = SCALE * factor * x;
        return new double[] {newX, newY};
    }
}
