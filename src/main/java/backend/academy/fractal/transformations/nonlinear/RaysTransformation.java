package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

/**
 * The RaysTransformation class implements a non-linear transformation that applies a rays effect
 * to the coordinates. It scales the coordinates based on their distance from the origin and an angular factor.
 */
public class RaysTransformation implements Transformation {
    private final static double SCALE = 0.5;
    private final static double PSI = 1.5;
    private final static double EPSILON = 1e-6;

    /**
     * Transforms the given coordinates by applying a rays effect.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [newX, newY]
     */
    @Override
    public double[] transform(double x, double y) {
        double rSquared = x * x + y * y;
        if (rSquared == 0) {
            rSquared = EPSILON; // Prevent division by zero
        }

        double v44 = Math.sqrt(x * x + y * y);
        double factor = (v44 * Math.tan(PSI * Math.PI * v44)) / rSquared;

        double newX = SCALE * factor * Math.cos(x);
        double newY = SCALE * factor * Math.sin(y);

        return new double[] {newX, newY};
    }
}
