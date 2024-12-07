package backend.academy.fractal.transformations;

public class FisheyeTransformation implements Transformation {
    private static final double SCALE = 1.25;

    @Override
    public double[] transform(double x, double y) {
        double r = Math.sqrt(x * x + y * y);
        double factor = 2 / (r + 1);
        double newX = SCALE * factor * x;
        double newY = SCALE * factor * y;
        return new double[] {newY, newX};
    }
}
