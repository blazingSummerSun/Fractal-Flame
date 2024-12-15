package backend.academy.fractal.image;

import backend.academy.fractal.units.Pixel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FractalImageTest {
    @Test
    void shouldUpdatePixel() {
        Pixel[][] data = new Pixel[2][2];
        data[0][0] = new Pixel(0, 0, 0, 0, 0, 0, 1);
        data[0][1] = new Pixel(0, 1, 0, 0, 0, 0, 1);
        data[1][0] = new Pixel(1, 0, 0, 0, 0, 0, 1);
        data[1][1] = new Pixel(1, 1, 0, 0, 0, 0, 1);
        FractalImage fractalImage = new FractalImage(data, 2, 2);
        Pixel pixel = new Pixel(0, 0, 255, 255, 255, 1, 1);

        fractalImage.updatePixel(0, 0, pixel);

        assertEquals(pixel, fractalImage.pixel(0, 0));
    }

    @Test
    void shouldFindPixel() {
        Pixel[][] data = new Pixel[2][2];
        data[0][0] = new Pixel(0, 0, 0, 0, 0, 0, 1);
        data[0][1] = new Pixel(0, 1, 0, 0, 0, 0, 1);
        data[1][0] = new Pixel(1, 0, 0, 0, 0, 0, 1);
        data[1][1] = new Pixel(1, 1, 0, 0, 0, 10, 1);
        FractalImage fractalImage = new FractalImage(data, 2, 2);

        assertTrue(fractalImage.contains(1, 1));
    }

    @Test
    void shouldNotFindPixel() {
        Pixel[][] data = new Pixel[2][2];
        data[0][0] = new Pixel(0, 0, 0, 0, 0, 0, 1);
        data[0][1] = new Pixel(0, 1, 0, 0, 0, 4, 1);
        data[1][0] = new Pixel(1, 0, 0, 0, 0, 5, 1);
        data[1][1] = new Pixel(1, 1, 0, 0, 0, 10, 1);
        FractalImage fractalImage = new FractalImage(data, 2, 2);

        assertFalse(fractalImage.contains(0, 0));
    }
}
