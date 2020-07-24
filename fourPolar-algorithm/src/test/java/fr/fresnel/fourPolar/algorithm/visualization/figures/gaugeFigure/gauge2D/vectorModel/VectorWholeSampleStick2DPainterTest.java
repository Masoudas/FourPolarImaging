package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Optional;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.junit.jupiter.api.Test;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.accessors.BatikImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.image.generic.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;

/**
 * Through out all the tests, we (naturally) assume that we have only one
 * channel.
 * 
 * Since blending is not a direct property of the tests, it is set to multiply.
 * 
 * Note that we don't need to test what happens to the sticks that are at the
 * boundary of the image, because they're automatically cut! As opposed to pixel
 * images.
 */
public class VectorWholeSampleStick2DPainterTest {
    private static int default_thickness = 5;
    private static int default_len = 20;
    private static File root = _getRoot();

    private static File _getRoot() {
        File root = new File(VectorWholeSampleStick2DPainterTest.class.getResource("").getPath(),
                "VectorWholeSampleStick2DPainterTestResults");
        root.mkdirs();
        return root;
    }

    @Test
    public void draw_Only2DSoIImage_PutsSoIInTheBackgroundGaugeFigure() throws IOException, TranscoderException {
        String testName = "Only2DSoIImage";

        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * Four images are generated, and with increasing z and t, the background
     * becomes whiter, where increase in z is greater than t.
     * 
     * @throws TranscoderException
     * @throws IOException
     * 
     * @throws VectorImageIOIssues
     */
    @Test
    public void draw_Only4DSoIImage_PutsSoIInTheBackgroundGaugeFigure() throws IOException, TranscoderException {
        String testName = "Only4DSoIImage";

        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, one with
     * angle zero at top left, moving to bottom right at 135. The colors would be
     * 
     * @throws TranscoderException
     * @throws IOException
     */
    @Test
    public void draw_RhoSticksOn2DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "RhoSticksOn2DImage";
        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };

        Hashtable<long[], Float> rho_angles = _createAngle(dimOrientationImg, new long[] { 256, 256, 0, 0, 0 },
                new float[] { 0, 45, 90, 135 });
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    private static Hashtable<long[], Float> _createAngle(long[] dim, long[] plane, float[] angles) {
        Hashtable<long[], Float> position_angle = new Hashtable<>();

        for (int i = 0; i < angles.length; i++) {
            position_angle.put(new long[] { (i + 1) * dim[0] / (angles.length + 1),
                    dim[1] / (angles.length + 1) * (i + 1), plane[2], plane[3], plane[4] },
                    new Float(Math.toRadians(angles[i])));
        }

        return position_angle;
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

    public static void setPixels(IOrientationImage orientationImage, OrientationAngle angle,
            Hashtable<long[], Float> vals) {
        IPixelRandomAccess<Float32> ra = orientationImage.getAngleImage(angle).getImage().getRandomAccess();
        Pixel<Float32> pixel = new Pixel<Float32>(new Float32(0));
        for (long[] pose : vals.keySet()) {
            ra.setPosition(pose);
            pixel.value().set(vals.get(pose));
            ra.setPixel(pixel);
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
            pixel.value().set((int) cursor.localize()[0] * 100 + (int) cursor.localize()[t_axis] * 4000
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

/**
 * Creates a folder that corresponds to imageName in root, and writes the images
 * there.
 */
class GaugeFigureWriter {
    private static final SVGTranscoder _transcoder = new SVGTranscoder();
    private static final String[] axis_order_string = IGaugeFigure.AXIS_ORDER.name().toLowerCase().split("");

    public static void write(File root, String imageName, IGaugeFigure gaugeFigure)
            throws IOException, TranscoderException {
        File imageFolder = new File(root, imageName);
        _createRootFolder(imageFolder);

        VectorImage vectorImage = ((VectorGaugeFigure) gaugeFigure).getVectorImage();
        ImagePlaneAccessor<SVGDocument> planeAccesser = BatikImagePlaneAccessor.get(vectorImage);

        for (int planeIndex = 1; planeIndex <= planeAccesser.numPlanes(); planeIndex++) {
            File imagePath = createPlaneImageFile(planeIndex, imageFolder, imageName, vectorImage.metadata());
            _writeBatikSVGDocument(planeAccesser.getImagePlane(planeIndex).getPlane(), imagePath);
        }

    }

    /**
     * Write the given batik svg document to the specified path. Note that
     * 
     * @throws IOException
     * @throws TranscoderException
     */
    private static void _writeBatikSVGDocument(SVGDocument svgDocument, File path)
            throws IOException, TranscoderException {
        if (path.exists()) {
            path.delete();
        }

        try (FileWriter writer = new FileWriter(path)) {
            PrintWriter printWriter = new PrintWriter(writer);

            TranscoderInput input = new TranscoderInput(svgDocument);
            TranscoderOutput output = new TranscoderOutput(printWriter);

            _transcoder.transcode(input, output);
        }
    }

    private static void _createRootFolder(File root) {
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    private static File createPlaneImageFile(int planeIndex, File root, String imageName, IMetadata metadata) {
        long[] plane_coords = MetadataUtil.getPlaneCoordinates(metadata, planeIndex)[1];

        String name_with_coordinates = imageName;
        for (int dim = 2; dim < plane_coords.length; dim++) {
            name_with_coordinates += "_" + axis_order_string[dim] + plane_coords[dim];
        }

        return new File(root, name_with_coordinates + ".svg");
    }

}

class DummyWholeSampleBuilder extends IVectorWholeSampleStick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;

    OrientationAngle slopeAngle;
    OrientationAngle colorAngle;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_JET);
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
        VectorGaugeFigure gaugeFigure = null;
        if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.rho) {
            gaugeFigure = VectorGaugeFigure.wholeSampleRho2DStick(_soiImage, new BatikVectorImageFactory());
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.delta) {
            gaugeFigure = VectorGaugeFigure.wholeSampleDelta2DStick(_soiImage, new BatikVectorImageFactory());
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.eta) {
            gaugeFigure = VectorGaugeFigure.wholeSampleEta2DStick(_soiImage, new BatikVectorImageFactory());
        }

        return gaugeFigure;
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
    Optional<FilterComposite> getColorBlender() {
        return Optional.ofNullable(null);
    }

}