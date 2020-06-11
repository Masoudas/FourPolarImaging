package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorOneCamera;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.PolarizationImageSetCompositesCreator;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet.RegistrationImageType;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.physics.channel.Channel;
import fr.fresnel.fourPolar.core.physics.na.NumericalAperture;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.captured.tiff.checker.TiffCapturedImageChecker;
import fr.fresnel.fourPolar.io.image.generic.IMetadataReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;
import fr.fresnel.fourPolar.io.image.polarization.TiffPolarizationImageSetWriter;
import fr.fresnel.fourPolar.io.imagingSetup.FourPolarImagingSetupToYaml;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.tiff.TiffPolarizationImageSetCompositesWriter;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet.IRegistrationSetProcessor;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet.RegistrationSetProcessorBuilder;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet.ISampleImageSetPreprocessor;
import fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet.SampleImageSetPreprocessorBuilder;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationSetProcessFailure;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.sampleSet.SampleSetPreprocessFailure;
import javassist.tools.reflect.CannotCreateException;
import net.imglib2.RealPoint;
import net.imglib2.img.display.imagej.ImageJFunctions;

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
    // Introduce yourself
    public static String userName = "Sophie 'The Boss' Brasselet";

    // RootFolder
    public static String rootFolder = "/home/masoud/Documents/SampleImages/A4PolarDataSet";

    // Registration image
    public static String registrationImage = "Sample_OneCamera.tif";

    // Registration image type
    public static RegistrationImageType registrationImageType = RegistrationImageType.SAMPLE;

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

    public static void main(String[] args) throws CannotCreateException, IncompatibleCapturedImage, IOException {
        beadImageSet = createRegistrationImageSet();
        sampleImageSet = createSampleImageSet();

        _showSampleImageSetForFoVCalculation();

    }

    // -------------------------------------------------------------------
    // YOU DON'T NEED TO CHANGE ANYTHING FROM HERE ON!
    // -------------------------------------------------------------------
    // Imaging setup
    private static IFourPolarImagingSetup setup = initializeImagingSetup();

    // Bead image set
    private static RegistrationImageSet beadImageSet = null;

    // Sample image set.
    private static SampleImageSet sampleImageSet = null;

    // Polarization constellation for one camera case
    private static OneCameraPolarizationConstellation polConstellation = createPolarizationConstellation();

    private static IFourPolarImagingSetup initializeImagingSetup() {
        IFourPolarImagingSetup setup = FourPolarImagingSetup.instance();
        setup.setCameras(camera);

        for (int i = 0; i < channels.length; i++) {
            setup.setChannel(channels[i], new Channel(wavelengths[i], 0, 0, 0, 0));
        }

        setup.setNumericalAperture(new NumericalAperture(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        return setup;
    }

    public static void writeSetupToDisk() throws IOException {
        FourPolarImagingSetupToYaml writer = new FourPolarImagingSetupToYaml(setup, sampleImageSet.rootFolder());
        writer.write();
    }

    public static RegistrationImageSet createRegistrationImageSet()
            throws CannotCreateException, IncompatibleCapturedImage {
        RegistrationImageSet beadImageSet = new RegistrationImageSet(new File(rootFolder), registrationImageType);

        File beadImagePath = new File(rootFolder, registrationImage);
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

    private static void _showSampleImageSetForFoVCalculation() {
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
            e.printStackTrace();
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

        setup.setFieldOfView(fovCalculator.calculate());

    }

    public static IRegistrationSetProcessor createRegistrationSetProcessor() {
        return new RegistrationSetProcessorBuilder(setup)
                .registrationCompositeCreator(
                        new PolarizationImageSetCompositesCreator(channels.length, baseImageColor, toRegisterImageColor))
                .build();
    }

    public static ISampleImageSetPreprocessor createSampleImageSetPreprocessor(RegistrationSetProcessResult result) {
        SampleImageSetPreprocessorBuilder processorBuilder = new SampleImageSetPreprocessorBuilder(setup, result);
        return processorBuilder.build();
    }

}

class CalculateFoVAndContinue implements ClickBehaviour {
    Bdv bdv;

    public CalculateFoVAndContinue(Bdv bdv) {
        this.bdv = bdv;
    }

    @Override
    public void click(int x, int y) {
        createFoV(x, y);

        RegistrationImageSet registrationImageSet = this.createRegImageSet();

        // Call preprocessor here
        IRegistrationSetProcessor preprocessor = SophiesPreChoice.createRegistrationSetProcessor();
        RegistrationSetProcessResult result = this.preprocessRegistrationSet(registrationImageSet, preprocessor);

        TiffPolarizationImageSetCompositesWriter compositesWriter = new TiffPolarizationImageSetCompositesWriter();
        for (int channel = 1; channel <= SophiesPreChoice.channels.length; channel++) {
            IPolarizationImageSetComposites composites = preprocessor.getRegistrationComposite(channel).get();
            this.showRegistrationCompositeImage(composites);
            this.writeComposite(compositesWriter, composites, registrationImageSet.rootFolder());
        }

        // Call processor here.
        SampleImageSet sampleImageSet = this.createSampleSet();
        TiffPolarizationImageSetWriter writer = new TiffPolarizationImageSetWriter();
        ISampleImageSetPreprocessor processor = SophiesPreChoice.createSampleImageSetPreprocessor(result);
        for (Iterator<ICapturedImageFileSet> itr = sampleImageSet.getIterator(); itr.hasNext();) {
            try {
                processor.setCapturedImageSet(itr.next());
            } catch (SampleSetPreprocessFailure e) {
                e.printStackTrace();
            }

            for (int channel = 1; channel <= SophiesPreChoice.channels.length; channel++) {
                IPolarizationImageSet polSet = null;
                try {
                    polSet = processor.getPolarizationImageSet(channel);
                } catch (SampleSetPreprocessFailure e) {
                    e.printStackTrace();
                }
                writeSamplePolarizationImage(writer, sampleImageSet, polSet);
            }
        }

        // Call display here.
        bdv.close();

        try {
            SophiesPreChoice.writeSetupToDisk();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeSamplePolarizationImage(TiffPolarizationImageSetWriter writer, SampleImageSet sampleImageSet,
            IPolarizationImageSet polSet) {
        try {
            writer.write(sampleImageSet.rootFolder(), polSet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SampleImageSet createSampleSet() {
        try {
            return SophiesPreChoice.createSampleImageSet();
        } catch (CannotCreateException | IncompatibleCapturedImage e) {
            e.printStackTrace();
            return null;
        }
    }

    private void writeComposite(TiffPolarizationImageSetCompositesWriter compositesWriter,
            IPolarizationImageSetComposites composites, File rootFolder) {
        try {
            compositesWriter.writeAsRegistrationComposite(rootFolder, composites);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private RegistrationSetProcessResult preprocessRegistrationSet(RegistrationImageSet registrationImageSet,
            IRegistrationSetProcessor preprocessor) {
        try {
            return preprocessor.process(registrationImageSet);
        } catch (RegistrationSetProcessFailure e) {
            e.printStackTrace();
            return null;
        }
    }

    private RegistrationImageSet createRegImageSet() {
        try {
            return SophiesPreChoice.createRegistrationImageSet();
        } catch (CannotCreateException | IncompatibleCapturedImage e) {
            return null;
        }
    }

    private void createFoV(int x, int y) {
        long[] pos2 = getIntersectionPoint(x, y);

        IShape shape = new ShapeFactory().point(pos2, SophiesPreChoice.sampleImageAxisOrder);

        // Create FoV here
        SophiesPreChoice.createFieldOfView((IPointShape) shape);
    }

    private long[] getIntersectionPoint(int x, int y) {
        final RealPoint pos = new RealPoint(5);
        bdv.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);
        bdv.close();

        double[] pos1 = new double[5];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t).limit(2).toArray();
        return pos2;
    }

    private void showRegistrationCompositeImage(IPolarizationImageSetComposites composites) {
        try {
            for (RegistrationRule rule : RegistrationRule.values()) {
                ImageJFunctions.show(
                        ImageToImgLib2Converter.getImg(composites.getCompositeImage(rule).getImage(), RGB16.zero()),
                        rule.name() + " of channel " + composites.channel());

            }

        } catch (ConverterToImgLib2NotFound e) {

        }
    }

}