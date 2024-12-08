package backend.academy.fractal.units;

/**
 * The Pixel record represents a pixel in a fractal image.
 * It contains the coordinates of the pixel, its RGB color components, the hit count, and the normal value.
 *
 * @param x the x-coordinate of the pixel
 * @param y the y-coordinate of the pixel
 * @param r the red component of the pixel's color
 * @param g the green component of the pixel's color
 * @param b the blue component of the pixel's color
 * @param hitCount the number of times the pixel has been hit
 * @param normal the normal value of the pixel
 */
public record Pixel(int x, int y, int r, int g, int b, int hitCount, double normal) {
}
