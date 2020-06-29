package fr.fresnel.fourPolar.core.util.image.colorMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;

public class ImageJColorMapTest {
    @Test
    public void getColor_REDColorMap_ReturnsProperColors() {
        ImageJColorMap cMap = ImageJColorMap.getDefaultColorMaps("Red.lut");

        double max = 1;

        ARGB8 red = cMap.getColor(0, 1, max);

        assertTrue(
            red.getR() == 255 && red.getG() == 0 && red.getB() == 0);
        
    }
    
}