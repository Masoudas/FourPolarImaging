package fr.fresnel.fourPolar.core.image.orientation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

public class OrientationImageTest {
    @Test
    public void createClass_DifferentImageDimension_ThrowsCannotFormPolarizationImageSet() {
        long[] dim1 = { 1, 1 };
        long[] dim2 = { 2, 1 };

        Image<Float32> rho = new ImgLib2ImageFactory().create(dim1, new Float32());
        Image<Float32> eta = new ImgLib2ImageFactory().create(dim1, new Float32());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim2, new Float32());

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(null, rho, delta, eta);
        });
    }

    @Test
    public void createClass_DuplicateImage_ThrowsCannotFormPolarizationImageSet() {
        long[] dim = { 1, 1, 1 };

        Image<Float32> rho = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim, new Float32());

        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(null, rho, rho, delta);
        });
        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(null, rho, delta, rho);
        });
        assertThrows(CannotFormOrientationImage.class, () -> {
            new OrientationImage(null, rho, delta, delta);
        });
    }

    @Test
    public void getPolarizationImage_ImgLib2PolarizationImage_ReturnsCorrectImage() throws CannotFormOrientationImage {
        long[] dim = { 2, 2 };

        Image<Float32> rho = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> eta = new ImgLib2ImageFactory().create(dim, new Float32());

        OrientationImage orientationImage = new OrientationImage(null, rho, delta, eta);

        assertTrue(orientationImage.getAngleImage(OrientationAngle.rho).getImage() == rho
                && orientationImage.getAngleImage(OrientationAngle.delta).getImage() == delta
                && orientationImage.getAngleImage(OrientationAngle.eta).getImage() == eta);
    }

    @Test
    public void getOrientationVectorIterator_RandomDataSet_ReturnsCorrectOrientationVectorForEachElement()
            throws CannotFormOrientationImage {
        long[] dim = { 10, 10, 10, 10, 3 };

        Image<Float32> baseSet = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> rho = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> delta = new ImgLib2ImageFactory().create(dim, new Float32());
        Image<Float32> eta = new ImgLib2ImageFactory().create(dim, new Float32());

        IPixelCursor<Float32> baseSetCursor = baseSet.getCursor();
        IPixelCursor<Float32> rhoCursor = rho.getCursor();
        IPixelCursor<Float32> deltaCursor = delta.getCursor();
        IPixelCursor<Float32> etaCursor = eta.getCursor();

        Random random = new Random();
        Float32 one = new Float32((float) (Math.PI / 180));
        while (baseSetCursor.hasNext()) {
            Pixel<Float32> pixel = new Pixel<Float32>(new Float32((float) (random.nextFloat() * Math.PI / 4)));

            baseSetCursor.next();
            baseSetCursor.setPixel(pixel);

            rhoCursor.next();
            rhoCursor.setPixel(pixel);

            pixel.value().add(one);
            deltaCursor.next();
            deltaCursor.setPixel(pixel);

            pixel.value().add(one);
            etaCursor.next();
            etaCursor.setPixel(pixel);
        }

        baseSetCursor.reset();
        OrientationImage orientationImage = new OrientationImage(null, rho, delta, eta);

        IOrientationVectorIterator iterator = orientationImage.getOrientationVectorIterator();

        if (!iterator.hasNext()) {
            assertTrue(false);
        }

        boolean equals = true;
        while (iterator.hasNext()) {
            IOrientationVector orientationVector = iterator.next();
            Float32 angle = baseSetCursor.next().value();

            equals &= _checkPrecision(angle.get(), orientationVector.getAngle(OrientationAngle.rho), 1e-6f);
            equals &= _checkPrecision(angle.get(),
                    orientationVector.getAngle(OrientationAngle.delta) - (float) Math.PI / 180, 1e-6f);
            equals &= _checkPrecision(angle.get(),
                    orientationVector.getAngle(OrientationAngle.eta) - (float) Math.PI / 90, 1e-6f);
        }

        assertTrue(equals);
    }

    @Test
    public void create_FromPolarizationImage_HasCorrectDimensionForAllImages() throws CannotFormPolarizationImageSet {
        ImageFactory factory = new ImgLib2ImageFactory();

        long[] dim = {2, 2, 2, 2};
        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());
        
        IPolarizationImageSet polImage = new PolarizationImageSet(null, pol0, pol45, pol90, pol135);
        
        IOrientationImage orientationImage = new OrientationImage(null, factory, polImage);
        assertTrue(
            Arrays.equals(
                orientationImage.getAngleImage(OrientationAngle.rho).getImage().getDimensions(), dim) &&
            Arrays.equals(
                orientationImage.getAngleImage(OrientationAngle.delta).getImage().getDimensions(), dim) &&
            Arrays.equals(
                orientationImage.getAngleImage(OrientationAngle.eta).getImage().getDimensions(), dim));
            
    }

    private static boolean _checkPrecision(float val1, float val2, float error) {
        return Math.abs(val1 - val2) < error;        
    }

}