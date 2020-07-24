package fr.fresnel.fourPolar.algorithm.visualization.figures.gaugeFigure.gauge2D;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.ColorBlender;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.color.SoftLightColorBlender;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.generic.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.ShapeUtils;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import ij.ImagePlus;
import ij.io.FileSaver;
import io.scif.img.ImgSaver;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class WholeSampleStick2DPainterTest {
    @Test
    public void rho2DStick_5DImageRhoChangesFrom0to180_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 1024, 512, 1, 3, 3 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int c = 0; c < dim[4]; c++) {
            for (int k = 0; k < dim[3]; k++) {
                int j = 0;
                for (int i = 0; i <= 180; i += 1) {
                    j = i % 20 >= 1 ? j : j + 2;
                    setPixel(ra, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, 0, k, c },
                            new Float32((float) Math.toRadians(i)));

                }
            }

        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        ISoIImage soiImage = _createSoIImage(metadata, fileSet);

        int length = 40;
        int thickness = 4;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IWholeSampleStick2DPainterBuilder builder = new DummyWholeSampleBuilder(orientationImage, soiImage, cMap,
                thickness, length, new SoftLightColorBlender(), OrientationAngle.rho, OrientationAngle.rho);
        IAngleGaugePainter painter = new WholeSampleStick2DPainter(builder);

        IShape entireImageRegion = ShapeFactory.closedBox(new long[] { 0, 0, 0, 0, 0 },
                new long[] { 1024, 512, 0, 3, 80 }, axisOrder);

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveAngleFigure(rhoImage, "rhoImage_5D.tif");
        _saveStickFigure(stickFigure, "rho2DStick_5D.tiff");

        assertTrue(true);
    }

    /**
     * In stick figure, each row has same delta, but different rho.
     */
    @Test
    public void delta2DStick_RhoAndDeltaChangeFrom0to180_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 300, 300, 1, 1, 1 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());

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
                long[] pose = new long[] { 50 + i * 50, 50 + j * 50, 0, 0, 0 };
                setPixel(rhoRA, pose, new Float32((float) Math.toRadians(j * 45)));
                setPixel(deltaRA, pose, new Float32((float) Math.toRadians(j * 45)));
            }
        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        ISoIImage soiImage = _createSoIImage(metadata, fileSet);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        int length = 20;
        int thickness = 2;

        IWholeSampleStick2DPainterBuilder builder = new DummyWholeSampleBuilder(orientationImage, soiImage, cMap,
                thickness, length, new SoftLightColorBlender(), OrientationAngle.rho, OrientationAngle.delta);
        IAngleGaugePainter painter = new WholeSampleStick2DPainter(builder);

        IShape entireImageRegion = ShapeFactory.closedBox(new long[] { 0, 0, 0, 0, 0 },
                new long[] { 1024, 512, 0, 0, 0 }, axisOrder);

        painter.draw(entireImageRegion, UINT16.zero());
        IGaugeFigure stickFigure = painter.getFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveAngleFigure(deltaImage, "deltaImage.tif");
        _saveStickFigure(stickFigure, "delta2DStick.tiff");

        assertTrue(true);

    }

    /**
     * In the orientation image, rho increases in each column, and eta in each row.
     * Hence, different colors on each row, and different angles on each column.
     */
    @Test
    public void eta2DStick_RhoFrom0To180Eta0to90_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 400, 400, 1, 1, 1 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());

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
                long[] pose = new long[] { 50 + i * 50, 50 + j * 50, 0, 0, 0 };
                setPixel(rhoRA, pose, new Float32((float) Math.toRadians(j * 45)));
                setPixel(etaRA, pose, new Float32((float) Math.toRadians(i * 18)));
            }
        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);

        ISoIImage soiImage = _createSoIImage(metadata, fileSet);

        int length = 20;
        int thickness = 5;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IWholeSampleStick2DPainterBuilder builder = new DummyWholeSampleBuilder(orientationImage, soiImage, cMap,
                thickness, length, new SoftLightColorBlender(), OrientationAngle.rho, OrientationAngle.eta);
        IAngleGaugePainter painter = new WholeSampleStick2DPainter(builder);

        IShape entireImageRegion = ShapeFactory.closedBox(new long[] { 0, 0, 0, 0, 0 },
                new long[] { 1024, 512, 0, 0, 0 }, axisOrder);

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveAngleFigure(rhoImage, "rhoImage.tif");
        _saveAngleFigure(deltaImage, "etaImage.tif");
        _saveStickFigure(stickFigure, "eta2DStick.tiff");

        assertTrue(true);

    }

    @Test
    public void drawRho2DStick_StickOnBorderOfImage_CutsTheStickOut()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 300, 300, 1, 1, 1 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        setPixel(ra, new long[] { 0, 0, 0, 0, 0 }, new Float32((float) Math.toRadians(90)));
        setPixel(ra, new long[] { 295, 295, 0, 0, 0 }, new Float32((float) Math.toRadians(0)));

        setPixel(ra, new long[] { 295, 0, 0, 0, 0 }, new Float32((float) Math.toRadians(90)));
        setPixel(ra, new long[] { 5, 295, 0, 0, 0 }, new Float32((float) Math.toRadians(0)));

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        ISoIImage soiImage = _createSoIImage(metadata, fileSet);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        int length = 20;
        int thickness = 5;

        IWholeSampleStick2DPainterBuilder builder = new DummyWholeSampleBuilder(orientationImage, soiImage, cMap,
                thickness, length, new SoftLightColorBlender(), OrientationAngle.rho, OrientationAngle.rho);
        IAngleGaugePainter painter = new WholeSampleStick2DPainter(builder);

        IShape entireImageRegion = ShapeFactory.closedBox(new long[] { 0, 0, 0, 0, 0 },
                new long[] { 1024, 512, 0, 0, 0 }, axisOrder);

        painter.draw(entireImageRegion, new UINT16(0));
        IGaugeFigure stickFigure = painter.getFigure();

        _saveAngleFigure(rhoImage, "rhoImage_OurOfRange.tif");
        _saveStickFigure(stickFigure, "rho2DStick_OurOfRange.tiff");

        assertTrue(true);

    }

    @Test
    public void eta2DStick_TwoSmallerRegionThanImage_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound {
        long[] dim = { 1024, 512, 1, 3, 1 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyWholeSampleFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int k = 0; k < dim[3]; k++) {
            int j = 0;
            for (int i = 0; i <= 180; i += 1) {
                j = i % 20 >= 1 ? j : j + 2;
                setPixel(ra, new long[] { 70 + ((i % 20) * 45), 5 + j * 25, 0, k, 0 },
                        new Float32((float) Math.toRadians(i)));
            }
        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        ISoIImage soiImage = SoIImage.create(fileSet, soi, 1);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        int length = 20;
        int thickness = 5;

        IWholeSampleStick2DPainterBuilder builder = new DummyWholeSampleBuilder(orientationImage, soiImage, cMap,
                thickness, length, new SoftLightColorBlender(), OrientationAngle.rho, OrientationAngle.eta);
        IAngleGaugePainter painter = new WholeSampleStick2DPainter(builder);

        IShape smallerRegionOfImage1 = ShapeFactory.closedPolygon2D(new long[] { 100, 500, 400, 300, 200 },
                new long[] { 100, 100, 500, 200, 500 });

        IShape smallerRegionOfImage2 = ShapeFactory.closedBox(new long[] { 600, 300 }, new long[] { 800, 500 },
                AxisOrder.XY);

        IShape scaledRegion1 = ShapeUtils.addNewDimension(smallerRegionOfImage1, axisOrder, new long[] { 1, 3, 1 });
        IShape scaledRegion2 = ShapeUtils.addNewDimension(smallerRegionOfImage2, axisOrder, new long[] { 1, 3, 1 });

        painter.draw(scaledRegion1, new UINT16(0));
        painter.draw(scaledRegion2, new UINT16(0));

        IGaugeFigure stickFigure = painter.getFigure();

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
        File root = new File(WholeSampleStick2DPainterTest.class.getResource("").getPath(), "/WholeSample");
        root.mkdir();

        ImagePlus imp = ImageJFunctions
                .wrapRGB(ImageToImgLib2Converter.getImg(((GaugeFigure) stickFigure).getImage(), ARGB8.zero()), "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(new File(root, stickImageName).getAbsolutePath());
    }

    private ISoIImage _createSoIImage(IMetadata metadata, ICapturedImageFileSet fileSet) {
        Image<UINT16> soi = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        for (IPixelCursor<UINT16> cursor = soi.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set((int) cursor.localize()[0]);
            cursor.setPixel(pixel);
        }

        ISoIImage soiImage = SoIImage.create(fileSet, soi, 1);
        return soiImage;
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

class DummyWholeSampleBuilder extends IWholeSampleStick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;

    OrientationAngle slopeAngle;
    OrientationAngle colorAngle;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;
    private ColorBlender _blender;

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

    public DummyWholeSampleBuilder(IOrientationImage _orientationImage, ISoIImage _soiImage, ColorMap _colorMap,
            int _thickness, int _length, ColorBlender blender, OrientationAngle slopeAngle,
            OrientationAngle colorAngle) {
        this._orientationImage = _orientationImage;
        this._soiImage = _soiImage;
        this._colorMap = _colorMap;
        this._thickness = _thickness;
        this._length = _length;
        this._blender = blender;
        this.slopeAngle = slopeAngle;
        this.colorAngle = colorAngle;
    }

    @Override
    ColorBlender getColorBlender() {
        return this._blender;
    }

    @Override
    GaugeFigure getGauageFigure() {
        if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.rho) {
            return GaugeFigure.wholeSampleRho2DStick(_soiImage);
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.delta) {
            return GaugeFigure.wholeSampleDelta2DStick(_soiImage);
        } else if (slopeAngle == OrientationAngle.rho && colorAngle == OrientationAngle.eta) {
            return GaugeFigure.wholeSampleEta2DStick(_soiImage);
        }

        return null;
    }

    @Override
    OrientationAngle getSlopeAngle() {
        return slopeAngle;
    }

    @Override
    OrientationAngle getColorAngle() {
        return colorAngle;
    }

}