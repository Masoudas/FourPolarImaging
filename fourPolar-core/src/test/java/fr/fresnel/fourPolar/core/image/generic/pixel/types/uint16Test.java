package fr.fresnel.fourPolar.core.image.generic.pixel.types;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class uint16Test {
    @Test
    public void set_AcceptableRange_ReturnsTheGivenValue() {
        uint16 pixelMin = new uint16(0);
        uint16 pixelMax = new uint16((int) (Math.pow(2, 16) - 1));
        uint16 pixelMid = new uint16(0x3fff);

        assertTrue(pixelMin.get() == uint16.MIN_VAL && pixelMax.get() == uint16.MAX_VAL
                && pixelMid.get() == 0x3fff);
    }

    @Test
    public void set_NegativeValue_setsValueToZero() {
        uint16 pixelNeg = new uint16(-1);

        assertTrue(pixelNeg.get() == uint16.MIN_VAL);
    }

    @Test
    public void set_OverRangeValue_saturatesToMaxVal() {
        uint16 pixelSat = new uint16(0xfffff);

        assertTrue(pixelSat.get() == uint16.MAX_VAL);

    }

    @Test
    public void sum_InRangeValues_ReturnsCorrectSum() {
        uint16 pixel1 = new uint16(5);
        uint16 pixel2 = new uint16(10);

        pixel1.sum(pixel2);
        assertTrue(pixel1.get() == 15);
    }

    @Test
    public void sum_SaturatedValues_ReturnsMaxVal() {
        uint16 pixel1 = new uint16(uint16.MAX_VAL);
        uint16 pixel2 = new uint16(1);

        pixel1.sum(pixel2);
        assertTrue(pixel1.get() == uint16.MAX_VAL);
    }

    @Test
    public void subtract_InRangeValue_ReturnsCorrectResult() {
        uint16 pixel1 = new uint16(10);
        uint16 pixel2 = new uint16(5);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 5);
    }

    @Test
    public void subtract_NegativeSubtraction_ReturnsZero() {
        uint16 pixel1 = new uint16(5);
        uint16 pixel2 = new uint16(10);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 0);
    }

}