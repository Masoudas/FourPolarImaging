package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import java.io.File;
import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter.Mode;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterCompositeBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;
import fr.fresnel.fourPolar.io.image.vector.svg.batik.BatikSVGVectorImageWriter;

/**
 * Through out all the tests, we (naturally) assume that we have only one
 * channel.
 * 
 * Since blending is not a direct property of the tests, it is set to multiply.
 */
public class VectorWholeSampleStick2DPainterTest {
    private static int default_thickness = 1;
    private static int default_len = 5;
    private static File root = new File(VectorWholeSampleStick2DPainterTest.class.getResource("").getPath());

    @Test
    public void draw_Only2DSoIImage_PutsSoIInTheBackgroundGaugeFigure() throws VectorImageIOIssues {
        String testName = "Only2DSoIImage";

        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        writeGaugeFigure(painter.getFigure(), testName);
    }

    /**
     * Four images are generated, and with increasing z and t, the background
     * becomes whiter, where increase in z is greater than t.
     * 
     * @throws VectorImageIOIssues
     */
    @Test
    public void draw_Only4DSoIImage_PutsSoIInTheBackgroundGaugeFigure() throws VectorImageIOIssues {
        String testName = "Only4DSoIImage";

        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        writeGaugeFigure(painter.getFigure(), testName);
    }

    private static IBoxShape _getWholeSoIImageRoI(ISoIImage soiImage) {
        return ImageUtil.getBoundaryAsBox(soiImage.getImage());
    }

    private static IVectorWholeSampleStick2DPainterBuilder getDefaultRhoStickBuilder(IOrientationImage orientationImage,
            ISoIImage soiImage) {
        return new DummyWholeSampleBuilder(orientationImage, soiImage, default_thickness, default_len,
                OrientationAngle.rho, OrientationAngle.rho, null);
    }

    private static IVectorWholeSampleStick2DPainterBuilder getDefaultDeltaStickBuilder(
            IOrientationImage orientationImage, ISoIImage soiImage) {
        return new DummyWholeSampleBuilder(orientationImage, soiImage, default_thickness, default_len,
                OrientationAngle.rho, OrientationAngle.delta, null);
    }

    private static IVectorWholeSampleStick2DPainterBuilder getDefaultEtaStickBuilder(IOrientationImage orientationImage,
            ISoIImage soiImage) {
        return new DummyWholeSampleBuilder(orientationImage, soiImage, default_thickness, default_len,
                OrientationAngle.rho, OrientationAngle.eta, null);
    }

    private static void writeGaugeFigure(IGaugeFigure figure, String testRoot) throws VectorImageIOIssues {
        BatikSVGVectorImageWriter writer = new BatikSVGVectorImageWriter();
        writer.write(new File(root, testRoot), testRoot, ((VectorGaugeFigure) figure).getVectorImage());
    }

}

class OrientationImageCreator {
    public static IOrientationImage create(long[] dim, float initial_val) {
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(IGaugeFigure.AXIS_ORDER)
                .bitPerPixel(PixelTypes.FLOAT_32).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        _setPixels(rhoImage, initial_val);
        _setPixels(deltaImage, initial_val);
        _setPixels(etaImage, initial_val);

        try {
            return OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        } catch (CannotFormOrientationImage e) {
        }
        return null;
    }

    private static void _setPixels(Image<Float32> image, float value) {
        for (IPixelCursor<Float32> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<Float32> pixel = cursor.next();
            pixel.value().set(value);
            cursor.setPixel(pixel);
        }
    }
}

class SoIImageCreator {
    /**
     * Intensity in each row increases with x coordinate, and also z and t.
     */
    public static ISoIImage createWithIncreasingIntensity(IOrientationImage orientationImage) {
        Image<UINT16> soi = new ImgLib2ImageFactory()
                .create(orientationImage.getAngleImage(OrientationAngle.rho).getImage().getMetadata(), UINT16.zero());
        _setPixels(soi);
        return SoIImage.create(orientationImage.getCapturedSet(), soi, 1);
    }

    private static void _setPixels(Image<UINT16> image) {
        int t_axis = IGaugeFigure.AXIS_ORDER.t_axis;
        int z_axis = IGaugeFigure.AXIS_ORDER.z_axis;

        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set((int) cursor.localize()[0] * 50 + (int) cursor.localize()[t_axis] * 4000
                    + (int) cursor.localize()[z_axis] * 8000);
            cursor.setPixel(pixel);
        }
    }
}

class DummyWholeSampleFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return "Set";
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return new int[] { 1 };
    }

}

class DummyWholeSampleBuilder extends IVectorWholeSampleStick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;

    OrientationAngle slopeAngle;
    OrientationAngle colorAngle;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;

    Optional<OrientationAnimationCreator> _animCreator;

    @Override
    ColorMap getColorMap() {
        return this._colorMap;
    }

    @Override
    int getSticklength() {
        return this._length;
    }

    @Override
    IOrientationImage getOrientationImage() {
        return this._orientationImage;
    }

    @Override
    ISoIImage getSoIImage() {
        return this._soiImage;
    }

    @Override
    int getStickThickness() {
        return this._thickness;
    }

    public DummyWholeSampleBuilder(IOrientationImage _orientationImage, ISoIImage _soiImage, int _thickness,
            int _length, OrientationAngle slopeAngle, OrientationAngle colorAngle,
            OrientationAnimationCreator animCreator) {
        this._orientationImage = _orientationImage;
        this._soiImage = _soiImage;
        this._thickness = _thickness;
        this._length = _length;
        this.slopeAngle = slopeAngle;
        this.colorAngle = colorAngle;
        this._animCreator = Optional.ofNullable(animCreator);
    }

    @Override
    VectorGaugeFigure getGauageFigure() {
        if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.rho) {
            return VectorGaugeFigure.wholeSampleRho2DStick(_soiImage, new BatikVectorImageFactory());
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.delta) {
            return VectorGaugeFigure.wholeSampleDelta2DStick(_soiImage, new BatikVectorImageFactory());
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.eta) {
            return VectorGaugeFigure.wholeSampleEta2DStick(_soiImage, new BatikVectorImageFactory());
        }

        return null;
    }

    @Override
    OrientationAngle getSlopeAngle() {
        return slopeAngle;
    }

    @Override
    OrientationAngle getColorAngle() {
        return colorAngle;
    }

    @Override
    Optional<OrientationAnimationCreator> getAnimationCreator() {
        return _animCreator;
    }

    @Override
    FilterComposite getColorBlender() {
        BlenderFilter blender = new BlenderFilter(BlenderFilter.IN.SOURCE_GRAPHIC, BlenderFilter.IN.BACKGROUND_IMAGE,
                Mode.MULTIPLY, null);
        return new FilterCompositeBuilder("blender", blender).build();
    }

}