package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
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
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class Angle3DStickPainterTest {
    @Test
    public void draw_SingleZPlaneEta90_DrawsInverseAngleSticks() throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 1024, 512 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());

        IPixelRandomAccess<Float32> rhoRA = rhoImage.getRandomAccess();
        IPixelRandomAccess<Float32> deltaRA = deltaImage.getRandomAccess();
        IPixelRandomAccess<Float32> etaRA = etaImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        IPixelCursor<Float32> deltaCursor = deltaImage.getCursor();
        IPixelCursor<Float32> etaCursor = etaImage.getCursor();

        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            deltaCursor.next();
            etaCursor.next();

            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
            deltaCursor.setPixel(pixel);
            etaCursor.setPixel(pixel);
        }

        int j = 0;
        for (int i = 0; i <= 180; i += 1) {
            j = i % 20 >= 1 ? j : j + 2;
            setPixel(rhoRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 }, new Float32((float) Math.toRadians(i)));
            setPixel(deltaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 }, new Float32((float) Math.toRadians(i)));
            setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 },
                    new Float32((float) Math.toRadians(90)));
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        SoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 20;
        int thickness = 16;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        Image<RGB16> gaugeImage = new ImgLib2ImageFactory().create(new long[] { dim[0], dim[1], 1, length },
                RGB16.zero());
        IGaugeFigure gaugeFigure = GaugeFigureFactory.createExisting(AngleGaugeType.Stick3D, gaugeImage, fileSet);

        Angle3DStickPainter painter = new Angle3DStickPainter(gaugeFigure, orientationImage, soiImage, length,
                thickness, cMap);
        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveStickFigure(stickFigure, "3DStick_SingleZPlane.tiff");

        assertTrue(true);

    }

    private void setPixel(IPixelRandomAccess<Float32> ra, long[] position, Float32 value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<Float32>(value));
    }

    private void _saveStickFigure(IGaugeFigure stickFigure, String stickImageName) throws ConverterToImgLib2NotFound {
        String root = Angle2DStickPainterTest.class.getResource("").getPath();
        ImagePlus imp = ImageJFunctions.wrapRGB(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()),
                "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(new File(root, stickImageName).getAbsolutePath());
    }

 
}