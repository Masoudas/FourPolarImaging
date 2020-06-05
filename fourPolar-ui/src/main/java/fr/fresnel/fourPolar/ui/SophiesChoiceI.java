package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.algorithm.fourPolar.FourPolarMapper;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IntensityToOrientationConverter;
import fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation.MatrixBasedInverseOpticalPropagationCalculator;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.OpticalPropagation;
import fr.fresnel.fourPolar.io.image.orientation.IOrientationImageWriter;
import fr.fresnel.fourPolar.io.image.orientation.TiffOrientationImageWriter;
import fr.fresnel.fourPolar.io.image.polarization.IPolarizationImageSetReader;
import fr.fresnel.fourPolar.io.image.polarization.TiffPolarizationImageSetReader;
import fr.fresnel.fourPolar.io.image.soi.ISoIImageWriter;
import fr.fresnel.fourPolar.io.image.soi.TiffSoIImageWriter;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.soi.ISoIImageCreator;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.soi.SoIImageCreator;
import javassist.tools.reflect.CannotCreateException;

/**
 * With this choice, Sophie (AKA Boss) can construct an orientation image
 * together with the SoI image from a polarization image. To employ this choice,
 * Sophie has to run SophiesPreChoice first.
 * 
 * To use this snippet, Sophie just have to set the propagation factors below,
 * and then run the code.
 */
public class SophiesChoiceI {
    /**
     * ATTENTION: These are PROPAGATION factors, and NOT inverse propagation
     * factors.
     */
    private static double _propFactor_xx_0 = 1.72622242;
    private static double _propFactor_yy_0 = 0.012080463;
    private static double _propFactor_zz_0 = 0.348276444;
    private static double _propFactor_xy_0 = 0.0;

    private static double _propFactor_xx_90 = 0.012080463;
    private static double _propFactor_yy_90 = 1.726222425;
    private static double _propFactor_zz_90 = 0.348276444;
    private static double _propFactor_xy_90 = 0.0;

    private static double _propFactor_xx_45 = 1.6369744726;
    private static double _propFactor_yy_45 = 1.6369744726;
    private static double _propFactor_zz_45 = 1.55973262275;
    private static double _propFactor_xy_45 = 2.97015445583;

    private static double _propFactor_xx_135 = 1.63697447260;
    private static double _propFactor_yy_135 = 1.63697447260;
    private static double _propFactor_zz_135 = 1.55973262275;
    private static double _propFactor_xy_135 = -2.9701544558;

    public static void main(String[] args) throws IOException, CannotCreateException, IncompatibleCapturedImage {
        SampleImageSet sampleImageSet = SophiesPreChoice.createSampleImageSet();
        int[] channels = SophiesPreChoice.channels;

        ISoIImageCreator soiImageCreator = SoIImageCreator.create(channels.length);

        polarizationImageSetReader = new TiffPolarizationImageSetReader(new ImgLib2ImageFactory(),
                SophiesPreChoice.channels.length);

        orientationImageWriter = new TiffOrientationImageWriter();
        soiImageWriter = new TiffSoIImageWriter();

        for (Iterator<ICapturedImageFileSet> capFilesItr = sampleImageSet.getIterator(); capFilesItr.hasNext();) {
            ICapturedImageFileSet fileSet = capFilesItr.next();
            for (int channel : channels) {
                IPolarizationImageSet polarizationImageSet = readPolarizationImages(sampleImageSet.rootFolder(),
                        fileSet, channel);

                IOrientationImage orientationImage = createOrientationImage(polarizationImageSet);
                mapIntensityToOrientation(polarizationImageSet, orientationImage);
                saveOrientationImage(orientationImage, sampleImageSet.rootFolder());

                ISoIImage soiImage = soiImageCreator.create(polarizationImageSet);
                soiImageWriter.write(sampleImageSet.rootFolder(), soiImage);
            }
        }

        closeAllResources(polarizationImageSetReader, orientationImageWriter);

    }

    private static IPolarizationImageSetReader polarizationImageSetReader = null;
    private static IOrientationImageWriter orientationImageWriter = null;
    private static ISoIImageWriter soiImageWriter = null;

    private static IPolarizationImageSet readPolarizationImages(File rootFolder, ICapturedImageFileSet fileSet,
            int channel) throws IOException {
        try {
            return polarizationImageSetReader.read(rootFolder, fileSet, channel);
        } catch (CannotFormPolarizationImageSet e) {

        }
        return null;
    }

    private static IOrientationImage createOrientationImage(IPolarizationImageSet polarizationImageSet) {
        return OrientationImageFactory.create(new ImgLib2ImageFactory(), polarizationImageSet);
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

    private static void saveOrientationImage(IOrientationImage orientationImage, File rootFolder) throws IOException {
        orientationImageWriter.writeInDegrees(rootFolder, orientationImage);

    }

    private static void closeAllResources(IPolarizationImageSetReader polarizationImageSetReader,
            IOrientationImageWriter orientationImageWriter) throws IOException {
        polarizationImageSetReader.close();
        orientationImageWriter.close();
    }

    private static void saveSoIImage(ISoIImage soiImage, File rootFolder) throws IOException {
        soiImageWriter.write(rootFolder, soiImage);

    }

    private static IInverseOpticalPropagation _getInverseOpticalPropagation() {
        IOpticalPropagation opticalPropagation = new OpticalPropagation(null, null);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol0, _propFactor_xx_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol0, _propFactor_yy_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol0, _propFactor_zz_0);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol0, _propFactor_xy_0);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol90, _propFactor_xx_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol90, _propFactor_yy_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol90, _propFactor_zz_90);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol90, _propFactor_xy_90);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol45, _propFactor_xx_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol45, _propFactor_yy_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol45, _propFactor_zz_45);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol45, _propFactor_xy_45);

        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XX, Polarization.pol135, _propFactor_xx_135);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.YY, Polarization.pol135, _propFactor_yy_135);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.ZZ, Polarization.pol135, _propFactor_zz_135);
        opticalPropagation.setPropagationFactor(DipoleSquaredComponent.XY, Polarization.pol135, _propFactor_xy_135);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        try {
            return inverseCalculator.getInverse(opticalPropagation);
        } catch (PropagationFactorNotFound | OpticalPropagationNotInvertible e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
