package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.algorithm.fourPolar.FourPolarMapper;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IntensityToOrientationConverter;
import fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation.MatrixBasedInverseOpticalPropagationCalculator;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageWriterFactory;

/**
 * Using this class, Sophie (AKA Boss) can construct an orientation image from a
 * polarization image. Note that polarization images should have already been
 * registered.
 * 
 * With this choice, the path to each polarization file is directly provided.
 */
class ChoiceI {
    public static void main(String[] args)
            throws NoReaderFoundForImage, IOException, CannotFormPolarizationImageSet, PropagationFactorNotFound,
            OpticalPropagationNotInvertible, IteratorMissMatch, ImpossibleOrientationVector, NoWriterFoundForImage {
        File pol0File = new File("");
        File pol45File = new File("");
        File pol90File = new File("");
        File pol135File = new File("");

        ImageFactory imgFactory = new ImgLib2ImageFactory();

        ImageReader<UINT16> reader = TiffImageReaderFactory.getReader(imgFactory, new UINT16());
        Image<UINT16> pol0Image = reader.read(pol0File);
        Image<UINT16> pol45Image = reader.read(pol45File);
        Image<UINT16> pol90Image = reader.read(pol90File);
        Image<UINT16> pol135Image = reader.read(pol135File);

        IPolarizationImageSet polarizationImageSet = new PolarizationImageSet(null, pol0Image, pol45Image, pol90Image,
                pol135Image);
        IOrientationImage orientationImage = new OrientationImage(null, imgFactory, polarizationImageSet);

        IInverseOpticalPropagation inverseOpticalProp = _getInverseOpticalPropagation();
        IntensityToOrientationConverter converter = new IntensityToOrientationConverter(inverseOpticalProp);

        FourPolarMapper mapper = new FourPolarMapper(converter);
        mapper.map(polarizationImageSet.getIterator(), orientationImage.getOrientationVectorIterator());

        File rhoFile = new File("/rho.tif");
        File deltaFile = new File("/delta.tif");
        File etaFile = new File("/eta.tif");
        
        ImageWriter<Float32> writer = TiffImageWriterFactory.getWriter(
            orientationImage.getAngleImage(OrientationAngle.rho).getImage(), new Float32());
        
        writer.write(rhoFile, orientationImage.getAngleImage(OrientationAngle.rho).getImage());
        writer.write(deltaFile, orientationImage.getAngleImage(OrientationAngle.delta).getImage());
        writer.write(etaFile, orientationImage.getAngleImage(OrientationAngle.eta).getImage());

    }

    private static IInverseOpticalPropagation _getInverseOpticalPropagation()
            throws PropagationFactorNotFound, OpticalPropagationNotInvertible {
        IOpticalPropagation opticalPropagation = new OpticalPropagation(null, null);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 1.7262);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.0120);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0.3482);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);
                
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.0120);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 1.726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.348);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 1.559);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 2.970);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 1.636);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 1.559);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -2.970);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator =
            new MatrixBasedInverseOpticalPropagationCalculator();
        
        return inverseCalculator.getInverse(opticalPropagation);
    }
    
}