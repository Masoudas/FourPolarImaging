package fr.fresnel.fourPolar.algorithm.util.image.converters;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

import io.scif.img.ImgOpener;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class GrayScaleToColorConverterTest {
    @Test
    public void useMaxEachPlane_PlaneWithNoMaximum_DrawsBlackImage() throws ConverterToImgLib2NotFound,
            InterruptedException {
        Image<UINT16> grayImage = new ImgLib2ImageFactory().create(new long[] { 30, 30, 5 }, UINT16.zero());
        IPixelCursor<UINT16> cursor = grayImage.getCursor();
       
        while (cursor.hasNext()) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(10);
            cursor.setPixel(pixel);
        }
        
        Image<RGB16> colorImage = new ImgLib2ImageFactory().create(grayImage.getDimensions(), RGB16.zero());

        GrayScaleToColorConverter.useMaxEachPlane(grayImage, colorImage);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(colorImage, RGB16.zero()));

        TimeUnit.SECONDS.sleep(20);

    }

    @Test
    public void useMaxEachPlane_() throws ConverterToImgLib2NotFound, InterruptedException {
        File grayImageFile = new File(GrayScaleToColorConverterTest.class.getResource("Example1.tif").getFile());
        ImgLib2ImageFactory factory = new ImgLib2ImageFactory();

        UnsignedShortType type = new UnsignedShortType();
        Image<UINT16> grayImage = factory.create(new ImgOpener().openImgs(grayImageFile.getAbsolutePath(), type).get(0),
                type);

        Image<RGB16> colorImage = factory.create(grayImage.getDimensions(), RGB16.zero());

        GrayScaleToColorConverter.useMaxEachPlane(grayImage, colorImage);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(colorImage, RGB16.zero()));

        TimeUnit.SECONDS.sleep(20);
    }
}