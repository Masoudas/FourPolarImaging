package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
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
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImageFactory;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.GaugeFigureType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.RealPoint;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class SingleDipoleInPlaneStickPainterTest {

    /**
     * We store the sticks for 0, 45, 90 and 135 in separate figures.
     * 
     */
    @Test
    public void draw_rho2DStick_DrawsEachStickProperly() throws ConverterToImgLib2NotFound, CannotFormOrientationImage {
        long[] dim = { 4, 4, 1, 1, 1 };
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYCZT).build();

        ICapturedImageFileSet fileSet = new DummyFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());

        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        int[] rhoAngles = { 0, 45, 90, 135 };
        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int i = 0; i < 4; i += 1) {
            setPixel(ra, new long[] { i, i, 0, 0, 0 }, new Float32((float) Math.toRadians(rhoAngles[i])));

        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        ISoIImage soiImage = SoIImage.create(fileSet, soi, 1);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        ISingleDipoleStick2DPainterBuilder builder = new DummySingleDipoleBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D, cMap, 8, 100);

        SingleDipoleInPlaneStickPainter painter = new SingleDipoleInPlaneStickPainter(builder);

        for (int i = 0; i < 4; i++) {
            IShape point = new ShapeFactory().point(new long[] { i, i, 0, 0, 0 }, AxisOrder.XYCZT);
            painter.draw(point, UINT16.zero());
            _saveStickFigure(painter.getFigure(), "rho" + rhoAngles[i] + ".tif");
        }

    }

    @Test
    public void draw_rho2DStick_InteractivelyShowDipole()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 512, 512, 1, 3, 3 };
        AxisOrder axisOrder = AxisOrder.XYCZT;
        IMetadata metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();

        ICapturedImageFileSet fileSet = new DummyFileSet();
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(metadata, Float32.zero());
        Image<UINT16> soiGray = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        Random random = new Random();
        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        IPixelCursor<UINT16> soiCursor = soiGray.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set((float) Math.toRadians(random.nextInt(180)));
            rhoCursor.setPixel(pixel);

            IPixel<UINT16> pixelSoI = soiCursor.next();
            pixelSoI.value().set(random.nextInt(180));
            soiCursor.setPixel(pixelSoI);
        }

        IOrientationImage orientationImage = OrientationImageFactory.create(fileSet, 1, rhoImage, deltaImage, etaImage);
        ISoIImage soiImage = SoIImage.create(fileSet, soiGray, 1);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        ISingleDipoleStick2DPainterBuilder builder = new DummySingleDipoleBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D, cMap, 8, 50);
        SingleDipoleInPlaneStickPainter painter = new SingleDipoleInPlaneStickPainter(builder);

        // Viewer to show the soi.
        // bdv.util.AxisOrder aOrder = bdv.util.AxisOrder.XYCZT;
        Bdv bdv = BdvFunctions.show(ImageToImgLib2Converter.getImg(soiImage.getImage(), UINT16.zero()), "SoI",
                BdvOptions.options().is2D());

        // Viewer to show the stick.
        Bdv bdv1 = BdvFunctions.show(ImageToImgLib2Converter.getImg(painter.getFigure().getImage(), RGB16.zero()),
                "Dipole", BdvOptions.options().is2D());

        Behaviours behaviours = new Behaviours(new InputTriggerConfig());
        behaviours.install(bdv.getBdvHandle().getTriggerbindings(), "my-new-behaviours");

        ShowDipoleUponClick doubleClick = new ShowDipoleUponClick(bdv1, painter);
        behaviours.behaviour(doubleClick, "print global pos", "button1");

        while (bdv.getBdvHandle().getViewerPanel().isShowing()) {

        }
    }

    private void setPixel(IPixelRandomAccess<Float32> ra, long[] position, Float32 value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<Float32>(value));
    }

    private void _saveStickFigure(IGaugeFigure stickFigure, String stickImageName) throws ConverterToImgLib2NotFound {
        File root = new File(SingleDipoleInPlaneStickPainterTest.class.getResource("").getPath() + "/SingleDipole");
        root.delete();
        root.mkdirs();

        File path = new File(root, stickImageName);

        ImagePlus imp = ImageJFunctions.wrapRGB(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()),
                "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(path.getAbsolutePath());
    }

    public void plotDipole(IAngleGaugePainter painter, long[] pos) throws ConverterToImgLib2NotFound {

    }
}

class ShowDipoleUponClick implements ClickBehaviour {
    Bdv bdv;
    IAngleGaugePainter painter;
    int place = 10;

    public ShowDipoleUponClick(Bdv bdv, IAngleGaugePainter painter) {
        this.bdv = bdv;
        this.painter = painter;

    }

    @Override
    public void click(int x, int y) {
        final RealPoint pos = new RealPoint(5);
        bdv.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);

        double[] pos1 = new double[5];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t).limit(5).toArray();
        IShape shape = new ShapeFactory().point(pos2, AxisOrder.XYCZT);

        painter.draw(shape, UINT16.zero());
        bdv.getBdvHandle().getViewerPanel().requestRepaint();

    }

}

class DummyFileSet implements ICapturedImageFileSet {

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
        // TODO Auto-generated method stub
        return null;
    }

}

class DummySingleDipoleBuilder extends ISingleDipoleStick2DPainterBuilder {
    private final IOrientationImage _orientationImage;
    private final ISoIImage _soiImage;
    private final AngleGaugeType _gaugeType;

    private ColorMap _colorMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_SPECTRUM);
    private int _thickness = 4;
    private int _length = 50;

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

    public DummySingleDipoleBuilder(IOrientationImage _orientationImage, ISoIImage _soiImage, AngleGaugeType _gaugeType,
            ColorMap _colorMap, int _thickness, int _length) {
        this._orientationImage = _orientationImage;
        this._soiImage = _soiImage;
        this._gaugeType = _gaugeType;
        this._colorMap = _colorMap;
        this._thickness = _thickness;
        this._length = _length;
    }

    @Override
    AngleGaugeType getAngleGaugeType() {
        return this._gaugeType;
    }

}