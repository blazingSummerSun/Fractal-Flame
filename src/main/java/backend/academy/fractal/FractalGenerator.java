package backend.academy.fractal;

import java.awt.image.BufferedImage;
import java.security.SecureRandom;

public class FractalGenerator {
    private static final double XMIN = -1.777;
    private static final double XMAX = 1.777;
    private static final double YMIN = -1;
    private static final double YMAX = 1;
    private static final int MAX_COLOR = 255;
    private static final int START_ITERATIONS = -20;
    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;
    private final SecureRandom random = new SecureRandom();

    public void generateFractal(
        int n,
        int eqCount,
        int iterations,
        int xRes,
        int yRes,
        AffineMatrix[] coeff,
        FractalImage image
    ) {
        for (int num = 0; num < n; num++) {
            double newX = random.nextDouble(XMIN, XMAX);
            double newY = random.nextDouble(YMIN, YMAX);

            for (int step = START_ITERATIONS; step < iterations; step++) {
                int i = random.nextInt(eqCount);
                double x = coeff[i].a() * newX + coeff[i].b() * newY + coeff[i].c();
                double y = coeff[i].d() * newX + coeff[i].e() * newY + coeff[i].f();

                if (step >= 0 && contains(newX, newY)) {
                    int x1 = xRes - (int) (((XMAX - newX) / (XMAX - XMIN)) * xRes);
                    int y1 = yRes - (int) (((YMAX - newY) / (YMAX - YMIN)) * yRes);

                    if (x1 < xRes && y1 < yRes) {
                        Pixel pixel = getPixel(image, x1, y1);
                        image.updatePixel(x1, y1, pixel);
                    }
                }
                newX = x;
                newY = y;
            }
        }
    }

    public boolean contains(double x, double y) {
        return x >= XMIN && x <= XMAX && y >= YMIN && y <= YMAX;
    }

    private static Pixel getPixel(FractalImage image, int x1, int y1) {
        Pixel pixel = image.pixel(x1, y1);
        if (pixel.hitCount() == 0) {
            // Assign a default color or use another method to set the initial color
            pixel = new Pixel(MAX_COLOR, MAX_COLOR, MAX_COLOR, 1); // white color as an example
        } else {
            pixel = new Pixel(
                (pixel.r() + MAX_COLOR) / 2,
                (pixel.g() + MAX_COLOR) / 2,
                (pixel.b() + MAX_COLOR) / 2,
                pixel.hitCount() + 1
            );
        }
        return pixel;
    }

    public BufferedImage createImage(FractalImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.pixel(x, y);
                int color = (pixel.r() << RED_SHIFT) | (pixel.g() << GREEN_SHIFT) | pixel.b();
                bufferedImage.setRGB(x, y, color);
            }
        }
        return bufferedImage;
    }
}
