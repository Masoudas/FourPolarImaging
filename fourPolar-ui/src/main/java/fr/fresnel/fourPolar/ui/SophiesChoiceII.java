package fr.fresnel.fourPolar.ui;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.GaugePainterFactory;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.SoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.TiffImageReaderFactory;
import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.img.display.imagej.ImageJFunctions;

/**
 * Given this choice, Sophie (AKA boss) can draw gauge figures that contain 2D
 * rho stick, 2D delta stick and 2D eta sticks.
 * 
 * To draw the gauge figures, the orientation images should be formed using
 * SophiesChoiceI.
 */
public class SophiesChoiceII {
    public static void main(final String[] args) throws IOException {
        // Define path to orientation images. NO BACKWARD SLASHES!
        final File rhoFile = new File("/home/masoud/Documents/SampleImages/1/rho.tif");
        final File deltaFile = new File("/home/masoud/Documents/SampleImages/1/delta.tif");
        final File etaFile = new File("/home/masoud/Documents/SampleImages/1/eta.tif");
        final File soiFile = new File("/home/masoud/Documents/SampleImages/1/soi.tif");

        // Stick visual params.
        final int length = 40;
        final int thickness = 2;
        final ColorMap cMapRho2D = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        final ColorMap cMapEtaAndDelta = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        // Threshold for SoI. Sticks will be drawn above this threshold.
        final int soiThreshold = 0;

        /**
         * A box RoI from min to max coordinates. The box can be 2d (in which case it
         * will be scaled to higher dimensions), or it can be a box that covers all
         * dimensions. Feel free to add coordinates in the curly braces.
         * 
         * Note that defining several RoI is possible. Ask Masoud!
         */
        final long[] min = { 0, 0 };
        final long[] max = { 1024, 512 };
        final IShape roi = new ShapeFactory().closedBox(min, max);

        /**
         * If a polygon RoI is desired, comment the previous three lines and uncomment
         * these lines. Note that a polygon must have at least three points. eel free to
         * add coordinates in the curly braces
         */
        // long[] xCoordinates = new long[]{1, 2, 3};
        // long[] yCoordinates = new long[]{1, 2, 3};
        // IShape roi = new ShapeFactory().closedPolygon2D(xCoordinates, yCoordinates);

        final IOrientationImage orientationImage = readOrientationImage(rhoFile, deltaFile, etaFile);

        final SoIImage soiImage = readSoIImage(rhoFile);

        final IAngleGaugePainter[] gaugePainters = _getGaugePainters(length, thickness, cMapRho2D, cMapEtaAndDelta,
                orientationImage, soiImage);

        for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
            iAngleGaugePainter.draw(roi, new UINT16(soiThreshold));
        }

        saveGaugeFigures(gaugePainters, soiFile);
        showSoIImages(gaugePainters);

    }

    private static IAngleGaugePainter[] _getGaugePainters(final int length, final int thickness,
            final ColorMap cMapRho2D, final ColorMap cMapEtaAndDelta, final IOrientationImage orientationImage,
            final SoIImage soiImage) {
        final IAngleGaugePainter[] gaugePainters = new IAngleGaugePainter[3];

        gaugePainters[0] = GaugePainterFactory.rho2DStick(orientationImage, soiImage, length, thickness, cMapRho2D);
        gaugePainters[1] = GaugePainterFactory.delta2DStick(orientationImage, soiImage, length, thickness,
                cMapEtaAndDelta);
        gaugePainters[2] = GaugePainterFactory.eta2DStick(orientationImage, soiImage, length, thickness,
                cMapEtaAndDelta);
        return gaugePainters;
    }

    private static SoIImage readSoIImage(final File rhoFile) throws IOException {
        ImageReader<UINT16> reader;
        SoIImage soiImage = null;
        try {
            reader = TiffImageReaderFactory.getReader(new ImgLib2ImageFactory(), UINT16.zero());
            final Image<UINT16> soi = reader.read(rhoFile);
            soiImage = new SoIImage(null, soi);

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

    private static void saveGaugeFigures(IAngleGaugePainter[] gaugePainters, File soiFile) {
        for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
            ImagePlus imp;
            try {
                imp = ImageJFunctions.wrapRGB(
                        ImageToImgLib2Converter.getImg(iAngleGaugePainter.getStickFigure().getImage(), RGB16.zero()),
                        "RGB");
                final FileSaver impSaver = new FileSaver(imp);
                impSaver.saveAsTiff(new File(soiFile.getParentFile().getAbsolutePath(),
                        iAngleGaugePainter.getStickFigure().getType().name()).getAbsolutePath());

            } catch (final ConverterToImgLib2NotFound e) {
            }

        }

    }

    private static void showSoIImages(IAngleGaugePainter[] gaugePainters) {
        for (final IAngleGaugePainter iAngleGaugePainter : gaugePainters) {
            try {
                ImageJFunctions.show(
                        ImageToImgLib2Converter.getImg(iAngleGaugePainter.getStickFigure().getImage(), RGB16.zero()),
                        iAngleGaugePainter.getStickFigure().getType().name());
            } catch (ConverterToImgLib2NotFound e) {
            }

        }

    }

}