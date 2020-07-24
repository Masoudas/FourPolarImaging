package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.algorithm.util.image.generic.color.GrayScaleToColorConverter;
import fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D.SingleDipoleStick2DPainterBuilder;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetNotFound;
import fr.fresnel.fourPolar.io.imageSet.acquisition.AcquisitionSetFromTextFileReader;
import fr.fresnel.fourPolar.io.imagingSetup.FourPolarImagingSetupFromYaml;
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
        _readImagingSetup();
        SampleImageSet sampleImageSet = _readSampleImageSet();

        final ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        final ICapturedImageFileSet fileSet = sampleImageSet.getIterator().next();
        final IOrientationImage orientationImage = SophiesChoiceII.readOrientationImage(sampleImageSet.rootFolder(),
                fileSet, 1);

        final ISoIImage soiImage = SophiesChoiceII.readSoIImage(sampleImageSet.rootFolder(), fileSet, 1);

        final IAngleGaugePainter gaugePainter = _getGaugePainter(length, thickness, cMap, orientationImage, soiImage,
                angleGaugeType);

        _showInteractive(soiImage, gaugePainter, 0);

    }

    private static File rootFolder = new File(SophiesPreChoice.rootFolder);
    private static IFourPolarImagingSetup setup;

    private static IFourPolarImagingSetup _readImagingSetup() throws IOException {
        setup = FourPolarImagingSetup.instance();
        FourPolarImagingSetupFromYaml reader = new FourPolarImagingSetupFromYaml(rootFolder);
        reader.read(setup);

        return setup;
    }

    private static SampleImageSet _readSampleImageSet() throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        SampleImageSet sampleImageSet = new SampleImageSet(rootFolder);

        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup);
        reader.read(sampleImageSet);

        return sampleImageSet;
    }

    private static IAngleGaugePainter _getGaugePainter(final int length, final int thickness, final ColorMap cMap,
            final IOrientationImage orientationImage, final ISoIImage soiImage, AngleGaugeType angleGaugeType) {
        IAngleGaugePainter gaugePainters = null;

        try {
            gaugePainters = new SingleDipoleStick2DPainterBuilder(orientationImage, soiImage, angleGaugeType)
                    .colorMap(cMap).stickThickness(thickness).stickLen(length).buildDeltaStickPainter();
        } catch (ConverterToImgLib2NotFound e) {

        }
        return gaugePainters;
    }

    private static void _showInteractive(ISoIImage soi, IAngleGaugePainter painter, int soiThreshold) {
        // Viewer to show the soi.
        try {
            Image<ARGB8> colorSoIImage = convertSoIToColorImage(soi);
            Bdv bdv_soi = BdvFunctions.show(ImageToImgLib2Converter.getImg(colorSoIImage, ARGB8.zero()), "SoI",
                    BdvOptions.options().is2D());

            Bdv bdv_dipole = BdvFunctions.show(
                    ImageToImgLib2Converter.getImg(painter.getFigure().getImage(), ARGB8.zero()), "Dipole",
                    BdvOptions.options().is2D());
            // Viewer to show the stick.

            Behaviours behaviours = new Behaviours(new InputTriggerConfig());
            behaviours.install(bdv_soi.getBdvHandle().getTriggerbindings(), "my-new-behaviours");

            ShowDipoleUponClick doubleClick = new ShowDipoleUponClick(bdv_soi, bdv_dipole, painter, soiThreshold);
            behaviours.behaviour(doubleClick, "print global pos", "button1");

        } catch (ConverterToImgLib2NotFound e) {
        }

    }

    private static Image<ARGB8> convertSoIToColorImage(ISoIImage soiImage) {
        try {
            return GrayScaleToColorConverter.colorUsingMaxEachPlane(soiImage.getImage());
        } catch (ConverterToImgLib2NotFound e) {
            // This dumb exception!
            return null;
        }
    }
}

class ShowDipoleUponClick implements ClickBehaviour {
    Bdv bdv_soi;
    Bdv bdv_dipole;
    IAngleGaugePainter painter;
    int place = 10;
    UINT16 soiThreshold;

    public ShowDipoleUponClick(Bdv bdv_soi, Bdv bdv_dipole, IAngleGaugePainter painter, int soiThreshold) {
        this.bdv_soi = bdv_soi;
        this.bdv_dipole = bdv_dipole;
        this.painter = painter;
        this.soiThreshold = new UINT16(soiThreshold);
    }

    @Override
    public void click(int x, int y) {
        final RealPoint pos = new RealPoint(3);
        bdv_soi.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);

        double[] pos1 = new double[5];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t).toArray();
        IShape shape = ShapeFactory.point(pos2, AxisOrder.XYCZT);

        painter.draw(shape, this.soiThreshold);
        bdv_dipole.getBdvHandle().getViewerPanel().requestRepaint();

    }

}