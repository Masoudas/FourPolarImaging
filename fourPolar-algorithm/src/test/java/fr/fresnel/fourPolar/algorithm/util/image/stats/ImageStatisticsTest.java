package fr.fresnel.fourPolar.algorithm.util.image.stats;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class ImageStatisticsTest {

    @Test
    public void getNPlanes_ReturnsCorrectNPlanes() {
        Image<UINT16> image = new ImgLib2ImageFactory().create(new long[]{2, 2, 2, 2, 2, 2}, UINT16.zero());

        assertTrue(ImageStatistics.getNPlanes(image) == 16);

        
    }

    @Test
    public void getMinMax_4PlaneImage_ReturnsCorrectMinMaxPerPlane() {
        Image<UINT16> image = new ImgLib2ImageFactory().create(new long[]{50, 50, 2, 2}, UINT16.zero());

        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();

        long[] loc = {25, 25, 0, 0};
        for (int i = 0; i < 2; i++) {
            loc[3] = i;
            for (int j = 0; j < 2; j++) {
               loc[2] = j;
               ra.setPosition(loc);
               ra.setPixel(new Pixel<UINT16>(new UINT16(25 + i + j)));
            }
        }

        double[][] minMax = ImageStatistics.getPlaneMinMax(image);
        
        boolean equals = true;
        for (int i = 0; i < minMax.length; i++) {
            equals &= minMax[0][i] == 0;
        }

        for (int i = 0; i < minMax.length; i++) {
            equals &= minMax[1][i] == 25 + i;
        }

        assertTrue(equals);
    }

    @Test
    public void getMinMax_CornerMinMax_CatchesCornetMinMax() {
        long[][] loc = {{0, 0}, {0, 49}, {49, 0}, {49, 49}};
        boolean equals = true;

        for (int j = 0; j < 4; j++) {
            Image<UINT16> image = new ImgLib2ImageFactory().create(new long[]{50, 50}, UINT16.zero());

            IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
            ra.setPosition(loc[j]);
            ra.setPixel(new Pixel<UINT16>(new UINT16(25)));
            
            double[][] minMax = ImageStatistics.getPlaneMinMax(image);
            
            equals &= minMax[0][0] == 0;
            equals &= minMax[1][0] == 25;
    
        }

        assertTrue(equals);
    }


}