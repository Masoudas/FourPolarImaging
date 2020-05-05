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
import fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D.SingleDipoleStick2DPainterBuilder;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.SoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import net.imglib2.RealPoint;

/**
 * With this choice, Sophie (AKA boss) can draw interactive gauge figures that
 * contain 2D rho stick, 2D delta stick or 2D eta sticks.
 * 
 * To draw the gauge figures, the orientation images should be formed using
 * SophiesChoiceI.
 * 
 * To use this code, the only needed piece of information is the path to images.
 * All other parameters are optional.
 */

public class SophiesChoiceIII {
    public static void main(String[] args) throws IOException {
        // Define root folder of orientation image. NO BACKSLASHES!
        String rootFolder = "/home/masoud/Documents/SampleImages/A4PolarDataSet";

        // Define path to orientation images.
        final File rhoFile = new File(rootFolder, "rho.tif");
        final File deltaFile = new File(rootFolder, "delta.tif");
        final File etaFile = new File(rootFolder, "eta.tif");
        final File soiFile = new File(rootFolder, "soi.tif");

       
        // By changing this parameter, Delta, Rho or Eta stick can be drawn.
        AngleGaugeType angleGaugeType = AngleGaugeType.Rho2D;

        

        // 2D Stick visual params.
        final int length = 40;
        final int thickness = 2;
        final ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        // Threshold for SoI. Sticks will be drawn above this threshold.
        final int soiThreshold = 0;

        // -------------------------------------------------------------------
        // YOU DON'T NEED TO TOUCH ANYTHING FROM HERE ON!
        // -------------------------------------------------------------------
        final IOrientationImage orientationImage = readOrientationImage(rhoFile, deltaFile, etaFile);

        final SoIImage soiImage = readSoIImage(soiFile);

        final IAngleGaugePainter gaugePainter = _getGaugePainter(length, thickness, cMap,
                orientationImage, soiImage, angleGaugeType);

        _showInteractive(soiImage, gaugePainter, soiThreshold);

    }

    private static SoIImage readSoIImage(final File soiFile) throws IOException {
        ImageReader<UINT16> reader;
        SoIImage soiImage = null;
        try {
            reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), UINT16.zero());
            final Image<UINT16> soi = reader.read(soiFile);
            soiImage = new SoIImage(new CapturedImageFileSet(1, soiFile), soi);

        } catch (final NoReaderFoundForImage e) {
        }

        return soiImage;
    }

    private static IOrientationImage readOrientationImage(final File rhoFile, final File deltaFile, final File etaFile)
            throws IOException {
        ImageReader<Float32> reader;
        IOrientationImage orientationImage = null;
        try {
            reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), Float32.zero());
            final Image<Float32> rhoImage = reader.read(rhoFile);
            final Image<Float32> deltaImage = reader.read(deltaFile);
            final Image<Float32> etaImage = reader.read(etaFile);
            orientationImage = new OrientationImage(null, rhoImage, deltaImage, etaImage);

        } catch (NoReaderFoundForImage | CannotFormOrientationImage e) {
        }

        return orientationImage;
    }

    private static IAngleGaugePainter _getGaugePainter(final int length, final int thickness, final ColorMap cMap,
            final IOrientationImage orientationImage, final SoIImage soiImage, AngleGaugeType angleGaugeType) {
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
            Bdv bdv = BdvFunctions.show(ImageToImgLib2Converter.getImg(soi.getImage(), UINT16.zero()), "SoI",
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
        IShape shape = new ShapeFactory().point(pos2, AxisOrder.XY);

        painter.draw(shape, this.soiThreshold);
        bdv.getBdvHandle().getViewerPanel().requestRepaint();

    }

}