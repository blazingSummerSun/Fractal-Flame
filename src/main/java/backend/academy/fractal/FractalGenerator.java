package backend.academy.fractal;

import backend.academy.fractal.image.FractalImage;
import backend.academy.fractal.transformations.Transformation;
import backend.academy.fractal.units.AffineMatrix;
import backend.academy.fractal.units.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

public class FractalGenerator {
    private static final double XMIN = -1.2;
    private static final double XMAX = 1.2;
    private static final double YMIN = -1.2;
    private static final double YMAX = 1.2;
    private static final double GAMMA = 2.2;
    private static final int INITIAL_ITER = -20;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final int width;
    private final int height;
    private final Transformation transformation;

    public FractalGenerator(int width, int height, Transformation transformation) {
        this.width = width;
        this.height = height;
        this.transformation = transformation;
    }

    public BufferedImage generateFractal(
            int maxIterations,
            int num,
            AffineMatrix[] matrices
    ) {
        FractalImage generatedImage = FractalImage.create(width, height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int points = 0; points < num; points++) {
            double newX = RANDOM.nextDouble(XMIN, XMAX);
            double newY = RANDOM.nextDouble(YMIN, YMAX);

            for (int step = INITIAL_ITER; step < maxIterations; step++) {
                int i = RANDOM.nextInt(matrices.length);
                double x = matrices[i].a() * newX + matrices[i].b() * newY + matrices[i].c();
                double y = matrices[i].d() * newX + matrices[i].e() * newY + matrices[i].f();

                double[] transformed = transformation.transform(x, y);
                newX = transformed[0];
                newY = transformed[1];

                if (step >= 0 && belongsTo(newX, newY)) {
                    int x1 = width - (int) (((XMAX - newX) / (XMAX - XMIN)) * width);
                    int y1 = height - (int) (((YMAX - newY) / (YMAX - YMIN)) * height);
                    if (x1 < width && y1 < height) {
                        if (!generatedImage.contains(x1, y1)) {
                            Pixel pixel = new Pixel(
                                    x1, y1,
                                    matrices[i].red(), matrices[i].green(), matrices[i].blue(), 1, 1
                            );
                            generatedImage.updatePixel(x1, y1, pixel);
                        } else {
                            Pixel oldPixel = generatedImage.pixel(x1, y1);
                            Pixel newPixel = new Pixel(
                                    x1, y1,
                                    (oldPixel.r() + matrices[i].red()) / 2,
                                    (oldPixel.g() + matrices[i].green()) / 2,
                                    (oldPixel.b() + matrices[i].blue()) / 2,
                                    oldPixel.hitCount() + 1, 1
                            );
                            generatedImage.updatePixel(x1, y1, newPixel);
                        }
                    }
                }
            }
        }
        correction(width, height, generatedImage);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pixel = generatedImage.pixel(x, y);
                int color = new Color(pixel.r(), pixel.g(), pixel.b()).getRGB();
                image.setRGB(x, y, color);
            }
        }

        return image;
    }

    private static boolean belongsTo(double x, double y) {
        return x >= XMIN && x <= XMAX && y >= YMIN && y <= YMAX;
    }

    private static void correction(int width, int height, FractalImage image) {
        double max = 0.0;

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                Pixel pixel = image.data()[row][col];
                if (pixel.hitCount() != 0) {
                    double normal = Math.log10(pixel.hitCount());
                    if (normal > max) {
                        max = normal;
                    }
                    image.updatePixel(row, col, new Pixel(
                            pixel.x(), pixel.y(), pixel.r(), pixel.g(), pixel.b(), pixel.hitCount(), normal));
                }
            }
        }
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                Pixel pixel = image.data()[row][col];
                double normalized = pixel.normal() / max;
                double factor = Math.pow(normalized, 1.0 / GAMMA);
                int red = (int) (pixel.r() * factor);
                int green = (int) (pixel.g() * factor);
                int blue = (int) (pixel.b() * factor);
                image.updatePixel(row, col, new Pixel(
                        pixel.x(), pixel.y(), red, green, blue, pixel.hitCount(), normalized));
            }
        }
    }
}
