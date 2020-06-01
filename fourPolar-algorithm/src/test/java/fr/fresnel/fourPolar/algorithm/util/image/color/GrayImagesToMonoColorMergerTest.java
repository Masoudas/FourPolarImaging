package fr.fresnel.fourPolar.algorithm.util.image.color;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class GrayImagesToMonoColorMergerTest {
    @Test
    public void merge_MergeAsRAndG_ReturnsCorrectImage() {
        IMetadata metadata1 = new Metadata.MetadataBuilder(new long[]{2,1}).bitPerPixel(PixelTypes.UINT_16).build();
        IMetadata metadata2 = new Metadata.MetadataBuilder(new long[]{2,1}).bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> image2 = new ImgLib2ImageFactory().create(metadata2, UINT16.zero());

        IPixelCursor<UINT16> pixelCursor1 = image1.getCursor();
        IPixel<UINT16> pixel1 = pixelCursor1.next();
        pixel1.value().set(UINT16.MAX_VAL);
        pixelCursor1.setPixel(pixel1);

        IPixelCursor<UINT16> pixelCursor2 = image2.getCursor();
        IPixel<UINT16> pixel2 = pixelCursor2.next();
        pixel2.value().set(UINT16.MAX_VAL);
        pixelCursor2.setPixel(pixel2);

        Image<RGB16> monochromeImage = GrayImagesToMonoColorMerger.convert(image1, Color.Red, image2, Color.Green);
        RGB16 color = monochromeImage.getCursor().next().value();

        assertTrue(color.getB() == 0 && color.getG() == 255 && color.getR() == 255);
    }
    
}