package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

public class FisheyeTransformation implements Transformation {
    private static final double SCALE = 1;

    @Override
    public double[] transform(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double factor = 2 / (r + 1);
        double newX = SCALE * factor * y;
        double newY = SCALE * factor * x;
        return new double[] {newX, newY};
    }
}
