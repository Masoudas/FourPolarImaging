package fr.fresnel.fourPolar.ui;

import java.io.IOException;
import java.util.Iterator;

import fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge3D.WholeSampleStick3DPainterBuilder;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import javassist.tools.reflect.CannotCreateException;

/**
 * Given this choice, Sophie (AKA boss) can draw 3D stick gauge figures.
 * 
 * To draw the gauge figures, the orientation images should be formed using
 * SophiesChoiceI.
 * 
 * Boss also needs to define a visualization session name (Which is optionally
 * the same name as ChoiceII), as an string, surrounded by double quotes. She
 * can also optionally define SoI threshold, or an ROI as documented below, as
 * well as defining stick length, thickness and color map.
 */
public class SophiesChoiceIV {
    static String visualizationSessionName = "First Session";

    // 2D Stick visual params.
    static int length = 10;
    static int thickness = 2;
    static String stick3DColorMap = ColorMapFactory.IMAGEJ_PHASE;

    // Threshold for SoI. Sticks will be drawn above this threshold.
    static int soiThreshold = 0;

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
                final IOrientationImage orientationImage = SophiesChoiceII
                        .readOrientationImage(sampleImageSet.rootFolder(), fileSet, channel);
                final ISoIImage soiImage = SophiesChoiceII.readSoIImage(sampleImageSet.rootFolder(), fileSet, channel);

                final IAngleGaugePainter gaugePainter = _getGaugePainter(orientationImage, soiImage);

                gaugePainter.draw(roi, new UINT16(soiThreshold));

                SophiesChoiceII.saveGaugeFigure(sampleImageSet.rootFolder(), gaugePainter);

            }
        }

        SophiesChoiceII.closeAllResources();
    }

    private static IAngleGaugePainter _getGaugePainter(final IOrientationImage orientationImage,
            final ISoIImage soiImage) {
        ColorMap colorMap = ColorMapFactory.create(stick3DColorMap);

        try {
            return new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).colorMap(colorMap).stickLen(length)
                    .stickThickness(thickness).build();
        } catch (ConverterToImgLib2NotFound e) {
            // Not caught!
            return null;
        }
    }
}