package backend.academy.fractal.units;

/**
 * The AffineMatrix record represents an affine transformation matrix with additional color components.
 * It includes six double values for the transformation matrix and three integer values for the RGB color components.
 *
 * @param a     the 'a' component of the affine transformation matrix
 * @param b     the 'b' component of the affine transformation matrix
 * @param c     the 'c' component of the affine transformation matrix
 * @param d     the 'd' component of the affine transformation matrix
 * @param e     the 'e' component of the affine transformation matrix
 * @param f     the 'f' component of the affine transformation matrix
 * @param red   the red component of the color
 * @param green the green component of the color
 * @param blue  the blue component of the color
 */
@SuppressWarnings("RecordComponentNumber")
public record AffineMatrix(double a, double b, double c, double d, double e, double f, int red, int green, int blue) {
}
