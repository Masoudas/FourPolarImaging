package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;
import fr.fresnel.fourPolar.core.image.orientation.OrientationImage;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMapFactory;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.IStickFigure;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.StickFigureFactory;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.StickType;
import net.imglib2.img.display.imagej.ImageJFunctions;

public class StickFigureFillerTest {
    @Test
    public void fillWith2DStick_RhoStick_GeneratesProperImage()
            throws CannotFormOrientationImage, ConverterToImgLib2NotFound, InterruptedException {
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

        for (int i = 0; i < 5; i++) {
            setPixel(ra, new long[] { 50 + i * 50, 50 + i * 50 }, new Float32((float) Math.toRadians(0.001 + i * 45)));
        }

        IOrientationImage orientationImage = new OrientationImage(fileSet, rhoImage, deltaImage, etaImage);
        Image<RGB16> soi = new ImgLib2ImageFactory().create(dim, RGB16.zero());
        IStickFigure stickFigure = StickFigureFactory.createExisting(StickType.Rho2D, soi, fileSet);
        
        int length = 30;
        int thickness = 5;
        ColorMap cMap = ColorMapFactory.create(ColorMapFactory.IMAGEJ_PHASE);
        
        StickFigureFiller.fillWith2DStick(orientationImage, stickFigure, length, thickness, cMap);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(stickFigure.getImage(), RGB16.zero()));
        
        TimeUnit.SECONDS.sleep(60);
        assertTrue(true);
    }

    @Test
    public void fillWith2DStick_DeltaStick_GeneratesProperImage() {
        long[] dim = { 500, 500 };
        Image<Float32> rhoImage = new ImgLib2ImageFactory().create(dim, Float32.zero());
        IPixelRandomAccess<Float32> ra = rhoImage.getRandomAccess();

        ArrayList<long[]> position = new ArrayList<>();
        ArrayList<Float32> rho = new ArrayList<>();

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 5; i++) {
                position.add(new long[] { 50 + i * 100, 50 + j * 100 });
                rho.add(new Float32(0));
            }
        }

    }

    @Test
    public void name() {
        // TODO
        /** DONT FORGET TO WRITE TEST FOR OUT OF RANGE STICKS */
    }

    private void setPixel(IPixelRandomAccess<Float32> ra, long[] position, Float32 value) {
        ra.setPosition(position);
        ra.setPixel(new Pixel<Float32>(value));
    }

}