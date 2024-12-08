package backend.academy;

import backend.academy.fractal.image.FractalGenerator;
import backend.academy.fractal.image.ImageFormat;
import backend.academy.fractal.image.ImageUtils;
import backend.academy.fractal.transformations.Transformation;
import backend.academy.fractal.transformations.linear.AffineTransformations;
import backend.academy.fractal.transformations.nonlinear.BubbleTransformation;
import backend.academy.fractal.transformations.nonlinear.DiamondTransformation;
import backend.academy.fractal.transformations.nonlinear.ExponentialTransformation;
import backend.academy.fractal.transformations.nonlinear.FisheyeTransformation;
import backend.academy.fractal.transformations.nonlinear.RaysTransformation;
import backend.academy.fractal.units.AffineMatrix;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.SecureRandom;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class for generating fractal images based on user input.
 * This class handles the command line interface for gathering parameters
 * and generating the fractal image using the specified transformations and parameters.
 */
@Slf4j @UtilityClass
public class Main {
    private static final int TRANSFORMATIONS_COUNT = 5;
    private final SecureRandom random = new SecureRandom();
    private int stage = 1;

    /**
     * Entry point of the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        PrintStream output = System.out;
        printUsage(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        try {
            printStage(output);
            int width = Integer.parseInt(reader.readLine());

            printStage(output);
            int height = Integer.parseInt(reader.readLine());

            printStage(output);
            int nonLinearTransformations = Integer.parseInt(reader.readLine());
            Transformation transformation = getTransformation(nonLinearTransformations);

            printStage(output);
            int iterations = Integer.parseInt(reader.readLine());

            printStage(output);
            int points = Integer.parseInt(reader.readLine());

            printStage(output);
            int matrices = Integer.parseInt(reader.readLine());
            AffineMatrix[] affineMatrices = new AffineMatrix[matrices];
            if (matrices == 0) {
                int randomIndex = random.nextInt(TRANSFORMATIONS_COUNT);
                affineMatrices = new AffineTransformations().getTransformation(randomIndex);
            } else {
                for (int i = 0; i < nonLinearTransformations; i++) {
                    output.println("Enter the affine transformation matrix:");
                    affineMatrices[i] = fillMatrix(reader);
                }
            }

            printStage(output);
            int symmetry = Integer.parseInt(reader.readLine());

            printStage(output);
            int threads = Integer.parseInt(reader.readLine());

            printStage(output);
            int format = Integer.parseInt(reader.readLine());

            printStage(output);
            FractalGenerator generator = new FractalGenerator(width, height, transformation);
            BufferedImage image = generator.generateFractal(iterations, points, affineMatrices, symmetry, threads);
            generateImage(image, format);

            output.println("The fractal has been generated at fractal.[extension]");

        } catch (IOException e) {
            log.error("Error while reading the input");
        } catch (NumberFormatException e) {
            log.error("Invalid input! Enter a valid number.");
        }
    }

    /**
     * Prints the usage instructions for the application.
     *
     * @param output the output stream to print the usage instructions to
     */
    private static void printUsage(PrintStream output) {
        output.println("""
            To generate a fractal image, provide the following arguments:
            1. The width of the image in pixels
            2. The height of the image in pixels
            3. Desired non-linear transformation (enter the number):
                0. Random
                1. Diamond
                2. Bubble
                3. Fisheye
                4. Exponential
                5. Rays
            4. The number of iterations to generate the fractal
            5. The number of points to generate in the fractal
            6. The number of N transformations to use in the fractal
                6.1 N transformations to use in the fractal in the format:
                a b c d e f r g b
                (if N == 0, random N will be chosen from the predetermined list)
            7. The symmetry coefficient of the fractal
            8. The number of threads to use for generating the fractal
            9. Format:
                1. PNG
                2. JPEG
                3. BMP
            """);
    }

    /**
     * Returns a Transformation object based on the specified index.
     *
     * @param index the index of the desired transformation
     * @return the Transformation object corresponding to the specified index
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private Transformation getTransformation(int index) {
        int randomIndex = index;
        if (randomIndex == 0) {
            randomIndex = random.nextInt(TRANSFORMATIONS_COUNT) + 1;
        }
        return switch (randomIndex) {
            case 1 -> new DiamondTransformation();
            case 2 -> new BubbleTransformation();
            case 3 -> new FisheyeTransformation();
            case 4 -> new ExponentialTransformation();
            default -> new RaysTransformation();
        };
    }

    /**
     * Reads and returns an AffineMatrix object from the provided BufferedReader.
     *
     * @param reader the BufferedReader to read the matrix from
     * @return the AffineMatrix object read from the input
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private AffineMatrix fillMatrix(BufferedReader reader) {
        try {
            String unparsedMatrix = reader.readLine();
            if (unparsedMatrix != null) {
                String[] affineTransformation = unparsedMatrix.split(" ");
                double a = Double.parseDouble(affineTransformation[0]);
                double b = Double.parseDouble(affineTransformation[1]);
                double c = Double.parseDouble(affineTransformation[2]);
                double d = Double.parseDouble(affineTransformation[3]);
                double e = Double.parseDouble(affineTransformation[4]);
                double f = Double.parseDouble(affineTransformation[5]);
                int red = Integer.parseInt(affineTransformation[6]);
                int green = Integer.parseInt(affineTransformation[7]);
                int blue = Integer.parseInt(affineTransformation[8]);
                return new AffineMatrix(a, b, c, d, e, f, red, green, blue);
            }
        } catch (IOException e) {
            log.error("Error while filling linear transformation! Pick a random one instead.");
        }
        return new AffineTransformations().getTransformation(random.nextInt(TRANSFORMATIONS_COUNT))[0];
    }

    /**
     * Prints the current stage of input required from the user.
     *
     * @param output the output stream to print the stage message to
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private void printStage(PrintStream output) {
        switch (stage) {
            case 1:
                output.println("1. Enter the width of the image in pixels:");
                stage++;
                break;
            case 2:
                output.println("2. Enter the height of the image in pixels:");
                stage++;
                break;
            case 3:
                output.println("3. Enter the desired non-linear transformation (enter the number):");
                stage++;
                break;
            case 4:
                output.println("4. Enter the number of iterations to generate the fractal:");
                stage++;
                break;
            case 5:
                output.println("5. Enter the number of points to generate in the fractal:");
                stage++;
                break;
            case 6:
                output.println("6. Enter the number of N transformations to use in the fractal:");
                stage++;
                break;
            case 7:
                output.println("7. Enter the symmetry coefficient of the fractal:");
                stage++;
                break;
            case 8:
                output.println("8. Enter the number of threads to use for generating the fractal:");
                stage++;
                break;
            case 9:
                output.println("9. Enter the format of the image:");
                stage++;
                break;
            default:
                output.println("Generating fractal...");
        }
    }

    /**
     * Prints the current stage of input required from the user.
     *
     * @param image the BufferedImage object to save
     * @param index the index of the image format to save the image in
     */
    private void generateImage(BufferedImage image, int index) {
        switch (index) {
            case 1:
                ImageUtils.save(image, Paths.get("fractal.png"), ImageFormat.PNG);
                break;
            case 2:
                ImageUtils.save(image, Paths.get("fractal.jpeg"), ImageFormat.JPEG);
                break;
            default:
                ImageUtils.save(image, Paths.get("fractal.bmp"), ImageFormat.BMP);
        }
    }
}
