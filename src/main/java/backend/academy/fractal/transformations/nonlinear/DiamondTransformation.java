package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

public class DiamondTransformation implements Transformation {
    private static final double SCALE = 1.25;

    @Override
    public double[] transform(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double theta = Math.atan2(y, x);
        double newX = SCALE * Math.sin(theta) * Math.cos(r);
        double newY = SCALE * Math.cos(theta) * Math.sin(r);
        return new double[] {newX, newY};
    }
}
