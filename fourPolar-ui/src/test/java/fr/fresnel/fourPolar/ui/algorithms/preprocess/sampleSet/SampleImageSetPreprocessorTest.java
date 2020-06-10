package fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.ChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorFourCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorOneCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorTwoCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.ChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.SampleCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;
import javassist.tools.reflect.CannotCreateException;

// TODO Implement a capturedImageRead interface, and directly return images (without loading).
// So that testing becomes dependent on images.
public class SampleImageSetPreprocessorTest {
    /**
     * We expect that no exception. Because realignment and segmentation throw no
     * error. Also background noise removal throws no error, because even though
     * image is empty and noise is positive, the pixels are set to zero again.
     */
    @Test
    public void getPolarizationImageSet_OneCameraOneChannelAllZeroImage_ProcessorThrowsNoException() {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVOneCamera(3, 3, 1, 1);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, camera, 1);

        SSPDummyDarkBackground background = new SSPDummyDarkBackground(1, 10, 10, 10, 10); // All must be same
        SSPDummyChannelRegistrationResult regResult = new SSPDummyChannelRegistrationResult(1, new Affine2D(),
                new Affine2D(), new Affine2D()); // Identity affines.
        RegistrationSetProcessResult result = new RegistrationSetProcessResult(channel1.length);
        result.setDarkBackground(1, background);
        result.setRegistrationResult(1, regResult);

        ISampleImageSetPreprocessorBuilder builder = this._createDefaultBuilder(result, imagingSetup);

        File sampleImage = new File(_root, "OneCamera/AllZeroImage.tif");
        ICapturedImageFile[] pol0 = { new SSPDummyCapturedImageFile(channel1, sampleImage) };
        SSPDummyCapturedImageFileSet fileSet = new SSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setCameras(camera);

        SampleImageSetPreprocessor processor = new SampleImageSetPreprocessor(builder);

        assertDoesNotThrow(() -> {
            processor.setCapturedImageSet(fileSet);
            processor.getPolarizationImageSet(1);
        });

        _allPolarizationHaveDimension(processor.getPolarizationImageSet(1), new long[] { 2, 2, 1, 1, 1 });
        assertTrue(this._allPixelsOfAllPolarizationsAreEqualToVal(processor.getPolarizationImageSet(1), 0));
    }

    /**
     * We expect that no exception. Because realignment and segmentation throw no
     * error. Also background noise removal throws no error, because even though
     * image is empty and noise is positive, the pixels are set to zero again.
     */
    @Test
    public void getPolarizationImageSet_TwoCameraOneChannelAllZeroImage_ProcessorThrowsNoException() {
        Cameras camera = Cameras.Two;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVTwoCamera(3, 3, 1, 1);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, camera, 1);

        SSPDummyDarkBackground background = new SSPDummyDarkBackground(1, 10, 10, 20, 20); // Each cam muse be same
        SSPDummyChannelRegistrationResult regResult = new SSPDummyChannelRegistrationResult(1, new Affine2D(),
                new Affine2D(), new Affine2D()); // Identity affines.
        RegistrationSetProcessResult result = new RegistrationSetProcessResult(channel1.length);
        result.setDarkBackground(1, background);
        result.setRegistrationResult(1, regResult);

        ISampleImageSetPreprocessorBuilder builder = this._createDefaultBuilder(result, imagingSetup);

        File registrationImage_pol0_90 = new File(_root, "TwoCamera/AllZeroImages/Pol0_90.tif");
        File registrationImage_pol45_135 = new File(_root, "TwoCamera/AllZeroImages/Pol45_135.tif");
        ICapturedImageFile[] pol0_90 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol0_90) };
        ICapturedImageFile[] pol45_135 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol45_135) };
        SSPDummyCapturedImageFileSet fileSet = new SSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45_135);
        fileSet.setCameras(camera);

        SampleImageSetPreprocessor processor = new SampleImageSetPreprocessor(builder);

        assertDoesNotThrow(() -> {
            processor.setCapturedImageSet(fileSet);
            processor.getPolarizationImageSet(1);
        });

        _allPolarizationHaveDimension(processor.getPolarizationImageSet(1), new long[] { 2, 2, 1, 1, 1 });
        assertTrue(this._allPixelsOfAllPolarizationsAreEqualToVal(processor.getPolarizationImageSet(1), 0));

    }

    /**
     * We expect that no exception. Because realignment and segmentation throw no
     * error. Also background noise removal throws no error, because even though
     * image is empty and noise is positive, the pixels are set to zero again.
     */
    @Test
    public void getPolarizationImageSet_FourCameraOneChannelAllZeroImage_ProcessorThrowsNoException() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVFourCamera(3, 3);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, camera, 1);

        SSPDummyDarkBackground background = new SSPDummyDarkBackground(1, 10, 10, 20, 20); // Each cam muse be same
        SSPDummyChannelRegistrationResult regResult = new SSPDummyChannelRegistrationResult(1, new Affine2D(),
                new Affine2D(), new Affine2D()); // Identity affines.
        RegistrationSetProcessResult result = new RegistrationSetProcessResult(channel1.length);
        result.setDarkBackground(1, background);
        result.setRegistrationResult(1, regResult);

        ISampleImageSetPreprocessorBuilder builder = this._createDefaultBuilder(result, imagingSetup);

        File registrationImage_pol0 = new File(_root, "FourCamera/AllZeroImages/Pol0.tif");
        File registrationImage_pol45 = new File(_root, "FourCamera/AllZeroImages/Pol45.tif");
        File registrationImage_pol90 = new File(_root, "FourCamera/AllZeroImages/Pol90.tif");
        File registrationImage_pol135 = new File(_root, "FourCamera/AllZeroImages/Pol135.tif");

        ICapturedImageFile[] pol0 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol0) };
        ICapturedImageFile[] pol45 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol45) };
        ICapturedImageFile[] pol90 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol90) };
        ICapturedImageFile[] pol135 = { new SSPDummyCapturedImageFile(channel1, registrationImage_pol135) };

        SSPDummyCapturedImageFileSet fileSet = new SSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        SampleImageSetPreprocessor processor = new SampleImageSetPreprocessor(builder);

        assertDoesNotThrow(() -> {
            processor.setCapturedImageSet(fileSet);
            processor.getPolarizationImageSet(1);
        });

        _allPolarizationHaveDimension(processor.getPolarizationImageSet(1), new long[] { 3, 3, 1, 1, 1 });
        assertTrue(this._allPixelsOfAllPolarizationsAreEqualToVal(processor.getPolarizationImageSet(1), 0));
    }

    private static String _root = SampleImageSetPreprocessorTest.class.getResource("").getPath();

    private boolean _allPixelsOfAllPolarizationsAreEqualToVal(IPolarizationImageSet imageSet, int value) {
        boolean isSetToValue = true;
        for (Polarization polarization : Polarization.values()) {
            for (IPixelCursor<UINT16> cursor = imageSet.getPolarizationImage(polarization).getImage()
                    .getCursor(); cursor.hasNext() && isSetToValue;) {
                isSetToValue = cursor.next().value().get() == value;
            }
        }

        return isSetToValue;
    }

    private void _allPolarizationHaveDimension(IPolarizationImageSet imageSet, long[] dim) {
        for (Polarization polarization : Polarization.values()) {
            assertArrayEquals(dim, imageSet.getPolarizationImage(polarization).getImage().getMetadata().getDim());
        }
    }

    private ISampleImageSetPreprocessorBuilder _createDefaultBuilder(RegistrationSetProcessResult result,
            IFourPolarImagingSetup setup) {

        TiffCapturedImageSetReader registrationImageReader = new TiffCapturedImageSetReader(new ImgLib2ImageFactory());
        return new SSPDummyBuilder(result, registrationImageReader, setup);
    }

    private IFourPolarImagingSetup createFPSetup(IFieldOfView fov, Cameras cameras, int numChannels) {
        SSPDummyFPSetup imagingSetup = new SSPDummyFPSetup();
        imagingSetup.setFieldOfView(fov);
        for (int channel = 1; channel <= numChannels; channel++) {
            imagingSetup.setChannel(1, null);
        }
        imagingSetup.setCameras(cameras);

        return imagingSetup;
    }

    private IFieldOfView createFoVOneCamera(long image_x, long image_y, long x_intersecion, long y_intersection) {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { image_x, image_y }).build();

        return new FoVCalculatorOneCamera(metadata,
                new ShapeFactory().point(new long[] { x_intersecion, y_intersection }, AxisOrder.XY),
                new OneCameraPolarizationConstellation(OneCameraPolarizationConstellation.Position.TopLeft,
                        OneCameraPolarizationConstellation.Position.TopRight,
                        OneCameraPolarizationConstellation.Position.BottomLeft,
                        OneCameraPolarizationConstellation.Position.BottomRight)).calculate();
    }

    /**
     * Assumes two cameras have image of same size and intersection is at same
     * point.
     */
    private IFieldOfView createFoVTwoCamera(long image_x, long image_y, long x_intersecion, long y_intersection) {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { image_x, image_y }).build();

        return new FoVCalculatorTwoCamera(metadata,
                new ShapeFactory().point(new long[] { x_intersecion, y_intersection }, AxisOrder.XY), metadata,
                new ShapeFactory().point(new long[] { x_intersecion, y_intersection }, AxisOrder.XY),
                new TwoCameraPolarizationConstellation(TwoCameraPolarizationConstellation.Position.Left,
                        TwoCameraPolarizationConstellation.Position.Left,
                        TwoCameraPolarizationConstellation.Position.Right,
                        TwoCameraPolarizationConstellation.Position.Right)).calculate();
    }

    /**
     * Assumes four cameras have image of same size.
     */
    private IFieldOfView createFoVFourCamera(long image_x, long image_y) {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { image_x, image_y }).build();

        return new FoVCalculatorFourCamera(metadata, metadata, metadata, metadata).calculate();
    }

}

class SSPDummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private ArrayList<IChannel> channels = new ArrayList<>();
    private IFieldOfView fov;

    @Override
    public Cameras getCameras() {
        return cameras;
    }

    @Override
    public void setCameras(Cameras cameras) throws IllegalArgumentException {
        this.cameras = cameras;
    }

    @Override
    public IChannel getChannel(int channel) throws IllegalArgumentException {
        return channels.get(channel);
    }

    @Override
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException {
        channels.add(propagationChannel);
    }

    @Override
    public int getNumChannel() {
        return this.channels.size();
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return null;
    }

    @Override
    public void setNumericalAperture(INumericalAperture na) {

    }

    @Override
    public IFieldOfView getFieldOfView() {
        return fov;
    }

    @Override
    public void setFieldOfView(IFieldOfView fov) throws IllegalArgumentException {
        this.fov = fov;
    }

}

class SSPDummyBuilder extends ISampleImageSetPreprocessorBuilder {
    RegistrationSetProcessResult result;
    ICapturedImageSetReader reader;
    IFourPolarImagingSetup setup;

    @Override
    IChannelDarkBackgroundRemover getBackgroundRemovers(int channel) {
        return ChannelDarkBackgroundRemover.create(result.getDarkBackground(channel));
    }

    @Override
    ICapturedImageSetReader getCapturedImageSetReader() {
        return reader;
    }

    @Override
    int getNumChannels() {
        return setup.getNumChannel();
    }

    @Override
    IChannelRealigner getRealigners(int channel) {
        try {
            return ChannelRealigner.create(result.getRegistrationResult(channel));
        } catch (CannotCreateException e) {
        }
        return null;
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        return new SampleCapturedImageSetSegmenter(setup.getFieldOfView(), setup.getCameras(), setup.getNumChannel());
    }

    public SSPDummyBuilder(RegistrationSetProcessResult result, ICapturedImageSetReader reader,
            IFourPolarImagingSetup setup) {
        this.result = result;
        this.reader = reader;
        this.setup = setup;
    }

}

class SSPDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;

    public void setFileSet(String label, ICapturedImageFile[] file) {
        this.files.put(label, file);
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return this.files.get(label);
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return this._cameras;
    }

    @Override
    public boolean hasLabel(String label) {
        return true;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.files.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();
    }

    @Override
    public int[] getChannels() {
        IntStream channels = IntStream.empty();
        for (ICapturedImageFile capturedImageFile : files.get(Cameras.getLabels(_cameras)[0])) {
            channels = IntStream.concat(channels, Arrays.stream(capturedImageFile.channels()));
        }

        return channels.toArray();
    }

}

class SSPDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public SSPDummyCapturedImageFile(int[] channels, File file) {
        _channels = channels;
        _file = file;
    }

    @Override
    public int[] channels() {
        return this._channels;
    }

    @Override
    public File file() {
        return this._file;
    }

}

class SSPDummyDarkBackground implements IChannelDarkBackground {
    private int channel;
    private HashMap<Polarization, Double> background = new HashMap<>();

    public SSPDummyDarkBackground(int channel, double pol0, double pol45, double pol90, double pol135) {
        this.channel = channel;
        background.put(Polarization.pol0, pol0);
        background.put(Polarization.pol45, pol45);
        background.put(Polarization.pol90, pol90);
        background.put(Polarization.pol135, pol135);
    }

    @Override
    public double getBackgroundLevel(Polarization polarization) {
        return background.get(polarization);
    }

    @Override
    public int channel() {
        return channel;
    }

}

class SSPDummyChannelRegistrationResult implements IChannelRegistrationResult {
    int channel;
    HashMap<RegistrationRule, Affine2D> affines = new HashMap<>();

    public SSPDummyChannelRegistrationResult(int channel, Affine2D... affine) {
        this.channel = channel;
        for (int i = 0; i < affine.length; i++) {
            affines.put(RegistrationRule.values()[i], affine[i]);
        }
    }

    @Override
    public boolean registrationSuccessful(RegistrationRule rule) {
        return true;
    }

    @Override
    public Optional<Affine2D> getAffineTransform(RegistrationRule rule) {
        return Optional.of(affines.get(rule));
    }

    @Override
    public double error(RegistrationRule rule) {
        return 0;
    }

    @Override
    public Optional<String> getFailureDescription(RegistrationRule rule) {
        return null;
    }

    @Override
    public int channel() {
        return 0;
    }

}