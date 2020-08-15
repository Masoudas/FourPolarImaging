package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.vectorModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.w3c.dom.Element;
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
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
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
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.image.generic.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.VectorGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.vectorFigure.animation.OrientationAnimationCreator;

/**
 * Most of the tests below are visual test. Hence, we need to read the
 * description of the image and look at the resulting image.
 * 
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
    private static int default_len = 30;
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
     * With this test, we set the stick length to one and check whether they're
     * drawn on the right location. The sticks are all horizontal and they're on the
     * same location in each plane.
     */
    @Test
    public void draw_CheckSticksAreDrawnOnCorrectPosition_DrawsCorrectStick() throws IOException, TranscoderException {
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        float angleShiftPerPlane = 0;
        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0 },
                angleShiftPerPlane);
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(new DummyWholeSampleBuilder(
                orientationImage, soiImage, 1, 1, OrientationAngle.rho, OrientationAngle.rho, null));
        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());

        ImagePlaneAccessor<SVGDocument> planes = BatikImagePlaneAccessor
                .get(((VectorGaugeFigure) painter.getFigure()).getVectorImage());
        for (long[] position : rho_angles.keySet()) {
            int planeIndex = planes.getPlaneIndex(position);
            assertTrue(_checkHorizontalStickInCorrectPosition(planes.getImagePlane(planeIndex).getPlane(), position[0],
                    position[1]));
        }
    }

    /**
     * Draws a 32*32 figure, with one very bright spot at the 16*16 pixel of the
     * image. Then a stick is drawn at the same location. We expect the stick to be
     * at the top of the soi point.
     */
    @Test
    public void draw_ASingleSoIWithRhoStick_DrawsRhoStickOnTopOfItsSoI() throws IOException, TranscoderException {
        String testName = "ASingleSoIWithRhoStick";

        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };
        long[] pose_soi = { 128, 128, 0, 0, 0 };

        Hashtable<long[], Float> rho_angles = new Hashtable<>();
        rho_angles.put(pose_soi, new Float(0));
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        Hashtable<long[], Integer> soi_vals = new Hashtable<>();
        soi_vals.put(pose_soi, new Integer(Integer.MAX_VALUE));
        ISoIImage soiImage = SoIImageCreator.createAndSetPixels(orientationImage, soi_vals);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(new DummyWholeSampleBuilder(
                orientationImage, soiImage, 1, 1, OrientationAngle.rho, OrientationAngle.rho, null));
        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());

        GaugeFigureWriter.write(root, testName, painter.getFigure());

    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, one with
     * angle zero at top left, moving to bottom right at 135. The colors start from
     * darker red to lighter red from top to bottom.
     */
    @Test
    public void draw_RhoSticksOn2DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "RhoSticksOn2DImage";
        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };

        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 45, 90, 135 }, 0);
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, one with
     * angle zero at top left, moving to bottom right at 135. The colors start from
     * darker red to lighter red from top to bottom. For plane z=1,t=0, rho is
     * augmented by angleShiftPerPlane, for z=0,t=1, by twice and for z=1,t=1 by
     * three times.
     */
    @Test
    public void draw_RhoSticksOn4DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "RhoSticksOn4DImage";
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        float angleShiftPerPlane = 15;
        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 45, 90, 135 },
                angleShiftPerPlane);
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, all
     * horizontal, The colors start from darker red to lighter red from top to
     * bottom for increasing delta.
     */
    @Test
    public void draw_DeltaSticksOn2DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "DeltaSticksOn2DImage";
        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };

        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        Hashtable<long[], Float> delta_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 60, 120, 179 },
                0);

        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.delta, delta_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultDeltaStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, all
     * horizontal, The colors start from darker red to lighter red from top to
     * bottom. For plane z=1,t=0, delta is augmented by angleShiftPerPlane, for
     * z=0,t=1, by twice and for z=1,t=1 by three times, hence sticks become brigher
     * for increasing delta
     * 
     */
    @Test
    public void draw_DeltaSticksOn4DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "DeltaSticksOn4DImage";
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        float angleShiftPerPlane = 15;
        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        Hashtable<long[], Float> delta_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 45, 90, 135 },
                angleShiftPerPlane);

        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.delta, delta_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultDeltaStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, all
     * horizontal, The colors start from darker red to lighter red from top to
     * bottom for increasing eta.
     */
    @Test
    public void draw_EtaSticksOn2DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "EtaSticksOn2DImage";
        long[] dimOrientationImg = { 256, 256, 1, 1, 1 };

        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        Hashtable<long[], Float> eta_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 30, 45, 89 }, 0);

        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.eta, eta_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultEtaStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in a single 2D plane, all
     * horizontal, The colors start from darker red to lighter red from top to
     * bottom. For plane z=1,t=0, eta is augmented by angleShiftPerPlane, for
     * z=0,t=1, by twice and for z=1,t=1 by three times, hence sticks become brigher
     * for increasing delta
     */
    @Test
    public void draw_EtaSticksOn4DImage_DrawsCorrectStick() throws IOException, TranscoderException {
        String testName = "EtaSticksOn4DImage";
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        float angleShiftPerPlane = 5;
        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        Hashtable<long[], Float> eta_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 30, 45, 60 },
                angleShiftPerPlane);

        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.eta, eta_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultEtaStickBuilder(orientationImage, soiImage));

        painter.draw(_getWholeSoIImageRoI(soiImage), UINT16.zero());
        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see two sticks in each 2D plane, all horizontal,
     * with same color. One stick would be at top left, and the other at bottom
     * right. This must happen for each plane.
     */
    @Test
    public void draw_RhoSticks4DImageTwoPartialROIs_DrawsSticksInsideRoIs() throws IOException, TranscoderException {
        String testName = "RhoSticks4DImageTwoPartialROIs";
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);

        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        IBoxShape roi1 = _createRoI(new long[] { 0, 0, 0, 0, 0 }, new long[] { 63, 63, 0, 1, 1 });
        IBoxShape roi2 = _createRoI(new long[] { 189, 189, 0, 0, 0 }, new long[] { 256, 256, 0, 1, 1 });

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));
        painter.draw(roi1, UINT16.zero());
        painter.draw(roi2, UINT16.zero());

        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    /**
     * With this test, we expect to see four sticks in each 2D plane, all
     * horizontal, with same color.
     */
    @Test
    public void draw_RhoSticks4DImageROIExceedsImageSize_DrawsSticksInsideRoIs()
            throws IOException, TranscoderException {
        String testName = "RhoSticks4DImageROIExceedsImageSize";
        long[] dimOrientationImg = { 256, 256, 1, 2, 2 };

        Hashtable<long[], Float> rho_angles = AngleCreator.create(dimOrientationImg, new float[] { 0, 0, 0, 0 }, 0);
        IOrientationImage orientationImage = OrientationImageCreator.create(dimOrientationImg, Float.NaN);
        OrientationImageCreator.setPixels(orientationImage, OrientationAngle.rho, rho_angles);
        ISoIImage soiImage = SoIImageCreator.createWithIncreasingIntensity(orientationImage);

        IBoxShape roi1 = _createRoI(new long[] { 0, 0, 0, 0, 0 }, new long[] { 300, 300, 2, 3, 3 });

        VectorWholeSampleStick2DPainter painter = new VectorWholeSampleStick2DPainter(
                getDefaultRhoStickBuilder(orientationImage, soiImage));
        painter.draw(roi1, UINT16.zero());

        GaugeFigureWriter.write(root, testName, painter.getFigure());
    }

    private static IBoxShape _getWholeSoIImageRoI(ISoIImage soiImage) {
        return ImageUtil.getBoundaryAsBox(soiImage.getImage());
    }

    private static IBoxShape _createRoI(long[] min, long[] max) {
        return ShapeFactory.closedBox(min, max, IGaugeFigure.AXIS_ORDER);
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

    /**
     * For a single horizontal stick of type line in the plane, checks whether the
     * center of the line
     */
    private static boolean _checkHorizontalStickInCorrectPosition(SVGDocument document, long x_center, long y_center) {
        Element stick = (Element) document.getDocumentElement().getElementsByTagNameNS(null, "line").item(0);

        double x1 = Double.parseDouble(stick.getAttributeNS(null, "x1"));
        double y1 = Double.parseDouble(stick.getAttributeNS(null, "y1"));

        double x2 = Double.parseDouble(stick.getAttributeNS(null, "x2"));
        double y2 = Double.parseDouble(stick.getAttributeNS(null, "y2"));

        return Math.abs((x1 + x2) / 2 - x_center) < 0.5 && Math.abs((y1 + y2) / 2 - y_center) < 0.5;
    }

}

class AngleCreator {
    /**
     * Sets angles in each plane, and for each plane applies offset * planeIndex
     */
    static Hashtable<long[], Float> create(long[] dim, float[] angles, float plane_offset) {
        Hashtable<long[], Float> position_angle = new Hashtable<>();

        for (int planeIndex = 0; planeIndex < getNPlanes(dim); planeIndex++) {
            long[] planeDim = getPlaneCoordinates(dim, planeIndex + 1);
            for (int i = 0; i < angles.length; i++) {
                position_angle.put(
                        new long[] { (i + 1) * dim[0] / (angles.length + 1), dim[1] / (angles.length + 1) * (i + 1),
                                planeDim[2], planeDim[3], planeDim[4] },
                        new Float(Math.toRadians(angles[i] + plane_offset * planeIndex)));
            }
        }

        return position_angle;
    }

    public static long[] getPlaneCoordinates(long[] imgDim, long planeIndex) {
        if (imgDim.length == 2) {
            return new long[] { imgDim[0], imgDim[1] };
        }

        planeIndex = planeIndex - 1; // To compensate for index starting from 1.
        long[] planesPerDim = numPlanesPerDimension(imgDim);
        long[] start = new long[planesPerDim.length];
        start[planesPerDim.length - 1] = planesPerDim[planesPerDim.length - 2] > 0
                ? planeIndex / planesPerDim[planesPerDim.length - 2]
                : planeIndex;

        long planeIndexRemainder = planeIndex - start[planesPerDim.length - 1] * planesPerDim[planesPerDim.length - 2];
        for (int dim = planesPerDim.length - 2; dim >= 3; dim--) {
            start[dim] = planeIndexRemainder / planesPerDim[dim - 1];
            planeIndexRemainder = planeIndexRemainder - start[dim] * planesPerDim[dim - 1];
        }
        start[2] = planeIndexRemainder;

        return start;
    }

    public static long[] numPlanesPerDimension(long[] imageDim) {
        if (imageDim.length <= 2) {
            return new long[imageDim.length];
        }

        long[] nPlanesPerDim = new long[imageDim.length];
        nPlanesPerDim[2] = imageDim[2];
        for (int dim = 3; dim < nPlanesPerDim.length; dim++) {
            nPlanesPerDim[dim] = nPlanesPerDim[dim - 1] * imageDim[dim];
        }

        return nPlanesPerDim;

    }

    private static int getNPlanes(long[] dims) {
        int nPlanes = 1;
        for (int dim = 2; dim < dims.length; dim++) {
            nPlanes *= dims[dim];
        }

        return nPlanes;
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

    public static ISoIImage createAndSetPixels(IOrientationImage orientationImage, Hashtable<long[], Integer> values) {
        Image<UINT16> soi = new ImgLib2ImageFactory()
                .create(orientationImage.getAngleImage(OrientationAngle.rho).getImage().getMetadata(), UINT16.zero());
        _setPixels(soi, values);
        return SoIImage.create(orientationImage.getCapturedSet(), soi, 1);
    }

    private static void _setPixels(Image<UINT16> image) {
        int t_axis = IGaugeFigure.AXIS_ORDER.t_axis;
        int z_axis = IGaugeFigure.AXIS_ORDER.z_axis;

        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set((int) cursor.localize()[0] * 500 + (int) cursor.localize()[t_axis] * 4000
                    + (int) cursor.localize()[z_axis] * 8000);
            cursor.setPixel(pixel);
        }
    }

    private static void _setPixels(Image<UINT16> image, Hashtable<long[], Integer> values) {
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        for (long[] position : values.keySet()) {
            ra.setPosition(position);
            ra.setPixel(new Pixel<UINT16>(new UINT16(values.get(position))));
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
        long[] plane_coords = AngleCreator.getPlaneCoordinates(metadata.getDim(), planeIndex);

        String name_with_coordinates = "plane_" + planeIndex;
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

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.DISK_STRONG_RED);
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

    @Override
    ARGB8 getStickTransparency() {
        return new ARGB8(0, 0, 0, 255);
    }

}