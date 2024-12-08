package backend.academy.fractal.image;

import backend.academy.fractal.units.Pixel;

/**
 * The FractalImage class represents a fractal image consisting of a 2D array of Pixel objects.
 * It provides methods to create a new fractal image, check if a pixel is part of the fractal,
 * find a pixel, and update a pixel in the image.
 */
public record FractalImage(Pixel[][] data, int width, int height) {

    /**
     * Creates a new FractalImage with the specified width and height.
     * Initializes all pixels to black with a hit count of 0 and normal value of 1.
     *
     * @param width  the width of the fractal image
     * @param height the height of the fractal image
     * @return a new FractalImage with the specified dimensions
     */
    public static FractalImage create(int width, int height) {
        Pixel[][] data = new Pixel[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = new Pixel(i, j, 0, 0, 0, 0, 1);
            }
        }
        return new FractalImage(data, width, height);
    }

    /**
     * Checks if the pixel at the specified coordinates was generated before.
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return true if the pixel's hit count is greater than 0, false otherwise
     */
    public boolean contains(int x, int y) {
        return data[x][y].hitCount() > 0;
    }

    /**
     * Retrieves the Pixel object at the specified coordinates.
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     * @return the Pixel object at the specified coordinates
     */
    public Pixel pixel(int x, int y) {
        return data[x][y];
    }

    /**
     * Updates the Pixel object at the specified coordinates with the provided Pixel object.
     *
     * @param x     the x-coordinate of the pixel
     * @param y     the y-coordinate of the pixel
     * @param pixel the new Pixel object to replace the existing one
     */
    public void updatePixel(int x, int y, Pixel pixel) {
        data[x][y] = pixel;
    }
}
