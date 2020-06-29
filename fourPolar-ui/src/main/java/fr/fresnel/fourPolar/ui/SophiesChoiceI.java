package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.IteratorMissMatch;
import fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.propagation.OpticalPropagationNotInvertible;
import fr.fresnel.fourPolar.algorithm.fourPolar.FourPolarMapper;
import fr.fresnel.fourPolar.algorithm.fourPolar.converters.IntensityToOrientationConverter;
import fr.fresnel.fourPolar.algorithm.fourPolar.inversePropagation.MatrixBasedInverseOpticalPropagationCalculator;
import fr.fresnel.fourPolar.core.exceptions.fourPolar.propagationdb.PropagationChannelNotInDatabase;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.exceptions.physics.propagation.PropagationFactorNotFound;
import fr.fresnel.fourPolar.core.fourPolar.propagationdb.IOpticalPropagationDB;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.physics.propagation.IInverseOpticalPropagation;
import fr.fresnel.fourPolar.core.physics.propagation.IOpticalPropagation;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetNotFound;
import fr.fresnel.fourPolar.io.fourPolar.propagationdb.XMLOpticalPropagationDBIO;
import fr.fresnel.fourPolar.io.image.orientation.IOrientationImageWriter;
import fr.fresnel.fourPolar.io.image.orientation.TiffOrientationImageWriter;
import fr.fresnel.fourPolar.io.image.polarization.IPolarizationImageSetReader;
import fr.fresnel.fourPolar.io.image.polarization.TiffPolarizationImageSetReader;
import fr.fresnel.fourPolar.io.image.soi.ISoIImageWriter;
import fr.fresnel.fourPolar.io.image.soi.TiffSoIImageWriter;
import fr.fresnel.fourPolar.io.imageSet.acquisition.AcquisitionSetFromTextFileReader;
import fr.fresnel.fourPolar.io.imagingSetup.FourPolarImagingSetupFromYaml;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.soi.ISoIImageCreator;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.soi.SoIImageCreator;
import javassist.tools.reflect.CannotCreateException;

/**
 * Given this choice, Sophie (AKA Boss) can construct an orientation image
 * together with the SoI image from a polarization image. To employ this choice,
 * Sophie has to run SophiesPreChoice first.
 * 
 * To use this snippet, boss just has to set the propagation factors below, and
 * then run the code.
 */
public class SophiesChoiceI {
    private static double soiThreshold = 1000;

    public static void main(String[] args)
            throws IOException, CannotCreateException, IncompatibleCapturedImage, PropagationChannelNotInDatabase {
        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        _readImagingSetup();

        SampleImageSet sampleImageSet = _readSampleImageSet();

        ISoIImageCreator soiImageCreator = SoIImageCreator.create(setup.getNumChannel());

        polarizationImageSetReader = new TiffPolarizationImageSetReader(new ImgLib2ImageFactory(),
                setup.getNumChannel());

        orientationImageWriter = new TiffOrientationImageWriter();
        soiImageWriter = new TiffSoIImageWriter();

        for (Iterator<ICapturedImageFileSet> capFilesItr = sampleImageSet.getIterator(); capFilesItr.hasNext();) {
            ICapturedImageFileSet fileSet = capFilesItr.next();
            for (int channel : IntStream.rangeClosed(1, setup.getNumChannel()).toArray()) {
                IPolarizationImageSet polarizationImageSet = readPolarizationImages(sampleImageSet.rootFolder(),
                        fileSet, channel);

                IOrientationImage orientationImage = createOrientationImage(polarizationImageSet);
                mapIntensityToOrientation(polarizationImageSet, orientationImage);
                saveOrientationImageInDegrees(orientationImage, sampleImageSet.rootFolder());

                ISoIImage soiImage = _createSoIImage(soiImageCreator, polarizationImageSet);
                saveSoIImage(soiImage, sampleImageSet.rootFolder());
            }
        }

        closeAllResources(polarizationImageSetReader, orientationImageWriter);
    }

    private static void _readImagingSetup() throws IOException {
        setup = FourPolarImagingSetup.instance();
        FourPolarImagingSetupFromYaml reader = new FourPolarImagingSetupFromYaml(rootFolder);
        reader.read(setup);
    }

    private static SampleImageSet _readSampleImageSet() throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        SampleImageSet sampleImageSet = new SampleImageSet(rootFolder);

        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup);
        reader.read(sampleImageSet);

        return sampleImageSet;
    }

    private static ISoIImage _createSoIImage(ISoIImageCreator soiImageCreator,
            IPolarizationImageSet polarizationImageSet) {
        ISoIImage soiImage = soiImageCreator.create(polarizationImageSet);
        return soiImage;
    }

    private static IFourPolarImagingSetup setup = null;
    private static IPolarizationImageSetReader polarizationImageSetReader = null;
    private static IOrientationImageWriter orientationImageWriter = null;
    private static ISoIImageWriter soiImageWriter = null;
    private static File rootFolder = new File(SophiesPreChoice.rootFolder);

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
            IOrientationImage orientationImage) throws PropagationChannelNotInDatabase, IOException {
        IInverseOpticalPropagation inverseOpticalProp = _getInverseOpticalPropagation(
            polarizationImageSet.channel());

        IntensityToOrientationConverter converter = new IntensityToOrientationConverter(inverseOpticalProp);

        FourPolarMapper mapper = new FourPolarMapper(converter);
        try {
            mapper.map(polarizationImageSet.getIterator(), orientationImage.getOrientationVectorIterator(),
                    soiThreshold);
        } catch (IteratorMissMatch e) {
        }

    }

    private static void saveOrientationImageInDegrees(IOrientationImage orientationImage, File rootFolder)
            throws IOException {
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

    private static IInverseOpticalPropagation _getInverseOpticalPropagation(int channel)
            throws PropagationChannelNotInDatabase, IOException {
        IOpticalPropagation opticalPropagation = _createOpticalPropagation(channel);

        MatrixBasedInverseOpticalPropagationCalculator inverseCalculator = new MatrixBasedInverseOpticalPropagationCalculator();

        try {
            return inverseCalculator.getInverse(opticalPropagation);
        } catch (PropagationFactorNotFound | OpticalPropagationNotInvertible e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static IOpticalPropagation _createOpticalPropagation(int channel)
            throws PropagationChannelNotInDatabase, IOException {
        return _readPropagationMatrixFromDatabase(channel);
    }

    public static IOpticalPropagation _readPropagationMatrixFromDatabase(int channel)
            throws PropagationChannelNotInDatabase, IOException {
        XMLOpticalPropagationDBIO dbIO = new XMLOpticalPropagationDBIO();
        IOpticalPropagationDB db = dbIO.read();

        return db.search(setup.getChannel(channel), setup.getNumericalAperture());

    }

}
