package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

/**
 * The ExponentialTransformation class implements a non-linear transformation that applies an exponential effect
 * to the coordinates. It scales the coordinates based on the exponential function.
 */
public class ExponentialTransformation implements Transformation {
    private static final double SCALE = 1.5;

    /**
     * Transforms the given coordinates by applying an exponential effect.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [newY, newX]
     */
    @Override
    public double[] transform(double x, double y) {
        double exp = Math.exp(x - 1);

        double newX = SCALE * exp * Math.cos(Math.PI * y);
        double newY = SCALE * exp * Math.sin(Math.PI * y);
        return new double[] {newY, newX};
    }

}
