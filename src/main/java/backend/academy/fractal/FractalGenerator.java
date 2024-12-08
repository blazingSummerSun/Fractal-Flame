package backend.academy.fractal;

import backend.academy.fractal.image.FractalImage;
import backend.academy.fractal.transformations.Transformation;
import backend.academy.fractal.units.AffineMatrix;
import backend.academy.fractal.units.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FractalGenerator {
    private static final double X_MIN = -1.2;
    private static final double X_MAX = 1.2;
    private static final double Y_MIN = -1.2;
    private static final double Y_MAX = 1.2;
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
            AffineMatrix[] matrices,
            int symmetry,
            int numThreads
    ) {
        FractalImage generatedImage = FractalImage.create(width, height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double angleIncrement = 2 * Math.PI / symmetry;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int pointsPerThread = num / numThreads;
        List<Future<FractalImage>> futures = new ArrayList<>();

        for (int thread = 0; thread < numThreads; thread++) {
            Future<FractalImage> future = executor.submit(() -> {
                FractalImage threadImage = FractalImage.create(width, height);
                for (int points = 0; points < pointsPerThread; points++) {
                    double newX = RANDOM.nextDouble(X_MIN, X_MAX);
                    double newY = RANDOM.nextDouble(Y_MIN, Y_MAX);

                    for (int step = INITIAL_ITER; step < maxIterations; step++) {
                        int i = RANDOM.nextInt(matrices.length);
                        double xLinear = matrices[i].a() * newX + matrices[i].b() * newY + matrices[i].c();
                        double yLinear = matrices[i].d() * newX + matrices[i].e() * newY + matrices[i].f();

                        double[] nonLinearTransformation = transformation.transform(xLinear, yLinear);
                        newX = nonLinearTransformation[0];
                        newY = nonLinearTransformation[1];
                        if (step >= 0) {
                            applySymmetry(
                                    symmetry, newX, newY, i, angleIncrement, matrices, threadImage
                            );
                        }
                    }
                }
                return threadImage;
            });
            futures.add(future);
        }

        for (Future<FractalImage> future : futures) {
            try {
                FractalImage threadImage = future.get();
                mergeImages(generatedImage, threadImage);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Some threads failed to complete", e);
            }
        }

        executor.close();

        correction(generatedImage);

        return generateImage(image, generatedImage);
    }

    private void applySymmetry(
            int symmetry,
            double newX,
            double newY,
            int i,
            double angleIncrement,
            AffineMatrix[] matrices,
            FractalImage threadImage) {
        for (int s = 0; s < symmetry; s++) {
            double angle = s * angleIncrement;
            double symX = newX * Math.cos(angle) - newY * Math.sin(angle);
            double symY = newX * Math.sin(angle) + newY * Math.cos(angle);

            if (belongsTo(symX, symY)) {
                int x1 = width - (int) (((X_MAX - symX) / (X_MAX - X_MIN)) * width);
                int y1 = height - (int) (((Y_MAX - symY) / (Y_MAX - Y_MIN)) * height);
                if (x1 < width && y1 < height) {
                    if (threadImage.contains(x1, y1)) {
                        Pixel oldPixel = threadImage.pixel(x1, y1);
                        Pixel newPixel = new Pixel(
                                x1, y1,
                                (oldPixel.r() + matrices[i].red()) / 2,
                                (oldPixel.g() + matrices[i].green()) / 2,
                                (oldPixel.b() + matrices[i].blue()) / 2,
                                oldPixel.hitCount() + 1, 1
                        );
                        threadImage.updatePixel(x1, y1, newPixel);
                    } else {
                        Pixel pixel = new Pixel(
                                x1, y1,
                                matrices[i].red(), matrices[i].green(), matrices[i].blue(), 1, 1
                        );
                        threadImage.updatePixel(x1, y1, pixel);
                    }
                }
            }
        }
    }

    private void mergeImages(FractalImage mainImage, FractalImage threadImage) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel threadPixel = threadImage.pixel(x, y);
                if (mainImage.contains(x, y)) {
                    Pixel mainPixel = mainImage.pixel(x, y);
                    Pixel newPixel = new Pixel(
                            x, y,
                            (mainPixel.r() + threadPixel.r()) / 2,
                            (mainPixel.g() + threadPixel.g()) / 2,
                            (mainPixel.b() + threadPixel.b()) / 2,
                            mainPixel.hitCount() + threadPixel.hitCount(), 1
                    );
                    mainImage.updatePixel(x, y, newPixel);
                } else {
                    mainImage.updatePixel(x, y, threadPixel);
                }
            }
        }
    }


    private static boolean belongsTo(double x, double y) {
        return x >= X_MIN && x <= X_MAX && y >= Y_MIN && y <= Y_MAX;
    }

    private void correction(FractalImage image) {
        double max = getMax(width, height, image);
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

    private static double getMax(int width, int height, FractalImage image) {
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
        return max;
    }

    private BufferedImage generateImage(BufferedImage image, FractalImage generatedImage) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel pixel = generatedImage.pixel(x, y);
                int color = new Color(pixel.r(), pixel.g(), pixel.b()).getRGB();
                image.setRGB(x, y, color);
            }
        }
        return image;
    }
}
