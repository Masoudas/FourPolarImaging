package fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile.PercentileChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorFourCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorOneCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorTwoCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased.DescriptorBasedRegistration;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.RegistrationCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.RegistrationCompositeFigureCreator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet.RegistrationImageType;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationSetProcessFailure;

// TODO Implement a capturedImageRead interface, and directly return images (without loading).
// So that testing becomes dependent on images. 
public class RegistrationSetProcessorTest {
    private static String _root = RegistrationSetProcessor.class.getResource("").getPath();

    @Test
    public void process_OneCameraAllZeroImage_ThrowsRegistrationSetProcessFailure()
            throws IOException, MetadataParseError {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVOneCamera(3, 3, 1, 1);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, Cameras.One, 1);
        IRegistrationSetProcessorBuilder builder = this._createDefaultBuilder(imagingSetup);

        File registrationImage = new File(_root, "OneCamera/AllZeroImage.tif");
        ICapturedImageFile[] pol0 = { new RSPDummyCapturedImageFile(channel1, registrationImage) };
        RSPDummyCapturedImageFileSet fileSet = new RSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setCameras(camera);

        RegistrationImageSet registrationImageSet = new RegistrationImageSet(new File(_root),
                RegistrationImageType.SAMPLE);
        registrationImageSet.addImageSet(fileSet);

        RegistrationSetProcessor processor = new RegistrationSetProcessor(builder);
        assertThrows(RegistrationSetProcessFailure.class, () -> {
            processor.process(registrationImageSet);
        });
    }

    @Test
    public void process_TwoCameraAllZeroImage_ThrowsRegistrationSetProcessFailure()
            throws IOException, MetadataParseError {
        Cameras camera = Cameras.Two;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVTwoCamera(3, 3, 1, 1);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, camera, 1);
        IRegistrationSetProcessorBuilder builder = this._createDefaultBuilder(imagingSetup);

        File registrationImage_pol0_90 = new File(_root, "TwoCamera/AllZeroImages/Pol0_90.tif");
        File registrationImage_pol45_135 = new File(_root, "TwoCamera/AllZeroImages/Pol45_135.tif");
        ICapturedImageFile[] pol0_90 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol0_90) };
        ICapturedImageFile[] pol45_135 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol45_135) };

        RSPDummyCapturedImageFileSet fileSet = new RSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45_135);
        fileSet.setCameras(camera);

        RegistrationImageSet registrationImageSet = new RegistrationImageSet(new File(_root),
                RegistrationImageType.SAMPLE);
        registrationImageSet.addImageSet(fileSet);

        RegistrationSetProcessor processor = new RegistrationSetProcessor(builder);

        assertThrows(RegistrationSetProcessFailure.class, () -> {
            processor.process(registrationImageSet);
        });
    }

    @Test
    public void process_FourCameraAllZeroImage_ThrowsRegistrationSetProcessFailure()
            throws IOException, MetadataParseError {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        IFieldOfView fov = this.createFoVFourCamera(3, 3);
        IFourPolarImagingSetup imagingSetup = createFPSetup(fov, camera, 1);
        IRegistrationSetProcessorBuilder builder = this._createDefaultBuilder(imagingSetup);

        File registrationImage_pol0 = new File(_root, "FourCamera/AllZeroImages/Pol0.tif");
        File registrationImage_pol45 = new File(_root, "FourCamera/AllZeroImages/Pol45.tif");
        File registrationImage_pol90 = new File(_root, "FourCamera/AllZeroImages/Pol90.tif");
        File registrationImage_pol135 = new File(_root, "FourCamera/AllZeroImages/Pol135.tif");

        ICapturedImageFile[] pol0 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol0) };
        ICapturedImageFile[] pol45 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol45) };
        ICapturedImageFile[] pol90 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol90) };
        ICapturedImageFile[] pol135 = { new RSPDummyCapturedImageFile(channel1, registrationImage_pol135) };

        RSPDummyCapturedImageFileSet fileSet = new RSPDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        RegistrationImageSet registrationImageSet = new RegistrationImageSet(new File(_root),
                RegistrationImageType.SAMPLE);
        registrationImageSet.addImageSet(fileSet);

        RegistrationSetProcessor processor = new RegistrationSetProcessor(builder);

        assertThrows(RegistrationSetProcessFailure.class, () -> {
            processor.process(registrationImageSet);
        });
    }

    // @Test
    // public void
    // process_TwoCameraProperImageSet_ThrowsRegistrationSetProcessFailure()
    // throws IOException, MetadataParseError {
    // Cameras camera = Cameras.Two;
    // int[] channel1 = { 1 };

    // File registrationImage_pol0_90 = new File(_root,
    // "TwoCamera/ProperImageSet/Pol0_90.tif");
    // File registrationImage_pol45_135 = new File(_root,
    // "TwoCamera/ProperImageSet/Pol45_135.tif");
    // ICapturedImageFile[] pol0_90 = { new RSPDummyCapturedImageFile(channel1,
    // registrationImage_pol0_90) };
    // ICapturedImageFile[] pol45_135 = { new RSPDummyCapturedImageFile(channel1,
    // registrationImage_pol45_135) };

    // RSPDummyCapturedImageFileSet fileSet = new RSPDummyCapturedImageFileSet();
    // fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_90);
    // fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45_135);

    // fileSet.setCameras(camera);

    // RegistrationImageSet registrationImageSet = new RegistrationImageSet(new
    // File(_root),
    // RegistrationImageType.SAMPLE);
    // registrationImageSet.addImageSet(fileSet);

    // SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
    // IMetadata regImgMetdata_pol0_90 =
    // metadataReader.read(registrationImage_pol0_90);
    // IMetadata regImgMetdata_pol45_135 =
    // metadataReader.read(registrationImage_pol45_135);
    // TwoCameraPolarizationConstellation constellation = new
    // TwoCameraPolarizationConstellation(
    // TwoCameraPolarizationConstellation.Position.Left,
    // TwoCameraPolarizationConstellation.Position.Left,
    // TwoCameraPolarizationConstellation.Position.Right,
    // TwoCameraPolarizationConstellation.Position.Right);
    // IPointShape intersection = new ShapeFactory().point(new long[] { 490, 250 },
    // AxisOrder.XY);

    // FoVCalculatorTwoCamera fovCalculator = new
    // FoVCalculatorTwoCamera(regImgMetdata_pol0_90, intersection,
    // regImgMetdata_pol45_135, intersection, constellation);

    // IFourPolarImagingSetup imagingSetup =
    // createFPSetup(fovCalculator.calculate(), camera, 1);
    // IRegistrationSetProcessorBuilder builder =
    // this._createDefaultBuilder(imagingSetup);

    // RegistrationSetProcessor processor = new RegistrationSetProcessor(builder);

    // assertDoesNotThrow(() -> {
    // processor.process(registrationImageSet);
    // });
    // }

    private IFourPolarImagingSetup createFPSetup(IFieldOfView fov, Cameras cameras, int numChannels) {
        RSPDummyFPSetup imagingSetup = new RSPDummyFPSetup();
        imagingSetup.setFieldOfView(fov);
        for (int channel = 1; channel <= numChannels; channel++) {
            imagingSetup.setChannel(1, null);
        }
        imagingSetup.setCameras(cameras);

        return imagingSetup;
    }

    private IRegistrationSetProcessorBuilder _createDefaultBuilder(IFourPolarImagingSetup imagingSetup) {
        int numChannels = imagingSetup.getNumChannel();

        RegistrationCapturedImageSetSegmenter segmenter = new RegistrationCapturedImageSetSegmenter(
                imagingSetup.getFieldOfView(), imagingSetup.getCameras(), numChannels);

        DescriptorBasedRegistration registrator = new DescriptorBasedRegistration();
        PercentileChannelDarkBackgroundEstimator darkBackgroundEstimator = new PercentileChannelDarkBackgroundEstimator(
                imagingSetup.getCameras());
        RegistrationCompositeFigureCreator compositeImageCreator = new RegistrationCompositeFigureCreator(numChannels,
                Color.Red, Color.Green);

        TiffCapturedImageSetReader registrationImageReader = new TiffCapturedImageSetReader(new ImgLib2ImageFactory());

        return new RSPDummyBuilder(compositeImageCreator, darkBackgroundEstimator, registrationImageReader, registrator,
                segmenter, numChannels);
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

class RSPDummyFPSetup implements IFourPolarImagingSetup {
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

class RSPDummyBuilder extends IRegistrationSetProcessorBuilder {
    private IPolarizationImageSetCompositesCreater compositeCreator;
    private IChannelDarkBackgroundEstimator backGroundEstimator;
    private ICapturedImageSetReader cSetReader;
    private IChannelRegistrator registrator;
    private ICapturedImageSetSegmenter segmenter;
    private int numchannels;

    @Override
    IPolarizationImageSetCompositesCreater getCompositeImageCreator() {
        return this.compositeCreator;
    }

    @Override
    IChannelDarkBackgroundEstimator getDarkBackgroundEstimator() {
        return this.backGroundEstimator;
    }

    @Override
    ICapturedImageSetReader getRegistrationImageReader() {
        return this.cSetReader;
    }

    @Override
    IChannelRegistrator getRegistrator() {
        return this.registrator;
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        return this.segmenter;
    }

    @Override
    int getNumChannels() {
        return this.numchannels;
    }

    public RSPDummyBuilder(IPolarizationImageSetCompositesCreater compositeCreator,
            IChannelDarkBackgroundEstimator backGroundEstimator, ICapturedImageSetReader cSetReader,
            IChannelRegistrator registrator, ICapturedImageSetSegmenter segmenter, int numchannels) {
        this.compositeCreator = compositeCreator;
        this.backGroundEstimator = backGroundEstimator;
        this.cSetReader = cSetReader;
        this.registrator = registrator;
        this.segmenter = segmenter;
        this.numchannels = numchannels;
    }

}

class RSPDummyCapturedImageFileSet implements ICapturedImageFileSet {
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

class RSPDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public RSPDummyCapturedImageFile(int[] channels, File file) {
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