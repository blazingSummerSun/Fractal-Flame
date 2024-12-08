package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

public class BubbleTransformation implements Transformation {
    private static final double SCALE = 1.7;
    private static final int BUBBLE_RADIUS = 4;

    @Override
    public double[] transform(double x, double y) {
        double rSquared = x * x + y * y;
        double factor = BUBBLE_RADIUS / (rSquared + BUBBLE_RADIUS);
        double bubbleX = SCALE * factor * x;
        double bubbleY = SCALE * factor * y;
        return new double[] {bubbleX, bubbleY};
    }
}
