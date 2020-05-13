package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.gauge2D;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.scijava.ui.behaviour.ClickBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.CapturedImageFileSet;
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
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;
import fr.fresnel.fourPolar.core.image.soi.SoIImage;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.image.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.IGaugeFigure;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.AngleGaugeType;
import fr.fresnel.fourPolar.core.visualization.figures.gaugeFigure.guage.IAngleGaugePainter;
import ij.ImagePlus;
import ij.io.FileSaver;
import net.imglib2.RealPoint;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class SingleDipoleStick2DPainterBuilderTest {

    /**
     * We store the sticks for 0, 45, 90 and 135 in separate figures.
     * 
     */
    @Test
    public void draw_rho2DStick_DrawsEachStickProperly() throws ConverterToImgLib2NotFound, CannotFormOrientationImage {
        int[] rhoAngles = { 0, 45, 90, 135 };
        AxisOrder axisOrder = AxisOrder.XY;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        long[] dim = { 4, 4 };
        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);

        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set(Float.NaN);
            rhoCursor.setPixel(pixel);
        }

        for (int i = 0; i < 4; i += 1) {
            setPixel(ra, new long[] { i, i }, new Float32((float) Math.toRadians(rhoAngles[i])));

        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soi = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soi);
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);

        IAngleGaugePainter painter = new SingleDipoleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D).stickLen(100).colorMap(cMap).stickThickness(8).build();

        for (int i = 0; i < 4; i++) {
            IShape point = new ShapeFactory().point(new long[] { i, i }, axisOrder);
            painter.draw(point, UINT16.zero());
            _saveStickFigure(painter.getFigure(), "rho" + rhoAngles[i] + ".tif");
        }

    }

    @Test
    public void draw_rho2DStick_InteractivelyShowDipole()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
        long[] dim = { 512, 512 };
        AxisOrder axisOrder = AxisOrder.XY;
        IMetadata metadata = new Metadata.MetadataBuilder().axisOrder(axisOrder).build();

        CapturedImageFileSet fileSet = new CapturedImageFileSet(1, new File("/aa/a.tif"));
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> deltaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<Float32> etaImage = new ImgLib2ImageFactory().create(dim, Float32.zero(), metadata);
        Image<RGB16> soi = new ImgLib2ImageFactory().create(dim, RGB16.zero(), metadata);

        Random random = new Random();
        IPixelCursor<Float32> rhoCursor = rhoImage.getCursor();
        IPixelCursor<RGB16> soiCursor = soi.getCursor();
        while (rhoCursor.hasNext()) {
            IPixel<Float32> pixel = rhoCursor.next();
            pixel.value().set((float) Math.toRadians(random.nextInt(180)));
            rhoCursor.setPixel(pixel);

            IPixel<RGB16> soiPixel = soiCursor.next();
            int white = random.nextInt(255);
            soiPixel.value().set(white, white, white);
            soiCursor.setPixel(soiPixel);
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<UINT16> soiGray = new ImgLib2ImageFactory().create(dim, UINT16.zero());
        ISoIImage soiImage = new SoIImage(fileSet, soiGray);

        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        IAngleGaugePainter painter = new SingleDipoleStick2DPainterBuilder(orientationImage, soiImage,
                AngleGaugeType.Rho2D).stickLen(50).colorMap(cMap).stickThickness(8).build();

        // Viewer to show the soi.
        Bdv bdv = BdvFunctions.show(ImageToImgLib2Converter.getImg(soi, RGB16.zero()), "SoI",
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
        String root = SingleDipoleStick2DPainterBuilderTest.class.getResource("").getPath();
        ImagePlus imp = ImageJFunctions.wrapRGB(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()),
                "RGB");
        FileSaver impSaver = new FileSaver(imp);
        impSaver.saveAsTiff(new File(root, stickImageName).getAbsolutePath());
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
        final RealPoint pos = new RealPoint(3);
        bdv.getBdvHandle().getViewerPanel().displayToGlobalCoordinates(x, y, pos);

        double[] pos1 = new double[3];
        pos.localize(pos1);
        long[] pos2 = Arrays.stream(pos1).mapToLong((t) -> (long) t).limit(2).toArray();
        IShape shape = new ShapeFactory().point(pos2, AxisOrder.XY);

        painter.draw(shape, UINT16.zero());
        bdv.getBdvHandle().getViewerPanel().requestRepaint();

    }

}