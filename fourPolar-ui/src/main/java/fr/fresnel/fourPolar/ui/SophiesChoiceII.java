package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.WholeSampleStick2DPainterBuilder;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.io.image.orientation.TiffOrientationImageReader;
import fr.fresnel.fourPolar.io.image.soi.TiffSoIImageReader;
import fr.fresnel.fourPolar.io.visualization.figures.gaugeFigure.tiff.TiffGaugeFigureWriter;
import javassist.tools.reflect.CannotCreateException;
import net.imglib2.img.display.imagej.ImageJFunctions;

/**
 * Given this choice, Sophie (AKA boss) can draw gauge figures that contain 2D
 * rho stick, 2D delta stick and 2D eta sticks.
 * 
 * To draw the gauge figures, the orientation images should be formed using
 * SophiesChoiceI.
 * 
 * Boss also needs to define a visualization session name. She can also
 * optionally define SoI threshold, or an ROI as documented below, as well as
 * defining stick length, thickness and color map.
 */
public class SophiesChoiceII {
    static String visualizationSessionName = "First Session";

    // 2D Stick visual params.
    static int length = 20;
    static int thickness = 2;
    static String rho2DStickColorMap = ColorMapFactory.IMAGEJ_PHASE;
    static String etaAndDelta2DStickColorMap = ColorMapFactory.IMAGEJ_PHASE;

    // Threshold for SoI. Sticks will be drawn above this threshold.
    static int soiThreshold = 50000;

    /**
     * A box RoI from min to max coordinates. The box can be 2d (in which case it
     * will be scaled to higher dimensions), or it can be a box that covers all
     * dimensions. Feel free to add coordinates in the curly braces.
     * 
     * Note that defining several RoI is possible. Ask Masoud!
     */
    static long[] min = { 0, 0, 0, 0, 0 };
    static long[] max = { 1024, 512, 0, 0, 0 };
    static IShape roi = new ShapeFactory().closedBox(min, max, AxisOrder.XYCZT);

    /**
     * If a polygon RoI is desired, comment the previous three lines and uncomment
     * these lines. Note that a polygon must have at least three points. eel free to
     * add coordinates in the curly braces
     * 
     * @throws IncompatibleCapturedImage
     * @throws CannotCreateException
     */
    // long[] xCoordinates = new long[]{1, 2, 3};
    // long[] yCoordinates = new long[]{1, 2, 3};
    // IShape roi = new ShapeFactory().closedPolygon2D(xCoordinates, yCoordinates);

    public static void main(final String[] args) throws IOException, CannotCreateException, IncompatibleCapturedImage {
        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        SampleImageSet sampleImageSet = SophiesPreChoice.createSampleImageSet();

        for (Iterator<ICapturedImageFileSet> fileSetItr = sampleImageSet.getIterator(); fileSetItr.hasNext();) {
            ICapturedImageFileSet fileSet = fileSetItr.next();
            for (int channel : SophiesPreChoice.channels) {
                final IOrientationImage orientationImage = readOrientationImage(sampleImageSet.rootFolder(), fileSet,
                        channel);
                final ISoIImage soiImage = readSoIImage(sampleImageSet.rootFolder(), fileSet, channel);

                final IAngleGaugePainter[] gaugePainters = _getGaugePainters(length, thickness, cMapRho2D,
                        cMapEtaAndDelta, orientationImage, soiImage);

                for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
                    iAngleGaugePainter.draw(roi, new UINT16(soiThreshold));
                }

                saveGaugeFigures(gaugePainters, sampleImageSet.rootFolder());
                showGaugeFigures(gaugePainters, fileSet, channel);

            }
        }

        closeAllResources();
    }

    private static final ColorMap cMapRho2D = ColorMapFactory.create(rho2DStickColorMap);
    private static final ColorMap cMapEtaAndDelta = ColorMapFactory.create(etaAndDelta2DStickColorMap);

    private static final TiffOrientationImageReader _orientationImageReader = new TiffOrientationImageReader(
            new ImgLib2ImageFactory(), SophiesPreChoice.channels.length);

    private static final TiffSoIImageReader _soiImageReader = new TiffSoIImageReader(new ImgLib2ImageFactory(),
            SophiesPreChoice.channels.length);

    private static final TiffGaugeFigureWriter _gaugeFigureWriter = new TiffGaugeFigureWriter();

    private static IAngleGaugePainter[] _getGaugePainters(final int length, final int thickness,
            final ColorMap cMapRho2D, final ColorMap cMapEtaAndDelta, final IOrientationImage orientationImage,
            final ISoIImage soiImage) {
        final IAngleGaugePainter[] gaugePainters = new IAngleGaugePainter[3];

        try {
            gaugePainters[0] = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage, AngleGaugeType.Rho2D)
                    .colorMap(cMapRho2D).stickThickness(thickness).stickLen(length).build();
            gaugePainters[1] = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage, AngleGaugeType.Delta2D)
                    .colorMap(cMapEtaAndDelta).stickThickness(thickness).stickLen(length).build();
            gaugePainters[2] = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage, AngleGaugeType.Eta2D)
                    .colorMap(cMapEtaAndDelta).stickThickness(thickness).stickLen(length).build();
        } catch (ConverterToImgLib2NotFound e) {

        }
        return gaugePainters;
    }

    public static ISoIImage readSoIImage(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException {
        return _soiImageReader.read(root4PProject, fileSet, channel);
    }

    public static IOrientationImage readOrientationImage(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException {
        IOrientationImage orientationImage = null;
        try {
            orientationImage = _orientationImageReader.readFromDegrees(root4PProject, fileSet, channel);
        } catch (CannotFormOrientationImage e) {
            // This exception is not caught, because Orientation images are properly on the
            // disk before.
            e.printStackTrace();
        }

        return orientationImage;
    }

    public static void saveGaugeFigures(IAngleGaugePainter[] gaugePainters, File root4PProject) {
        for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
            saveGaugeFigure(root4PProject, iAngleGaugePainter);
        }

    }

    public static void saveGaugeFigure(File root4PProject, final IAngleGaugePainter iAngleGaugePainter) {
        try {
            _gaugeFigureWriter.write(root4PProject, visualizationSessionName, iAngleGaugePainter.getFigure());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showGaugeFigures(IAngleGaugePainter[] gaugePainters, ICapturedImageFileSet fileSet,
            int channel) {
        for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
            try {
                ImageJFunctions.show(
                        ImageToImgLib2Converter.getImg(iAngleGaugePainter.getFigure().getImage(), RGB16.zero()),
                        iAngleGaugePainter.getFigure().getGaugeType().name() + " of " + fileSet.getSetName()
                                + " Channel " + channel);
            } catch (ConverterToImgLib2NotFound e) {
            }

        }

    }

    public static void closeAllResources() throws IOException {
        _gaugeFigureWriter.close();
        _orientationImageReader.close();
        _soiImageReader.close();
    }

}