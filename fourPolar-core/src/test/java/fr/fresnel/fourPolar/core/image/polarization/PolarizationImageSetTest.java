package fr.fresnel.fourPolar.core.image.polarization;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IPolarizationsIntensityIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public class PolarizationImageSetTest {
    @Test
    public void createClass_DifferentImageDimension_ThrowsCannotFormPolarizationImageSet() {
        long[] dim1 = { 1, 1, 1 };
        long[] dim2 = { 2, 1, 1 };

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim1, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim1, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim1, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim2, new UINT16());

        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol90, pol135);
        });
    }

    @Test
    public void createClass_DuplicateImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim = { 1, 1, 1 };

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());

        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol0, pol90, pol135);
        });
        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol0, pol135);
        });
        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol90, pol0);
        });
        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol45, pol135);
        });
        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol90, pol45);
        });
        assertThrows(CannotFormPolarizationImageSet.class, () -> {
            new PolarizationImageSet(null, pol0, pol45, pol90, pol90);
        });
    }

    @Test
    public void getPolarizationImage_ImgLib2PolarizationImage_ThrowsCannotFormPolarizationImageSet()
            throws CannotFormPolarizationImageSet {
        long[] dim = { 1, 1 };

        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());

        IPolarizationImageSet imageSet = new PolarizationImageSet(null, pol0, pol45, pol90, pol135);

        assertTrue(imageSet.getPolarizationImage(Polarization.pol0).getImage() == pol0 &&
            imageSet.getPolarizationImage(Polarization.pol45).getImage() == pol45 &&
            imageSet.getPolarizationImage(Polarization.pol90).getImage() == pol90 &&
            imageSet.getPolarizationImage(Polarization.pol135).getImage() == pol135);
    }

    @Test
    public void getCursor_RandomDataSet_ReturnsCorrectIntensityForEachElement() throws CannotFormPolarizationImageSet {
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

        IPolarizationImageSet polSet = new PolarizationImageSet(null, pol0, pol45, pol90, pol135);
        IPolarizationsIntensityIterator iterator = polSet.getIterator();

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