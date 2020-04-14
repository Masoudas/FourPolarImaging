package fr.fresnel.fourPolar.algorithm.util.image.converters;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

import io.scif.img.ImgOpener;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class GrayScaleToColorConverterTest {
    @Test
    public void convertImage_() throws ConverterToImgLib2NotFound, InterruptedException {
        File grayImageFile = new File(GrayScaleToColorConverterTest.class.getResource("Example1.tif").getFile());
        ImgLib2ImageFactory factory = new ImgLib2ImageFactory();

        UnsignedShortType type = new UnsignedShortType();
        Image<UINT16> grayImage = factory.create(new ImgOpener().openImgs(grayImageFile.getAbsolutePath(), type).get(0),
                type);

        Image<RGB16> colorImage = factory.create(grayImage.getDimensions(), RGB16.zero());

        GrayScaleToColorConverter.convertPlane(grayImage, colorImage);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(colorImage, RGB16.zero()));

        TimeUnit.SECONDS.sleep(20);
    }
}