package backend.academy.fractal.transformations;

/**
 * The Transformation interface defines a method for transforming 2D coordinates.
 * Implementations of this interface will apply specific transformations to the given coordinates.
 */
public interface Transformation {
    /**
     * Transforms the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the transformed coordinates as an array [newX, newY]
     */
    double[] transform(double x, double y);
}
