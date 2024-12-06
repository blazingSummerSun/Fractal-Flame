package backend.academy.fractal.transformations;

public class RaysTransformation implements Transformation {
    private final static double SCALE = 0.5;
    private final static double PSI = 1.5;
    private final static double EPSILON = 1e-6;

    @Override
    public double[] transform(double x, double y) {
        double rSquared = x * x + y * y;
        if (rSquared == 0) {
            rSquared = EPSILON; // Prevent division by zero
        }

        double v44 = Math.sqrt(x * x + y * y); // Magnitude of the vector
        double factor = (v44 * Math.tan(PSI * Math.PI * v44)) / rSquared;

        double newX = SCALE * factor * Math.cos(x);
        double newY = SCALE * factor * Math.sin(y);

        return new double[] {newX, newY};
    }
}
