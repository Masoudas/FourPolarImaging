package fr.fresnel.fourPolar.ui.algorithms.preprocess.registration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile.PercentileChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.FoVCalculatorOneCamera;
import fr.fresnel.fourPolar.algorithm.preprocess.fov.IFoVCalculator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased.DescriptorBasedRegistration;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.RegistrationCapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.RegistrationCompositeFigureCreator;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.io.exceptions.image.generic.metadata.MetadataParseError;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.captured.tiff.TiffCapturedImageSetReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.metadata.SCIFIOMetadataReader;

public class RegistrationSetProcessorTest {
    private static String _root = RegistrationSetProcessor.class.getResource("").getPath();

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

    private IFourPolarImagingSetup createFPSetup(IFieldOfView fov, Cameras cameras, int numChannels) {
        RSPDummyFPSetup imagingSetup = new RSPDummyFPSetup();
        imagingSetup.setFieldOfView(fov);
        for (int channel = 1; channel <= numChannels; channel++) {
            imagingSetup.setChannel(1, null);
        }
        imagingSetup.setCameras(cameras);

        return imagingSetup;
    }

    public void process_AllZeroImage_ThrowsRegistrationSetProcessFailure() throws IOException, MetadataParseError {
        SCIFIOMetadataReader metadataReader = new SCIFIOMetadataReader();
        IMetadata regImgMetdata = metadataReader.read(new File(_root, "OneCamera/AllZeroImage.tif"));

        IPointShape intersection = new ShapeFactory().point(new long[] { 2, 2 }, AxisOrder.XY);

        OneCameraPolarizationConstellation constellation = new OneCameraPolarizationConstellation(Position.TopLeft,
                Position.TopRight, Position.BottomLeft, Position.BottomRight);

        FoVCalculatorOneCamera fovCalculator = new FoVCalculatorOneCamera(regImgMetdata, intersection, constellation);

        IFourPolarImagingSetup imagingSetup = createFPSetup(fovCalculator.calculate(), Cameras.One, 1);
        IRegistrationSetProcessorBuilder builder = this._createDefaultBuilder(imagingSetup);
     
        RegistrationSetProcessor processor = new RegistrationSetProcessor(builder);

    }

}

class RSPDummyFoV implements IFieldOfView {
    HashMap<Polarization, IBoxShape> map = new HashMap<Polarization, IBoxShape>();

    public RSPDummyFoV(IBoxShape pol0, IBoxShape pol45, IBoxShape pol90, IBoxShape pol135) {
        map.put(Polarization.pol0, pol0);
        map.put(Polarization.pol45, pol45);
        map.put(Polarization.pol90, pol90);
        map.put(Polarization.pol135, pol135);
    }

    @Override
    public IBoxShape getFoV(Polarization pol) {
        return map.get(pol);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    IChannelDarkBackgroundEstimator getDarkBackgroundEstimator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    ICapturedImageSetReader getRegistrationImageReader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    IChannelRegistrator getRegistrator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    ICapturedImageSetSegmenter getSegmenter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    int getNumChannels() {
        // TODO Auto-generated method stub
        return 0;
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