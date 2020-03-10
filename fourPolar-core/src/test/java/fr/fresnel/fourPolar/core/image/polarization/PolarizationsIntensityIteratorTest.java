package fr.fresnel.fourPolar.core.image.polarization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.fourPolar.IPolarizationsIntensityIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationsIntensityIteratorTest {
    @Test
    public void next_RandomDataSet_ReturnsCorrectIntensityForEachElement() {
        long[] dim = { 10, 10, 10, 10, 10 };

        Image<UINT16> baseSet = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());

        Random random = new Random();

        UINT16 one = new UINT16(1);
        IPixelCursor<UINT16> baseSetCursor = baseSet.getCursor();

        IPixelCursor<UINT16> pol0Cursor = pol0.getCursor();
        IPixelCursor<UINT16> pol45Cursor = pol45.getCursor();
        IPixelCursor<UINT16> pol90Cursor = pol90.getCursor();
        IPixelCursor<UINT16> pol135Cursor = pol135.getCursor();
        while (baseSetCursor.hasNext()) {
            baseSetCursor.next();
            UINT16 data = new UINT16(random.nextInt(100));
            baseSetCursor.setPixel(new Pixel<UINT16>(data));

            pol0Cursor.next();
            pol0Cursor.setPixel(new Pixel<UINT16>(data));

            data.sum(one);
            pol45Cursor.next();
            pol45Cursor.setPixel(new Pixel<UINT16>(data));

            data.sum(one);
            pol90Cursor.next();
            pol90Cursor.setPixel(new Pixel<UINT16>(data));

            data.sum(one);
            pol135Cursor.next();
            pol135Cursor.setPixel(new Pixel<UINT16>(data));

        }

        IPolarizationsIntensityIterator iterator = new PolarizationsIntensityIterator(
            pol0.getCursor(), pol45.getCursor(), pol90.getCursor(), pol135.getCursor());

        baseSetCursor.reset();
        boolean equals = true;
        while (iterator.hasNext()) {
            IPolarizationsIntensity intensity = iterator.next();

            UINT16 data = baseSetCursor.next().value();

            equals &= data.get() == (int)intensity.getIntensity(Polarization.pol0);
            equals &= data.get() == (int)intensity.getIntensity(Polarization.pol45) - 1;
            equals &= data.get() == (int)intensity.getIntensity(Polarization.pol90) - 2;
            equals &= data.get() == (int)intensity.getIntensity(Polarization.pol135) - 3;
        }
        
        assertTrue(equals);
        
    }
    
}