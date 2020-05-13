package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters.ImpossibleOrientationVector;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.algorithm.fourPolar.FourPolarMapper;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IntensityToOrientationConverter;
import fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation.MatrixBasedInverseOpticalPropagationCalculator;
import fr.fresnel.fourPolar.algorithm.preprocess.soi.SoICalculator;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
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
import net.imglib2.img.display.imagej.ImageJFunctions;

/**
 * With this choice, Sophie (AKA Boss) can construct an orientation image
 * together with the SoI image from a polarization image. Note that polarization
 * images should be registered before hand.
 * 
 * To use this code, the only needed piece of information is the path to images.
 */
public class SophiesChoiceI {
    public static void main(String[] args) throws IOException {
        // Define root folder of polarization image. NO BACKSLASHES!
        String rootFolder = "/home/masoud/Documents/SampleImages/A4PolarDataSet";

        // Add name of file for each polarization image here.
        File pol0File = new File(rootFolder, "AVG_Pol0.tif");
        File pol45File = new File(rootFolder, "AVG_Pol45.tif");
        File pol90File = new File(rootFolder, "AVG_Pol90.tif");
        File pol135File = new File(rootFolder, "AVG_Pol135.tif");

        ImageFactory imgFactory = new ImgLib2ImageFactory();
        Image<UINT16>[] polImages = readPolarizationImages(pol0File, pol45File, pol90File, pol135File, imgFactory);
        IPolarizationImageSet polarizationImageSet = formPolarizationImage(polImages);
        IOrientationImage orientationImage = new OrientationImage(null, imgFactory, polarizationImageSet);

        SoIImage soiImage = createSoIImage(imgFactory, polarizationImageSet);

        mapIntensityToOrientation(polarizationImageSet, orientationImage);

        saveOrientationImages(orientationImage, rootFolder);
        saveSoIImage(soiImage, rootFolder);

        showImages(orientationImage, soiImage);

    }

    private static void saveSoIImage(SoIImage soIImage, String rootFolder) throws IOException {
        ImageWriter<UINT16> writer;
        try {
            writer = TiffImageWriterFactory.getWriter(soIImage.getImage(), UINT16.zero());
            writer.write(new File(rootFolder, "soiImage.tif"), soIImage.getImage());
            writer.close();

        } catch (NoWriterFoundForImage e) {
        }

    }

    private static SoIImage createSoIImage(ImageFactory imgFactory, IPolarizationImageSet polarizationImageSet) {
        SoIImage soIImage = new SoIImage(polarizationImageSet, imgFactory);
        new SoICalculator().calculateUINT16Sum(polarizationImageSet.getIterator(), soIImage.getImage().getCursor());
        return soIImage;
    }

    private static void showImages(IOrientationImage orientationImage, SoIImage soiImage) {
        try {
            for (OrientationAngle angle : OrientationAngle.values()) {
                ImageJFunctions.show(ImageToImgLib2Converter.getImg(
                        orientationImage.getAngleImage(OrientationAngle.rho).getImage(), Float32.zero()), angle.name());

            }
            ImageJFunctions.show(ImageToImgLib2Converter.getImg(
                soiImage.getImage(), UINT16.zero()), "SoIImage");

        } catch (ConverterToImgLib2NotFound e) {

        }
    }

    private static Image<UINT16>[] readPolarizationImages(File pol0File, File pol45File, File pol90File,
            File pol135File, ImageFactory imgFactory) throws IOException {
        Image<UINT16>[] polImages = new Image[4];

        ImageReader<UINT16> reader;
        try {
            reader = TiffImageReaderFactory.getReader(imgFactory, UINT16.zero());
            polImages[0] = reader.read(pol0File);
            polImages[1] = reader.read(pol45File);
            polImages[2] = reader.read(pol90File);
            polImages[3] = reader.read(pol135File);
            reader.close();
        } catch (NoReaderFoundForImage e) {
        }
        return polImages;
    }

    private static IPolarizationImageSet formPolarizationImage(Image<UINT16>[] polImages) {
        try {
            return new PolarizationImageSet(null, polImages[0], polImages[1], polImages[2], polImages[3]);
        } catch (CannotFormPolarizationImageSet e) {
        }
        return null;

    }

    private static void mapIntensityToOrientation(IPolarizationImageSet polarizationImageSet,
            IOrientationImage orientationImage) {
        IInverseOpticalPropagation inverseOpticalProp = _getInverseOpticalPropagation();

        IntensityToOrientationConverter converter = new IntensityToOrientationConverter(inverseOpticalProp);

        FourPolarMapper mapper = new FourPolarMapper(converter);
        try {
            mapper.map(polarizationImageSet.getIterator(), orientationImage.getOrientationVectorIterator());
        } catch (IteratorMissMatch e) {
        }

    }

    private static void saveOrientationImages(IOrientationImage orientationImage, String rootFolder)
            throws IOException {
        ImageWriter<Float32> writer;
        try {
            writer = TiffImageWriterFactory.getWriter(orientationImage.getAngleImage(OrientationAngle.rho).getImage(),
                    Float32.zero());
            for (OrientationAngle angle : OrientationAngle.values()) {
                writer.write(new File(rootFolder, angle.name() + ".tif"),
                        orientationImage.getAngleImage(angle).getImage());
            }
            writer.close();

        } catch (NoWriterFoundForImage e) {
        }

    }

    private static IInverseOpticalPropagation _getInverseOpticalPropagation() {
        IOpticalPropagation opticalPropagation = new OpticalPropagation(null, null);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, 1.72622242);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, 0.012080463);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, 0.348276444);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, 0.012080463);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, 1.726222425);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, 0.348276444);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, 0.0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, 1.6369744726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, 1.6369744726);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, 1.55973262275);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, 2.97015445583);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, 1.63697447260);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, 1.63697447260);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, 1.55973262275);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, -2.9701544558);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        try {
            return inverseCalculator.getInverse(opticalPropagation);
        } catch (PropagationFactorNotFound | OpticalPropagationNotInvertible e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
