package fr.fresnel.fourPolar.core.image.generic.pixel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UnsignedShortPixelTest {
    @Test
    public void set_AcceptableRange_ReturnsTheGivenValue() {
        UnsignedShortPixel pixelMin = new UnsignedShortPixel(0);
        UnsignedShortPixel pixelMax = new UnsignedShortPixel((int) (Math.pow(2, 16) - 1));
        UnsignedShortPixel pixelMid = new UnsignedShortPixel(0x3fff);

        assertTrue(pixelMin.get() == UnsignedShortPixel.MIN_VAL && pixelMax.get() == UnsignedShortPixel.MAX_VAL
                && pixelMid.get() == 0x3fff);
    }

    @Test
    public void set_NegativeValue_setsValueToZero() {
        UnsignedShortPixel pixelNeg = new UnsignedShortPixel(-1);

        assertTrue(pixelNeg.get() == UnsignedShortPixel.MIN_VAL);
    }

    @Test
    public void set_OverRangeValue_saturatesToMaxVal() {
        UnsignedShortPixel pixelSat = new UnsignedShortPixel(0xfffff);

        assertTrue(pixelSat.get() == UnsignedShortPixel.MAX_VAL);

    }

    @Test
    public void sum_InRangeValues_ReturnsCorrectSum() {
        UnsignedShortPixel pixel1 = new UnsignedShortPixel(5);
        UnsignedShortPixel pixel2 = new UnsignedShortPixel(10);

        pixel1.sum(pixel2);
        assertTrue(pixel1.get() == 15);
    }

    @Test
    public void sum_SaturatedValues_ReturnsMaxVal() {
        UnsignedShortPixel pixel1 = new UnsignedShortPixel(UnsignedShortPixel.MAX_VAL);
        UnsignedShortPixel pixel2 = new UnsignedShortPixel(1);

        pixel1.sum(pixel2);
        assertTrue(pixel1.get() == UnsignedShortPixel.MAX_VAL);
    }

    @Test
    public void subtract_InRangeValue_ReturnsCorrectResult() {
        UnsignedShortPixel pixel1 = new UnsignedShortPixel(10);
        UnsignedShortPixel pixel2 = new UnsignedShortPixel(5);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 5);
    }

    @Test
    public void subtract_NegativeSubtraction_ReturnsZero() {
        UnsignedShortPixel pixel1 = new UnsignedShortPixel(5);
        UnsignedShortPixel pixel2 = new UnsignedShortPixel(10);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 0);
    }

}