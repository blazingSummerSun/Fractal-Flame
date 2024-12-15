package backend.academy.fractal.image;

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

/**
 * The FractalGenerator class is responsible for generating fractal images
 * using specified transformations and parameters.
 * It supports multithreading execution to increase performance.
 */
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

    /**
     * Constructs a FractalGenerator.
     *
     * @param width          the width of the generated image
     * @param height         the height of the generated image
     * @param transformation the transformation to apply during fractal generation
     */
    public FractalGenerator(int width, int height, Transformation transformation) {
        this.width = width;
        this.height = height;
        this.transformation = transformation;
    }

    /**
     * Generates a fractal image.
     *
     * @param maxIterations the maximum number of iterations for generating the fractal
     * @param num           the number of points to generate
     * @param matrices      the affine transformation matrices
     * @param symmetry      the symmetry coefficient of the fractal
     * @param numThreads    the number of threads to use for generating the fractal
     * @return the generated fractal image as a BufferedImage
     */
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

        if (numThreads == 1) {
            FractalImage threadImage = generateSnapshot(
                num, maxIterations, matrices, symmetry, angleIncrement
            );
            mergeImages(generatedImage, threadImage);
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            int pointsPerThread = num / numThreads + 1;
            List<Future<FractalImage>> futures = new ArrayList<>();

            for (int thread = 0; thread < numThreads; thread++) {
                Future<FractalImage> future = executor.submit(() -> generateSnapshot(
                    pointsPerThread, maxIterations, matrices, symmetry, angleIncrement
                ));
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
        }

        correction(generatedImage);

        return generateImage(image, generatedImage);
    }

    private FractalImage generateSnapshot(
        int num,
        int maxIterations,
        AffineMatrix[] matrices,
        int symmetry,
        double angleIncrement
    ) {
        FractalImage threadImage = FractalImage.create(width, height);
        for (int points = 0; points < num; points++) {
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
    }

    /**
     * Applies symmetry to the generated fractal points.
     *
     * @param symmetry       the symmetry coefficient
     * @param newX           the X coordinate of the point
     * @param newY           the Y coordinate of the point
     * @param i              the index of the affine matrix
     * @param angleIncrement the angle increment for symmetry
     * @param matrices       the affine transformation matrices
     * @param threadImage    the image being generated by the current thread
     */
    private void applySymmetry(
        int symmetry,
        double newX,
        double newY,
        int i,
        double angleIncrement,
        AffineMatrix[] matrices,
        FractalImage threadImage
    ) {
        for (int s = 0; s < symmetry; s++) {
            double angle = s * angleIncrement;
            double symX = newX * Math.cos(angle) - newY * Math.sin(angle);
            double symY = newX * Math.sin(angle) + newY * Math.cos(angle);

            if (belongsTo(symX, symY)) {
                int x1 = width - (int) (((X_MAX - symX) / (X_MAX - X_MIN)) * width);
                int y1 = height - (int) (((Y_MAX - symY) / (Y_MAX - Y_MIN)) * height);
                if (x1 < width && y1 < height) {
                    updateSymmetryPixel(x1, y1, matrices[i], threadImage);
                }
            }
        }
    }

    private void updateSymmetryPixel(
        int x1,
        int y1,
        AffineMatrix matrix,
        FractalImage threadImage
    ) {
        if (threadImage.contains(x1, y1)) {
            Pixel oldPixel = threadImage.pixel(x1, y1);
            Pixel newPixel = new Pixel(
                x1, y1,
                (oldPixel.r() + matrix.red()) / 2,
                (oldPixel.g() + matrix.green()) / 2,
                (oldPixel.b() + matrix.blue()) / 2,
                oldPixel.hitCount() + 1, 1
            );
            threadImage.updatePixel(x1, y1, newPixel);
        } else {
            Pixel pixel = new Pixel(
                x1, y1,
                matrix.red(), matrix.green(), matrix.blue(), 1, 1
            );
            threadImage.updatePixel(x1, y1, pixel);
        }
    }

    /**
     * Merges the images generated by multiple threads into the main image.
     *
     * @param mainImage   the main fractal image
     * @param threadImage the fractal image generated by a thread
     */
    private void mergeImages(FractalImage mainImage, FractalImage threadImage) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Pixel threadPixel = threadImage.pixel(x, y);
                if (threadPixel.hitCount() > 0) {
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
    }

    /**
     * Checks if the given point belongs to the fractal area.
     *
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return true if the point belongs to the fractal area, false otherwise
     */
    private static boolean belongsTo(double x, double y) {
        return x >= X_MIN && x <= X_MAX && y >= Y_MIN && y <= Y_MAX;
    }

    /**
     * Applies gamma correction to the generated fractal image.
     *
     * @param image the fractal image to be corrected
     */
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

    /**
     * Gets the maximum normalized hit count in the fractal image.
     *
     * @param width  the width of the image
     * @param height the height of the image
     * @param image  the fractal image
     * @return the maximum normalized hit count
     */
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

    /**
     * Generates the final image from the fractal image data.
     *
     * @param image          the BufferedImage to be generated
     * @param generatedImage the fractal image data
     * @return the generated BufferedImage
     */
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
