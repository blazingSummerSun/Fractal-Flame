package backend.academy.fractal.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageUtils {
    private ImageUtils() {
    }

    public static void save(BufferedImage image, Path filename, ImageFormat format) {
        try {
            ImageIO.write(image, format.name(), new File(filename.toString()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}
