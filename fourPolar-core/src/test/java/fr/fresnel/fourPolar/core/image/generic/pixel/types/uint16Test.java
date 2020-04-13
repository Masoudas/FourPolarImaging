package fr.fresnel.fourPolar.core.image.generic.pixel.types;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class uint16Test {
    @Test
    public void set_AcceptableRange_ReturnsTheGivenValue() {
        UINT16 pixelMin = new UINT16(0);
        UINT16 pixelMax = new UINT16((int) (Math.pow(2, 16) - 1));
        UINT16 pixelMid = new UINT16(0x3fff);

        assertTrue(pixelMin.get() == UINT16.MIN_VAL && pixelMax.get() == UINT16.MAX_VAL
                && pixelMid.get() == 0x3fff);
    }

    @Test
    public void set_NegativeValue_setsValueToZero() {
        UINT16 pixelNeg = new UINT16(-1);

        assertTrue(pixelNeg.get() == UINT16.MIN_VAL);
    }

    @Test
    public void set_OverRangeValue_saturatesToMaxVal() {
        UINT16 pixelSat = new UINT16(0xfffff);

        assertTrue(pixelSat.get() == UINT16.MAX_VAL);

    }

    @Test
    public void sum_InRangeValues_ReturnsCorrectSum() {
        UINT16 pixel1 = new UINT16(5);
        UINT16 pixel2 = new UINT16(10);

        pixel1.add(pixel2);
        assertTrue(pixel1.get() == 15);
    }

    @Test
    public void sum_SaturatedValues_ReturnsMaxVal() {
        UINT16 pixel1 = new UINT16(UINT16.MAX_VAL);
        UINT16 pixel2 = new UINT16(1);

        pixel1.add(pixel2);
        assertTrue(pixel1.get() == UINT16.MAX_VAL);
    }

    @Test
    public void subtract_InRangeValue_ReturnsCorrectResult() {
        UINT16 pixel1 = new UINT16(10);
        UINT16 pixel2 = new UINT16(5);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 5);
    }

    @Test
    public void subtract_NegativeSubtraction_ReturnsZero() {
        UINT16 pixel1 = new UINT16(5);
        UINT16 pixel2 = new UINT16(10);

        pixel1.subtract(pixel2);
        assertTrue(pixel1.get() == 0);
    }

    @Test
    public void copy_CopyType_ShouldReturnNewInstanceSameValue() {
        UINT16 u16 = new UINT16(10);

        UINT16 u16_coUint16 = (UINT16)u16.copy();

        assertTrue(u16_coUint16 != u16 && u16_coUint16.get() == u16.get());
        
    }

}