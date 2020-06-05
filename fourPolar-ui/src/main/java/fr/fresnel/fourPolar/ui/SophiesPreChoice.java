package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.ChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile.PercentileChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorOneCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased.DescriptorBasedRegistration;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.BeadCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.SampleCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.soi.SoICalculator;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.BeadImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.preprocess.PreprocessResult;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.checker.TiffCapturedImageChecker;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;
import fr.fresnel.fourPolar.io.image.polarization.TiffPolarizationImageSetWriter;
import fr.fresnel.fourPolar.io.image.soi.TiffSoIImageWriter;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff.TiffPolarizationImageSetCompositesWriter;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.Preprocessor;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.SampleImageSetPreprocessor;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffReader;
import javassist.tools.reflect.CannotCreateException;

import net.imglib2.RealPoint;

/**
 * With this pre-choic, Sophie (AKA boss) can preprocess a sample image. For
 * this end, she has to provide a bead image (which can also be the same sample
 * image as well).
 * 
 * 
 * To use this code, Sophie only needs to fill the static parameters in
 * SophiesPreChoice up to the point where she sees the message that says don't
 * change anything from here.
 */
public class SophiesPreChoice {
    // RootFolder
    public static String rootFolder = "/home/masoud/Documents/SampleImages/A4PolarDataSet";

    // Bead image
    public static String beadImage = "Sample_OneCamera.tif";

    // Sample image
    public static String sampleImage = "Sample_OneCamera.tif";

    // Number of channels
    public static int[] channels = { 1 };

    // Wavelength (in meter)
    public static double[] wavelengths = { 1e-9 };

    // Axis order of the image.
    public static AxisOrder sampleImageAxisOrder = AxisOrder.XY;

    // Number of cameras
    public static Cameras camera = Cameras.One;

    // Position of each polarization inside the image.
    private static Position positionPol0 = Position.TopLeft;
    private static Position positionPol45 = Position.TopRight;
    private static Position positionPol90 = Position.BottomLeft;
    private static Position positionPol135 = Position.BottomRight;

    // Registration result composite image colors.
    private static Color baseImageColor = Color.Green;
    private static Color toRegisterImageColor = Color.Red;

    public static void main(String[] args) throws CannotCreateException, IncompatibleCapturedImage {
        beadImageSet = createBeadImageSet();
        sampleImageSet = createSampleImageSet();

        _showBeadImageForFoVCalculation();

    }

    // -------------------------------------------------------------------
    // YOU DON'T NEED TO CHANGE ANYTHING FROM HERE ON!
    // -------------------------------------------------------------------
    // FoV
    private static IFieldOfView fov;

    // Imaging setup
    private static IFourPolarImagingSetup setup = initializeImagingSetup();

    // Bead image set
    private static BeadImageSet beadImageSet = null;

    // Sample image set.
    private static SampleImageSet sampleImageSet = null;

    // Polarization constellation for one camera case
    private static OneCameraPolarizationConstellation polConstellation = createPolarizationConstellation();

    private static IFourPolarImagingSetup initializeImagingSetup() {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(camera);

        for (int i = 0; i < channels.length; i++) {
            setup.setChannel(channels[i], new Channel(wavelengths[i], 1, 1, 1, 1));
        }

        return setup;
    }

    public static BeadImageSet createBeadImageSet() throws CannotCreateException, IncompatibleCapturedImage {
        BeadImageSet beadImageSet = new BeadImageSet(new File(rootFolder));

        File beadImagePath = new File(rootFolder, beadImage);
        ICapturedImageFileSet beadCapturedImageFileSet = createFileSet(beadImagePath);

        beadImageSet.addImageSet(beadCapturedImageFileSet);

        return beadImageSet;
    }

    public static SampleImageSet createSampleImageSet() throws CannotCreateException, IncompatibleCapturedImage {
        SampleImageSet sampleImageSet = new SampleImageSet(new File(rootFolder));

        File sampleImagePath = new File(rootFolder, sampleImage);
        ICapturedImageFileSet sampleCapturedImageFileSet = createFileSet(sampleImagePath);

        sampleImageSet.addImageSet(sampleCapturedImageFileSet);

        return sampleImageSet;
    }

    public static ICapturedImageFileSet createFileSet(File pol0_45_90_135)
            throws CannotCreateException, IncompatibleCapturedImage {
        IMetadataReader metadataReader = new SCIFIOMetadataReader();
        TiffCapturedImageChecker checker = new TiffCapturedImageChecker(metadataReader);
        CapturedImageFileSetBuilder builder = new CapturedImageFileSetBuilder(setup, checker);

        builder.add(channels, pol0_45_90_135);

        return builder.build();
    }

    private static OneCameraPolarizationConstellation createPolarizationConstellation() {
        return new OneCameraPolarizationConstellation(positionPol0, positionPol45, positionPol90, positionPol135);
    }

    private static void _showBeadImageForFoVCalculation() {
        File beadImagePath = beadImageSet.getIterator().next().getFile(Cameras.getLabels(camera)[0])[0].file();

        try {
            Image<UINT16> beadImageGray = new SCIFIOUINT16TiffReader(new ImgLib2ImageFactory()).read(beadImagePath);
            Image<RGB16> beadImageColor = GrayScaleToColorConverter.colorUsingMaxEachPlane(beadImageGray);
            Bdv bdv = BdvFunctions.show(ImageToImgLib2Converter.getImg(beadImageColor, RGB16.zero()), "SoI",
                    BdvOptions.options().is2D());

            Behaviours behaviours = new Behaviours(new InputTriggerConfig());
            behaviours.install(bdv.getBdvHandle().getTriggerbindings(), "my-new-behaviours");

            CalculateFoVAndContinue doubleClick = new CalculateFoVAndContinue(bdv);
            behaviours.behaviour(doubleClick, "print global pos", "button1");
        } catch (IOException | MetadataParseError | ConverterToImgLib2NotFound e) {
        }
    }

    public static void createFieldOfView(IPointShape intersectionPoint) {
        File beadImage = beadImageSet.getIterator().next().getFile(Cameras.getLabels(camera)[0])[0].file();

        IMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata metadata = null;
        try {
            metadata = metadataReader.read(beadImage);
        } catch (IOException | MetadataParseError e) {
            // Caught before.
        }

        FoVCalculatorOneCamera fovCalculator = new FoVCalculatorOneCamera(metadata, intersectionPoint,
                polConstellation);

        fov = fovCalculator.calculate();

    }

    public static Preprocessor createBeadSetProcessor() {
        Preprocessor preprocessor = null;
        try {
            preprocessor = new Preprocessor(beadImageSet, channels.length);

            ImageFactory factory = new ImgLib2ImageFactory();
            preprocessor.setCapturedImageSetReader(new TiffCapturedImageSetReader(factory));
            preprocessor.setDarkBackgroundCalculator(new PercentileChannelDarkBackgroundEstimator(camera));
            preprocessor.setRegistrationCompositeColor(baseImageColor, toRegisterImageColor);
            preprocessor.setRegistrator(new DescriptorBasedRegistration());
            preprocessor.setSegmenter(new BeadCapturedImageSetSegmenter(fov, camera, channels.length));
            preprocessor.setRegistratonCompositeWriter(new TiffPolarizationImageSetCompositesWriter(factory));

        } catch (NoReaderFoundForImage | CannotCreateException | NoWriterFoundForImage e) {
            // Never caught
        }
        return preprocessor;
    }

    public static SampleImageSetPreprocessor createSampleImageSetPreprocessor(PreprocessResult result) {
        SampleImageSetPreprocessor preprocessor = new SampleImageSetPreprocessor(sampleImageSet, channels.length);

        try {
            ImageFactory factory = new ImgLib2ImageFactory();
            preprocessor.setCapturedImageSetLoader(new TiffCapturedImageSetReader(factory));
            preprocessor.setSegmenter(new SampleCapturedImageSetSegmenter(fov, camera, channels.length));
            _setChannelDarkBackgroundRemovers(preprocessor, result);
            _setChannelRealigners(preprocessor, result);
            preprocessor.setPolarizationImageSetWriter(new TiffPolarizationImageSetWriter(factory));
            preprocessor.setSoICalculator(new SoICalculator());
            preprocessor.setSoIImageWriter(new TiffSoIImageWriter(factory));

        } catch (NoReaderFoundForImage | NoWriterFoundForImage e) {
        }

        return preprocessor;
    }

    private static void _setChannelDarkBackgroundRemovers(SampleImageSetPreprocessor preprocessor,
            PreprocessResult result) {
        for (int channel = 0; channel < channels.length; channel++) {
            IChannelDarkBackgroundRemover remover = ChannelDarkBackgroundRemover
                    .create(result.getDarkBackground(channels[channel]));
            preprocessor.setChannelDarkBackgroundRemover(channels[channel], remover);
        }

    }

    private static void _setChannelRealigners(SampleImageSetPreprocessor preprocessor, PreprocessResult result) {
        for (int channel = 0; channel < channels.length; channel++) {
            IChannelRealigner realigner = ChannelRealigner.create(result.getRegistrationResult(channels[channel]));
            preprocessor.setChannelRealigner(channels[channel], realigner);
        }

    }

}

class CalculateFoVAndContinue implements ClickBehaviour {
    Bdv bdv;

    public CalculateFoVAndContinue(Bdv bdv) {
        this.bdv = bdv;
    }

    @Override
    public void click(int x, int y) {
        final RealPoint pos = new RealPoint(5);
        bdv.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);
        bdv.close();

        double[] pos1 = new double[5];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t)
                .limit(SophiesPreChoice.sampleImageAxisOrder.numAxis).toArray();

        IShape shape = new ShapeFactory().point(pos2, SophiesPreChoice.sampleImageAxisOrder);

        // Create FoV here
        SophiesPreChoice.createFieldOfView((IPointShape) shape);

        // Call preprocessor here
        Preprocessor preprocessor = SophiesPreChoice.createBeadSetProcessor();
        PreprocessResult result = null;
        try {
            result = preprocessor.process();
        } catch (IOException e) {
        }
        // Call processor here.
        SampleImageSetPreprocessor processor = SophiesPreChoice.createSampleImageSetPreprocessor(result);
        try {
            processor.process();
        } catch (IOException e) {
        }

        // Call display here.
        showImages();
        bdv.close();

    }

    private static void showPolarizationAndSoIImages(IOrientationImage orientationImage, SoIImage soiImage) {
        try {
            for (OrientationAngle angle : OrientationAngle.values()) {
                ImageJFunctions.show(ImageToImgLib2Converter.getImg(
                        orientationImage.getAngleImage(OrientationAngle.rho).getImage(), Float32.zero()), angle.name());

            }
            ImageJFunctions.show(ImageToImgLib2Converter.getImg(soiImage.getImage(), UINT16.zero()), "SoIImage");

        } catch (ConverterToImgLib2NotFound e) {

        }
    }


}