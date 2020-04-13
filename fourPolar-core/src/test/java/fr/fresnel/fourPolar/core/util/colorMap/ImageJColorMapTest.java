package fr.fresnel.fourPolar.core.util.colorMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;

public class ImageJColorMapTest {
    @Test
    public void getColor_REDColorMap_ReturnsProperColors() {
        ImageJColorMap cMap = ImageJColorMap.getDefaultColorMaps("Red.lut");

        double min = 0;
        double max = 1;

        RGB16 white = cMap.getColor(min, max, min);
        RGB16 red = cMap.getColor(min, max, max);

        assertTrue(
            white.getR() == 0 && white.getG() == 0 && white.getB() == 0 &&
            red.getR() == 255 && red.getG() == 0 && red.getB() == 0);
        
    }
    
}