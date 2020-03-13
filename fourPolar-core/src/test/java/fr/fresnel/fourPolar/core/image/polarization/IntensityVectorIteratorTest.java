package fr.fresnel.fourPolar.core.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class IntensityVectorIteratorTest {
    /**
     * Create a base set of random integers. Then set the intensity for each
     * polarization to that random integer plus one. Then use the iterator and check
     * against the base set.
     */
    @Test
    public void next_RandomDataSet_ReturnsCorrectIntensityForEachElement() {
        long[] dim = { 10, 10, 10, 10, 3 };

        IPixelCursor<UINT16> baseSetCursor = new ImgLib2ImageFactory().create(dim, new UINT16()).getCursor();
        IPixelCursor<UINT16> pol0Cursor = new ImgLib2ImageFactory().create(dim, new UINT16()).getCursor();
        IPixelCursor<UINT16> pol45Cursor = new ImgLib2ImageFactory().create(dim, new UINT16()).getCursor();
        IPixelCursor<UINT16> pol90Cursor = new ImgLib2ImageFactory().create(dim, new UINT16()).getCursor();
        IPixelCursor<UINT16> pol135Cursor = new ImgLib2ImageFactory().create(dim, new UINT16()).getCursor();

        Random random = new Random();
        UINT16 one = new UINT16(1);
        while (baseSetCursor.hasNext()) {
            Pixel<UINT16> pixel = new Pixel<UINT16>(new UINT16(random.nextInt(100)));

            baseSetCursor.next();
            baseSetCursor.setPixel(pixel);

            pol0Cursor.next();
            pol0Cursor.setPixel(pixel);

            pixel.value().add(one);
            pol45Cursor.next();
            pol45Cursor.setPixel(pixel);

            pixel.value().add(one);
            pol90Cursor.next();
            pol90Cursor.setPixel(pixel);

            pixel.value().add(one);
            pol135Cursor.next();
            pol135Cursor.setPixel(pixel);
        }

        baseSetCursor.reset(); pol0Cursor.reset(); pol45Cursor.reset(); pol90Cursor.reset(); pol135Cursor.reset();

        IIntensityVectorIterator iterator = new IntensityVectorIterator(
            pol0Cursor, pol45Cursor, pol90Cursor, pol135Cursor);

        if (!iterator.hasNext()) {
            assertTrue(false);
        }

        boolean equals = true;
        while (iterator.hasNext()) {
            IntensityVector intensity = iterator.next();
            UINT16 data = baseSetCursor.next().value();

            equals &= data.get() == (int) intensity.getIntensity(Polarization.pol0);
            equals &= data.get() == (int) intensity.getIntensity(Polarization.pol45) - 1;
            equals &= data.get() == (int) intensity.getIntensity(Polarization.pol90) - 2;
            equals &= data.get() == (int) intensity.getIntensity(Polarization.pol135) - 3;
        }

        assertTrue(equals);

    }

}