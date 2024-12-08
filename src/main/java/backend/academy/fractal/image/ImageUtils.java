package backend.academy.fractal.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * Utility class for operations related to an image.
 */
public class ImageUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageUtils() {
    }

    /**
     * Saves a BufferedImage to a file in the specified format.
     *
     * @param image    the BufferedImage to save
     * @param filename the path of the file to save the image to
     * @param format   the format to save the image in
     * @throws RuntimeException if an error occurs while saving the image
     */
    public static void save(BufferedImage image, Path filename, ImageFormat format) {
        try {
            ImageIO.write(image, format.name(), new File(filename.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
