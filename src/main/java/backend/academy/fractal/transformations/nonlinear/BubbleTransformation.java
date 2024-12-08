package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

/**
 * The BubbleTransformation class implements a non-linear transformation that applies a bubble effect
 * to the coordinates. It scales the coordinates based on their distance from the origin.
 */
public class BubbleTransformation implements Transformation {
    private static final double SCALE = 1.7;
    private static final int BUBBLE_RADIUS = 4;

    /**
     * Transforms the given coordinates by applying a bubble effect.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [bubbleX, bubbleY]
     */
    @Override
    public double[] transform(double x, double y) {
        double rSquared = x * x + y * y;
        double factor = BUBBLE_RADIUS / (rSquared + BUBBLE_RADIUS);
        double bubbleX = SCALE * factor * x;
        double bubbleY = SCALE * factor * y;
        return new double[] {bubbleX, bubbleY};
    }
}
