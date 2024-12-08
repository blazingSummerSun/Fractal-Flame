package backend.academy.fractal.transformations.nonlinear;

import backend.academy.fractal.transformations.Transformation;

public class ExponentialTransformation implements Transformation {
    private static final double SCALE = 1.5;

    @Override
    public double[] transform(double x, double y) {
        double exp = Math.exp(x - 1);

        double newX = SCALE * exp * Math.cos(Math.PI * y);
        double newY = SCALE * exp * Math.sin(Math.PI * y);
        return new double[] {newY, newX};
    }

}
