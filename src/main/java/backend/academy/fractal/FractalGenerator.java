package backend.academy.fractal;

import java.awt.image.BufferedImage;

public interface FractalGenerator {
    BufferedImage generateFractal(
            int maxIterations,
            int num,
            AffineMatrix[] matrices,
            int xRes,
            int yRes);
}
