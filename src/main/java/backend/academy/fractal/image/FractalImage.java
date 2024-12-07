package backend.academy.fractal.image;

import backend.academy.fractal.units.Pixel;

public record FractalImage(Pixel[][] data, int width, int height) {
    public static FractalImage create(int width, int height) {
        Pixel[][] data = new Pixel[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = new Pixel(i, j, 0, 0, 0, 0, 1);
            }
        }
        return new FractalImage(data, width, height);
    }

    public boolean contains(int x, int y) {
        return data[x][y].hitCount() > 0;
    }

    public Pixel pixel(int x, int y) {
        return data[x][y];
    }

    public void updatePixel(int x, int y, Pixel pixel) {
        data[x][y] = pixel;
    }
}
