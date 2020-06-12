package fr.fresnel.fourPolar.ui;

import java.io.IOException;
import java.util.Arrays;

import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.SingleDipoleStick2DPainterBuilder;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
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
import javassist.tools.reflect.CannotCreateException;
import net.imglib2.RealPoint;

/**
 * Given this choice, Sophie (AKA boss) can draw interactive gauge figures that
 * contain 2D rho stick, 2D delta stick or 2D eta sticks.
 * 
 * To draw the gauge figures, the orientation images should be formed using
 * SophiesChoiceI.
 *
 * All other parameters are optional, including stick length, thickness and
 * color map.
 * 
 * Note that the gauge figure is drawing only for the first channel of the first
 * captured image given in PreChoice. If wish to analyze multiple captured
 * images or different channels, talk to Masoud.
 */

public class SophiesChoiceIII {
    // By changing this parameter, Delta, Rho or Eta stick can be drawn.
    static AngleGaugeType angleGaugeType = AngleGaugeType.Rho2D;

    // 2D Stick visual params.
    static int length = 40;
    static int thickness = 2;
    static String stickColorMap = ColorMapFactory.IMAGEJ_PHASE;

    public static void main(String[] args) throws IOException, CannotCreateException, IncompatibleCapturedImage {
        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        final ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        final SampleImageSet sampleImageSet = SophiesPreChoice.createSampleImageSet();

        final ICapturedImageFileSet fileSet = sampleImageSet.getIterator().next();
        int channel = SophiesPreChoice.channels[0];

        final IOrientationImage orientationImage = SophiesChoiceII.readOrientationImage(sampleImageSet.rootFolder(),
                fileSet, channel);

        final ISoIImage soiImage = SophiesChoiceII.readSoIImage(sampleImageSet.rootFolder(), fileSet, channel);

        final IAngleGaugePainter gaugePainter = _getGaugePainter(length, thickness, cMap, orientationImage, soiImage,
                angleGaugeType);

        _showInteractive(soiImage, gaugePainter, 0);

    }

    private static IAngleGaugePainter _getGaugePainter(final int length, final int thickness, final ColorMap cMap,
            final IOrientationImage orientationImage, final ISoIImage soiImage, AngleGaugeType angleGaugeType) {
        IAngleGaugePainter gaugePainters = null;

        try {
            gaugePainters = new SingleDipoleStick2DPainterBuilder(orientationImage, soiImage, angleGaugeType)
                    .colorMap(cMap).stickThickness(thickness).stickLen(length).build();
        } catch (ConverterToImgLib2NotFound e) {

        }
        return gaugePainters;
    }

    private static void _showInteractive(ISoIImage soi, IAngleGaugePainter painter, int soiThreshold) {
        // Viewer to show the soi.
        try {
            Image<RGB16> colorSoIImage = convertSoIToColorImage(soi);
            Bdv bdv = BdvFunctions.show(ImageToImgLib2Converter.getImg(colorSoIImage, RGB16.zero()), "SoI",
                    BdvOptions.options().is2D());

            Bdv bdv1 = BdvFunctions.show(ImageToImgLib2Converter.getImg(painter.getFigure().getImage(), RGB16.zero()),
                    "Dipole", BdvOptions.options().is2D());
            // Viewer to show the stick.

            Behaviours behaviours = new Behaviours(new InputTriggerConfig());
            behaviours.install(bdv.getBdvHandle().getTriggerbindings(), "my-new-behaviours");

            ShowDipoleUponClick doubleClick = new ShowDipoleUponClick(bdv1, painter, soiThreshold);
            behaviours.behaviour(doubleClick, "print global pos", "button1");

            while (bdv.getBdvHandle().getViewerPanel().isShowing()) {

            }

        } catch (ConverterToImgLib2NotFound e) {
        }

    }

    private static Image<RGB16> convertSoIToColorImage(ISoIImage soiImage) {
        try {
            return GrayScaleToColorConverter.colorUsingMaxEachPlane(soiImage.getImage());
        } catch (ConverterToImgLib2NotFound e) {
            // This dumb exception!
            return null;
        }
    }
}

class ShowDipoleUponClick implements ClickBehaviour {
    Bdv bdv;
    IAngleGaugePainter painter;
    int place = 10;
    UINT16 soiThreshold;

    public ShowDipoleUponClick(Bdv bdv, IAngleGaugePainter painter, int soiThreshold) {
        this.bdv = bdv;
        this.painter = painter;
        this.soiThreshold = new UINT16(soiThreshold);
    }

    @Override
    public void click(int x, int y) {
        final RealPoint pos = new RealPoint(3);
        bdv.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);

        double[] pos1 = new double[3];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t).limit(2).toArray();
        IShape shape = new ShapeFactory().point(pos2, AxisOrder.XYCZT);

        painter.draw(shape, this.soiThreshold);
        bdv.getBdvHandle().getViewerPanel().requestRepaint();

    }

}