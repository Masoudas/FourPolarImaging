package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge3D;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.polarization.soi.SoIImage;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.img.display.imagej.ImageJFunctions;

/**
 * Note that when creating Image for gauge figure in this text, the number of z
 * planes must soi_z * sticklen.
 */
public class WholeSampleStick3DPainterBuilderTest {
    /**
     * The figure generated in this example would be the same as the Delta2DStick,
     * because all sticks would be in the same plane.
     * 
     * The importance of this test is to ensure that the 3D figure can be generated
     * for the planar images.
     * 
     * The sticks that are drawn are flipped, in the sense that instead of showing
     * 45 they show 135. Note that three planes would be filled, because stick has
     * non-zero length in all directions
     * 
     * @throws InterruptedException
     */
    @Test
    public void draw_SingleZPlaneEta90_DrawsFlippedAngleSticks()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512 };
        AxisOrder axisOrder = AxisOrder.XY;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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
            setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 }, new Float32((float) Math.toRadians(90)));
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1024, 512 },
                axisOrder);

        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(4).colorMap(cMap).build();
        painter.draw(entireImageRegion, new UINT16(0));

        IGaugeFigure stickFigure = painter.getFigure();
        _saveStickFigure(stickFigure, "3DStick_SingleZPlaneEta90.tiff");

        assertTrue(true);

    }

    /**
     * The purpose of this test is to show that for 3dim images of xyt, the stick
     * figure is generated properly. The sticks that are drawn are flipped, in the
     * sense that instead of showing 45 they show 135, and they are at the same
     * plane for each time
     * 
     */
    @Test
    public void draw_SingleZPlaneEta90OverTime_DrawsFlippedAngleSticks()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512, 3 };
        AxisOrder axisOrder = AxisOrder.XYT;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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

        for (int t = 0; t < dim[2]; t++) {
            int j = 0;
            for (int i = 0; i <= 180; i += 1) {
                j = i % 20 >= 1 ? j : j + 2;
                setPixel(rhoRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, t },
                        new Float32((float) Math.toRadians(i)));
                setPixel(deltaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, t },
                        new Float32((float) Math.toRadians(i)));
                setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, t },
                        new Float32((float) Math.toRadians(90)));
            }

        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1021, 511, 2 },
                axisOrder);

        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(4).colorMap(cMap).build();
        painter.draw(entireImageRegion, new UINT16(0));

        IGaugeFigure stickFigure = painter.getFigure();
        _saveStickFigure(stickFigure, "3DStick_SingleZPlaneMultipleTimeEta90.tiff");

        assertTrue(true);

    }

    /**
     * The figure generated in this example would be the same as the Delta2DStick,
     * because all sticks would be in the same plane. The occupied z planes would be
     * 10 +-1, 30+-1, 50+-1 for each time stamp. Why plus minus? Because the stick
     * has a length in the x-y plane too.
     * 
     * The sticks that are drawn are flipped, in the sense that instead of showing
     * 45 they show 135.
     * 
     * @throws InterruptedException
     */
    @Test
    public void draw_MultiZPlaneEta90_DrawsFlippedAngleSticks()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512, 1, 3, 2 }; // three z, two t.

        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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

        for (int t = 0; t < dim[4]; t++) {
            for (int z = 0; z < dim[3]; z++) {
                int j = 0;
                for (int i = 0; i <= 180; i += 1) {
                    j = i % 20 >= 1 ? j : j + 2;
                    setPixel(rhoRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, 0, z, t },
                            new Float32((float) Math.toRadians(i)));
                    setPixel(deltaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, 0, z, t },
                            new Float32((float) Math.toRadians(i)));
                    setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, 0, z, t },
                            new Float32((float) Math.toRadians(90)));
                }

            }
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(4).colorMap(cMap).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0, 0 },
                new long[] { 1024, 512, 0, 3, 2 }, axisOrder);
        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveStickFigure(stickFigure, "3DStick_SingleChannel.tiff");

        assertTrue(true);

    }

    /**
     * The figure generated is a straight line regardless of rho and delta values.
     * 
     * @throws InterruptedException
     */
    @Test
    public void draw_MultiZPlaneEta0_DrawsPerpendicularSticks()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512, 3 };
        AxisOrder axisOrder = AxisOrder.XYZ;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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

        for (int z = 0; z < dim[2]; z++) {
            int j = 0;
            for (int i = 0; i <= 180; i += 1) {
                j = i % 20 >= 1 ? j : j + 2;
                setPixel(rhoRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, z },
                        new Float32((float) Math.toRadians(i)));
                setPixel(deltaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, z },
                        new Float32((float) Math.toRadians(i)));
                setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, z },
                        new Float32((float) Math.toRadians(0)));
            }

        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(10).colorMap(cMap).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0, 0 }, new long[] { 1023, 511, 2 },
                AxisOrder.XYZ);
        painter.draw(entireImageRegion, new UINT16(0));

        _saveStickFigure(painter.getFigure(), "3DStick_MultipleZPlaneEta0.tiff");

        assertTrue(true);

    }

    /**
     * In this test, we define a region which exceeds the dimensions the SoIImage,
     * but the singleZ-Plane sticks are drawn correctly.
     * 
     * @throws CannotFormOrientationImage
     * @throws ConverterToImgLib2NotFound
     */
    @Test
    public void draw_RegionOutOfSoIImage_DrawsSticksInsideTheSoiIImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 1024, 512 };
        AxisOrder axisOrder = AxisOrder.XY;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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
            setPixel(etaRA, new long[] { 70 + ((i % 20) * 45), 5 + j * 25 }, new Float32((float) Math.toRadians(90)));
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(4).colorMap(cMap).build();

        // Notice the region is out of image dimensions.
        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 2000, 2000 },
                axisOrder);
        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveStickFigure(stickFigure, "3DStick_SingleZPlane_RoIOutOfRange.tiff");

        assertTrue(true);

    }

    /**
     * In this test, we have sticks that are going outside the boundary of the image
     * frame. but the sticks are drawn regardless.
     * 
     * @throws CannotFormOrientationImage
     * @throws ConverterToImgLib2NotFound
     */
    @Test
    public void draw_OutOfRangeSticks_DrawsPartOfStickInsideFrame()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 1024, 512 };
        AxisOrder axisOrder = AxisOrder.XY;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

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

        long[] pixel0 = new long[] { 0, 0 };
        setPixel(rhoRA, pixel0, new Float32((float) Math.toRadians(45)));
        setPixel(deltaRA, pixel0, new Float32((float) Math.toRadians(90)));
        setPixel(etaRA, pixel0, new Float32((float) Math.toRadians(90)));

        long[] pixel0511 = new long[] { 0, 511 };
        setPixel(rhoRA, pixel0511, new Float32((float) Math.toRadians(90)));
        setPixel(deltaRA, pixel0511, new Float32((float) Math.toRadians(90)));
        setPixel(etaRA, pixel0511, new Float32((float) Math.toRadians(90)));

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero(), metadata);
        SoIImage soiImage = new SoIImage(fileSet, soi);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new WholeSampleStick3DPainterBuilder(orientationImage, soiImage).stickLen(20)
                .stickThickness(4).colorMap(cMap).build();

        IShape entireImageRegion = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1023, 511 }, axisOrder);
        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveStickFigure(stickFigure, "3DStick_SingleZPlane_StickOutOfImageFrame.tiff");

        assertTrue(true);

    }

    private void setPixel(IPixelRandomAccess<Float32> ra, long[] position, Float32 value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<Float32>(value));
    }

    private void _saveStickFigure(IGaugeFigure stickFigure, String stickImageName) throws ConverterToImgLib2NotFound {
        String root = WholeSampleStick3DPainterBuilderTest.class.getResource("").getPath();
        ImagePlus imp = ImageJFunctions.wrapRGB(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()),
                "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(new File(root, stickImageName).getAbsolutePath());
    }

}