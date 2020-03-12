package fr.fresnel.fourPolar.algorithm.fourPolar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IntensityToOrientationConverter;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.polarization.IntensityVector;
import fr.fresnel.fourPolar.core.physics.propagation.InverseOpticalPropagation;

public class FourPolarMapperTest {
    /**
     * Define a number of pair of predefined intensity and orientation. Assign the
     * intensites, calculate orientation and compare the results.
     * 
     * @throws ImpossibleOrientationVector
     */
    @Test
    public void map_2By2PolarizationImage_MapsToCorrectOrientation()
            throws CannotFormPolarizationImageSet, IteratorMissMatch, ImpossibleOrientationVector {
        IntensityVector intensity1 = new IntensityVector(13910.52, 32224.54, 13910.52, 32224.54);
        OrientationVector vector1 = new OrientationVector((float) (0f / 180f * Math.PI), (float) (180f / 180f * Math.PI),
                (float) (0f / 180f * Math.PI));

        IntensityVector intensity2 = new IntensityVector(971, 3160, 971, 3160);
        OrientationVector vector2 = new OrientationVector((float) (0f / 180f * Math.PI), (float) (90f / 180f * Math.PI),
                (float) (0f / 180f * Math.PI));

        IntensityVector intensity3 = new IntensityVector(10372.49, 15983.54, 18017.8, 15983.54);
        OrientationVector vector3 = new OrientationVector((float) (0f / 180f * Math.PI), (float) (180f / 180f * Math.PI),
                (float) (45f / 180f * Math.PI));

        IntensityVector intensity4 = new IntensityVector(18035, 32069, 7689, 32069);
        OrientationVector vector4 = new OrientationVector((float) (0f / 180f * Math.PI), (float) (90f / 180f * Math.PI),
                (float) (45f / 180f * Math.PI));

        InverseOpticalPropagation inverseProp = new InverseOpticalPropagation(null);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XX, 0.7880897720);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.YY, 0.20470751910);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.ZZ, -1.0419630892);
        inverseProp.setInverseFactor(Polarization.pol0, DipoleSquaredComponent.XY, 0);

        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XX, 0.20470751910);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.YY, 0.78808977204);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.ZZ, -1.0419630892);
        inverseProp.setInverseFactor(Polarization.pol90, DipoleSquaredComponent.XY, 0.0);

        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XX, -0.1108420459);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.YY, -0.1108420459);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.ZZ, 0.55323020559);
        inverseProp.setInverseFactor(Polarization.pol45, DipoleSquaredComponent.XY, 0.16834141369);

        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XX, -0.11084204599);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.YY, -0.11084204599);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.ZZ, 0.553230205596);
        inverseProp.setInverseFactor(Polarization.pol135, DipoleSquaredComponent.XY, -0.16834141369);

        long[] dim = { 2, 2 };
        Image<UINT16> pol0 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol45 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol90 = new ImgLib2ImageFactory().create(dim, new UINT16());
        Image<UINT16> pol135 = new ImgLib2ImageFactory().create(dim, new UINT16());

        IPixelCursor<UINT16> pol0Cursor = pol0.getCursor();
        IPixelCursor<UINT16> pol45Cursor = pol45.getCursor();
        IPixelCursor<UINT16> pol90Cursor = pol90.getCursor();
        IPixelCursor<UINT16> pol135Cursor = pol135.getCursor();

        _setIntensity(pol0Cursor, pol45Cursor, pol90Cursor, pol135Cursor, intensity1);
        _setIntensity(pol0Cursor, pol45Cursor, pol90Cursor, pol135Cursor, intensity2);
        _setIntensity(pol0Cursor, pol45Cursor, pol90Cursor, pol135Cursor, intensity3);
        _setIntensity(pol0Cursor, pol45Cursor, pol90Cursor, pol135Cursor, intensity4);

        PolarizationImageSet polarizationImageSet = new PolarizationImageSet(
            null, pol0, pol45, pol90, pol135);
        OrientationImage orientationImage = new OrientationImage(null, new ImgLib2ImageFactory(), polarizationImageSet);
        
        IntensityToOrientationConverter converter = new IntensityToOrientationConverter(inverseProp);
        FourPolarMapper mapper = new FourPolarMapper(converter);

        mapper.map(polarizationImageSet.getIterator(), orientationImage.getOrientationVectorIterator());

        IOrientationVectorIterator iterator = orientationImage.getOrientationVectorIterator();

        if (!iterator.hasNext()){
            assertTrue(false, "Orientation image has no orientation vectors");
        }
        
        double err = 1e-1;
        assertTrue(
            _compareAngles(iterator.next(), vector1, err) &&
            _compareAngles(iterator.next(), vector2, err) &&
            _compareAngles(iterator.next(), vector3, err) &&
            _compareAngles(iterator.next(), vector4, err)
        );
                
    }

    private void _setIntensity(IPixelCursor<UINT16> pol0Cursor, IPixelCursor<UINT16> pol45Cursor,
            IPixelCursor<UINT16> pol90Cursor, IPixelCursor<UINT16> pol135Cursor, IntensityVector intensity) {
        pol0Cursor.next();
        pol0Cursor.setPixel(new Pixel<UINT16>(new UINT16((int)intensity.getIntensity(Polarization.pol0))));
        pol45Cursor.next();
        pol45Cursor.setPixel(new Pixel<UINT16>(new UINT16((int)intensity.getIntensity(Polarization.pol45))));
        pol90Cursor.next();
        pol90Cursor.setPixel(new Pixel<UINT16>(new UINT16((int)intensity.getIntensity(Polarization.pol90))));
        pol135Cursor.next();
        pol135Cursor.setPixel(new Pixel<UINT16>(new UINT16((int)intensity.getIntensity(Polarization.pol135))));
    }

    private boolean _compareAngles(IOrientationVector vec1, OrientationVector vec2, double err) {
        return Math.abs(vec1.getAngle(OrientationAngle.rho) - vec2.getAngle(OrientationAngle.rho)) < err &&
            Math.abs(vec1.getAngle(OrientationAngle.delta) - vec2.getAngle(OrientationAngle.delta)) < err &&
            Math.abs(vec1.getAngle(OrientationAngle.eta) - vec2.getAngle(OrientationAngle.eta)) < err;
    }

}