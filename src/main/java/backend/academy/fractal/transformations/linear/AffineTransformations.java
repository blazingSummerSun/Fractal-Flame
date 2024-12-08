package backend.academy.fractal.transformations.linear;

import backend.academy.fractal.units.AffineMatrix;

/**
 * The AffineTransformations class holds a collection of predefined affine transformations.
 * It provides a method to pick a set of transformations based on an index.
 */
public class AffineTransformations {

    @SuppressWarnings("checkstyle:MagicNumber")
    AffineMatrix[][] transformations = {
        {
            new AffineMatrix(1, 0, 0, 0, 1, 0, 255, 255, 255),
            new AffineMatrix(1.5, 0, 0, 0, 1.5, 0, 147, 112, 219),
            new AffineMatrix(Math.cos(Math.PI / 4), -Math.sin(Math.PI / 4), 0,
                Math.sin(Math.PI / 4), Math.cos(Math.PI / 4), 0,
                48, 177, 206),
            new AffineMatrix(1, 0.2, 0, 0.2, 1, 0, 0, 0, 255),
            new AffineMatrix(1, 0, 0.5, 0, 1, -0.5, 0, 0, 255)
        },
        {
            new AffineMatrix(0.6, 0.0, -0.5, 0.0, 0.6, 0.5, 160, 32, 240),
            new AffineMatrix(0.6, 0.0, 0.5, 0.0, 0.6, 0.5, 255, 215, 0),
            new AffineMatrix(0.6, 0.0, -0.5, 0.0, 0.6, -0.5, 255, 127, 80),
            new AffineMatrix(0.6, 0.0, 0.5, 0.0, 0.6, -0.5, 138, 40, 210),
            new AffineMatrix(1.5, 0, 0, 0, 1.5, 0, 255, 255, 255),
            new AffineMatrix(Math.cos(Math.PI / 4), -Math.sin(Math.PI / 4), 0,
                Math.sin(Math.PI / 4), Math.cos(Math.PI / 4), 0,
                255, 0, 0)
        },
        {
            new AffineMatrix(0.9, 0.2, 0.0, 0.1, 0.8, 0.0, 255, 105, 180),
            new AffineMatrix(0.6, 0.0, -0.2, 0.0, 0.6, 0.2, 135, 206, 250),
            new AffineMatrix(0.7, -0.3, 0.0, -0.3, 0.7, 0.0, 255, 215, 0),
            new AffineMatrix(0.8, 0.1, 0.3, 0.1, 0.8, -0.3, 124, 252, 0),
        },
        {
            new AffineMatrix(0.8, -0.2, 0.1, 0.2, 0.8, -0.1, 255, 99, 71),
            new AffineMatrix(0.7, 0.0, 0.0, 0.0, 0.9, 0.2, 135, 206, 250),
            new AffineMatrix(-0.6, 0.4, 0.1, 0.4, 0.6, -0.3, 144, 238, 144),
            new AffineMatrix(0.5, -0.5, -0.2, 0.5, 0.5, 0.1, 255, 215, 0),
            new AffineMatrix(0.9, 0.1, 0.0, 0.1, 0.9, 0.2, 75, 0, 130)
        },
        {
            new AffineMatrix(0.7, -0.2, 0.0, 0.2, 0.7, 0.0, 255, 165, 0),
            new AffineMatrix(0.6, 0.0, -0.1, 0.0, 0.8, 0.2, 70, 130, 180),
            new AffineMatrix(-0.5, 0.3, 0.2, 0.3, 0.5, -0.2, 220, 20, 60),
            new AffineMatrix(0.5, 0.0, -0.2, 0.0, 0.5, 0.3, 124, 252, 0),
            new AffineMatrix(0.4, -0.4, 0.1, 0.4, 0.4, -0.1, 255, 255, 51)
        }
    };

    /**
     * Pick a set of affine transformations based on the specified index.
     *
     * @param index the index of the transformation set to get
     * @return an array of AffineMatrix objects representing the transformation set
     */
    public AffineMatrix[] getTransformation(int index) {
        return transformations[index];
    }
}
