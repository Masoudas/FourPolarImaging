package fr.fresnel.fourPolar.algorithm.util.image.color;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import io.scif.config.SCIFIOConfig;
import io.scif.img.ImgOpener;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

public class MaxPlaneGrayScaleToColorConverterTest {
    @Test
    public void useMaxEachPlane_ImageXYCZT_PlaneWithNoMaximum_DrawsBlackImage()
            throws ConverterToImgLib2NotFound, InterruptedException {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 30, 30, 1, 2, 2 }).axisOrder(AxisOrder.XYCZT)
                .build();
        Image<UINT16> grayImage = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        IPixelCursor<UINT16> cursor = grayImage.getCursor();

        while (cursor.hasNext()) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(10);
            cursor.setPixel(pixel);
        }

        Image<RGB16> colorImage = GrayScaleToColorConverter.useMaxEachPlane(grayImage);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(colorImage, RGB16.zero()));

        TimeUnit.SECONDS.sleep(20);

    }

    @Test
    public void useMaxEachPlane_AGrayImageFromDisk_KeepsGrayValuesAfterColoring()
            throws ConverterToImgLib2NotFound, InterruptedException {
        File grayImageFile = new File(MaxPlaneGrayScaleToColorConverterTest.class.getResource("Example1.tif").getFile());
        ImgLib2ImageFactory factory = new ImgLib2ImageFactory();

        Img<UnsignedShortType> diskImage = new ImgOpener()
                .openImgs(grayImageFile.getAbsolutePath(), new UnsignedShortType(), new SCIFIOConfig()).get(0);

        // Build metadata, appending z, c and t.
        IMetadata grayMetadata = new Metadata.MetadataBuilder(new long[] { 1024, 1024, 1, 1, 1 })
                .axisOrder(AxisOrder.XYCZT).build();
        Image<UINT16> grayImage = factory.create(grayMetadata, UINT16.zero());

        IPixelCursor<UINT16> grayCursor = grayImage.getCursor();
        for (Cursor<UnsignedShortType> cursor = diskImage.cursor(); cursor.hasNext();) {
            UnsignedShortType val = cursor.next();
            long[] pos = { 0, 0, 0, 0, 0 };
            cursor.localize(pos);

            IPixel<UINT16> pixel = grayCursor.next();
            pixel.value().set(val.get());
            grayCursor.setPixel(pixel);

        }

        Image<RGB16> colorImage = GrayScaleToColorConverter.useMaxEachPlane(grayImage);

        ImageJFunctions.show(ImageToImgLib2Converter.getImg(colorImage, RGB16.zero()));

        TimeUnit.SECONDS.sleep(20);
    }
}