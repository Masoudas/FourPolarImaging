package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Random;

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
import fr.fresnel.fourPolar.core.image.polarization.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.SoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import ij.ImagePlus;
import ij.io.FileSaver;
import io.scif.img.ImgSaver;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class WholeSampleStick2DPainterTest {
    @Test
    public void rho2DStick_RhoChangesFrom0to180_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        int j = 0;
        for (int i = 0; i <= 180; i += 1) {
            j = i % 20 >= 1 ? j : j + 2;
            setPixel(ra, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 }, new Float32((float) Math.toRadians(i)));

        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 40;
        int thickness = 2;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveStickFigure(stickFigure, "rho2DStick.tiff");

        assertTrue(true);
    }

    @Test
    public void rho2DStick_4DImageRhoChangesFrom0to180_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512, 3, 80 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int c = 0; c < dim[3]; c++) {
            for (int k = 0; k < dim[2]; k++) {
                int j = 0;
                for (int i = 0; i <= 180; i += 1) {
                    j = i % 20 >= 1 ? j : j + 2;
                    setPixel(ra, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, k, c },
                            new Float32((float) Math.toRadians(i)));

                }
            }

        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 40;
        int thickness = 4;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage_3D.tif");
        _saveStickFigure(stickFigure, "rho2DStick_3D.tiff");

        assertTrue(true);
    }

    @Test
    public void delta2DStick_RhoAndDeltaChangeFrom0to180_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 300, 300 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());

        IPixelRandomAccess<Float32> rhoRA = rhoImage.getRandomAccess();
        IPixelRandomAccess<Float32> deltaRA = deltaImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        IPixelCursor<Float32> deltaCursor = deltaImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();

            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);

            deltaCursor.next();
            deltaCursor.setPixel(pixel);
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                long[] pose = new long[] { 50 + i * 50, 50 + j * 50 };
                setPixel(rhoRA, pose, new Float32((float) Math.toRadians(i * 45)));
                setPixel(deltaRA, pose, new Float32((float) Math.toRadians(j * 45)));
            }
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 20;
        int thickness = 2;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Delta2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveAngleFigure(deltaImage, "deltaImage.tif");
        _saveStickFigure(stickFigure, "delta2DStick.tiff");

        assertTrue(true);

    }

    @Test
    public void eta2DStick_RhoFrom0To180Eta0to90_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 400, 400 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());

        IPixelRandomAccess<Float32> rhoRA = rhoImage.getRandomAccess();
        IPixelRandomAccess<Float32> etaRA = etaImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        IPixelCursor<Float32> etaCursor = etaImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();

            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);

            etaCursor.next();
            etaCursor.setPixel(pixel);
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i <= 5; i++) {
                long[] pose = new long[] { 50 + i * 50, 50 + j * 50 };
                setPixel(rhoRA, pose, new Float32((float) Math.toRadians(j * 45)));
                setPixel(etaRA, pose, new Float32((float) Math.toRadians(i * 18)));
            }
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 20;
        int thickness = 2;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Eta2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveAngleFigure(deltaImage, "etaImage.tif");
        _saveStickFigure(stickFigure, "eta2DStick.tiff");

        assertTrue(true);

    }

    @Test
    public void drawRho2DStick_StickOnBorderOfImage_CutsTheStickOut()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 300, 300 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        setPixel(ra, new long[] { 0, 0 }, new Float32((float) Math.toRadians(90)));
        setPixel(ra, new long[] { 295, 295 }, new Float32((float) Math.toRadians(0)));

        setPixel(ra, new long[] { 295, 5 }, new Float32((float) Math.toRadians(90)));
        setPixel(ra, new long[] { 5, 295 }, new Float32((float) Math.toRadians(0)));

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 20;
        int thickness = 2;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 });

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage_OurOfRange.tif");
        _saveStickFigure(stickFigure, "rho2DStick_OurOfRange.tiff");

        assertTrue(true);

    }

    @Test
    public void eta2DStick_SmallerRegionThanImage_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 1024, 512, 3 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int k = 0; k < dim[2]; k++) {
            int j = 0;
            for (int i = 0; i <= 180; i += 1) {
                j = i % 20 >= 1 ? j : j + 2;
                setPixel(ra, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, k },
                        new Float32((float) Math.toRadians(i)));
            }
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);

        int length = 20;
        int thickness = 2;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Eta2D).colorMap(cMap).stickThickness(thickness).stickLen(length).build();

        IShape smallerRegionOfImage = new ShapeFactory().closedPolygon2D(new long[] { 100, 500, 400, 300, 200 },
                new long[] { 100, 100, 500, 200, 500 });

        painter.draw(smallerRegionOfImage, new UINT16(0));
        IGaugeFigure stickFigure = painter.getStickFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveStickFigure(stickFigure, "rho2DStick_smallerRegion.tiff");

        assertTrue(true);

    }

    private void setPixel(IPixelRandomAccess<Float32> ra, long[] position, Float32 value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<Float32>(value));
    }

    private void _saveAngleFigure(Image<Float32> angleImage, String angleImageName) throws ConverterToImgLib2NotFound {
        ImgSaver saver = new ImgSaver();
        String root = WholeSampleStick2DPainterTest.class.getResource("").getPath();
        saver.saveImg(new File(root, angleImageName).getPath(),
                ImageToImgLib2Converter.getImg(angleImage, Float32.zero()));

    }

    private void _saveStickFigure(IGaugeFigure stickFigure, String stickImageName) throws ConverterToImgLib2NotFound {
        String root = WholeSampleStick2DPainterTest.class.getResource("").getPath();
        ImagePlus imp = ImageJFunctions.wrapRGB(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()),
                "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(new File(root, stickImageName).getAbsolutePath());
    }

}