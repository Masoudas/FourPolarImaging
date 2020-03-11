package fr.fresnel.fourPolar.core.image.orientation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationVectorIteratorTest {
    /**
     * Create a base set of random floats [0, pi/4]. Then set the orientation vector
     * for each angle to that random float plus pi/180. Then use the iterator and
     * check.
     */
    @Test
    public void next_RandomDataSet_ReturnsCorrectOrientationVectorForEachElement() {
        long[] dim = { 10, 10, 10, 10, 3 };

        IPixelCursor<Float32> baseSetCursor = new ImgLib2ImageFactory().create(dim, new Float32()).getCursor();
        IPixelCursor<Float32> rho = new ImgLib2ImageFactory().create(dim, new Float32()).getCursor();
        IPixelCursor<Float32> delta = new ImgLib2ImageFactory().create(dim, new Float32()).getCursor();
        IPixelCursor<Float32> eta = new ImgLib2ImageFactory().create(dim, new Float32()).getCursor();

        Random random = new Random();
        Float32 one = new Float32((float) (Math.PI / 180));

        while (baseSetCursor.hasNext()) {
            Pixel<Float32> pixel = new Pixel<Float32>(new Float32((float)(random.nextFloat() * Math.PI / 4)));

            baseSetCursor.next();
            baseSetCursor.setPixel(pixel);

            rho.next();
            rho.setPixel(pixel);

            pixel.value().add(one);
            delta.next();
            delta.setPixel(pixel);

            pixel.value().add(one);
            eta.next();
            eta.setPixel(pixel);
        }

        baseSetCursor.reset();
        rho.reset();
        delta.reset();
        eta.reset();

        IOrientationVectorIterator iterator = new OrientationVectorIterator(
            rho, delta, eta);

        if (!iterator.hasNext()) {
            assertTrue(false);
        }

        boolean equals = true;
        while (iterator.hasNext()) {
            IOrientationVector orientationVector = iterator.next();
            Float32 angle = baseSetCursor.next().value();

            equals &= _checkPrecision(angle.get(), orientationVector.getAngle(OrientationAngle.rho), 1e-6f);
            equals &= _checkPrecision(angle.get(), orientationVector.getAngle(OrientationAngle.delta) - (float)Math.PI/180, 1e-6f);
            equals &= _checkPrecision(angle.get(), orientationVector.getAngle(OrientationAngle.eta) - (float)Math.PI/90, 1e-6f);
        }

        assertTrue(equals);
    }

    private static boolean _checkPrecision(float val1, float val2, float error) {
        return Math.abs(val1 - val2) < error;        
    }


}