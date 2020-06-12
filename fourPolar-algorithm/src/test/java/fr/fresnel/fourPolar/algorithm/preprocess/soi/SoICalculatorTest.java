package fr.fresnel.fourPolar.algorithm.preprocess.soi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;

public class SoICalculatorTest {
    @Test
    public void calculateUINT16Sum_MAX_VAL_ReturnsMAX_VALForAllCases() {
        ISoICalculator calculator = SoICalculator.create();

        IPixelCursor<UINT16> pixelCursor = new TestPixelCursor();
        calculator.calculateUINT16Sum(new TestIntensityIterator(), pixelCursor);

        pixelCursor.reset();
        while (pixelCursor.hasNext()) {
            assertTrue(pixelCursor.next().value().get() == UINT16.MAX_VAL);    
        }
        

    }

}

class TestIntensityIterator implements IIntensityVectorIterator {
    static int len = 5;
    int counter = 0;

    IntensityVector[] vec = new IntensityVector[len];

    public TestIntensityIterator() {
        vec[0] = new IntensityVector(UINT16.MAX_VAL, UINT16.MAX_VAL, UINT16.MAX_VAL, UINT16.MAX_VAL);
        vec[1] = new IntensityVector(UINT16.MAX_VAL, 0, 0, 0);
        vec[2] = new IntensityVector(0, UINT16.MAX_VAL, 0, 0);
        vec[3] = new IntensityVector(0, 0, UINT16.MAX_VAL, 0);
        vec[4] = new IntensityVector(0, 0, 0, UINT16.MAX_VAL);
    }

    @Override
    public boolean hasNext() {
        return counter < len;
    }

    @Override
    public IntensityVector next() {
        return vec[counter++];
    }

    @Override
    public long size() {
        return len;
    }

}

class TestPixelCursor implements IPixelCursor<UINT16> {
    UINT16[] pixel = new UINT16[TestIntensityIterator.len];

    int counter = 0;
    public TestPixelCursor(){
        for (int i = 0; i < pixel.length; i++) {
            pixel[i] = UINT16.zero();
        }

    }

    @Override
    public boolean hasNext() {
        return counter < TestIntensityIterator.len;
    }

    @Override
    public IPixel<UINT16> next() {
        return new Pixel<UINT16>(pixel[counter++]);
    }

    @Override
    public long[] localize() {
        return null;
    }

    @Override
    public void setPixel(IPixel<UINT16> pixel) {
        this.pixel[--counter] = pixel.value();
        counter++;
    }

    @Override
    public void reset() {
        counter = 0;

    }

    @Override
    public long size() {
        return 0;
    }
    
}