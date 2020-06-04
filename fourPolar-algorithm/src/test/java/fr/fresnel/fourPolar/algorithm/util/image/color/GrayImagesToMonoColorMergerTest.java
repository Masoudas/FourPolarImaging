package fr.fresnel.fourPolar.algorithm.util.image.color;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

public class GrayImagesToMonoColorMergerTest {
    @Test
    public void merge_CreateGreen2DImage_ReturnsCorrectImage() {
        IMetadata metadata1 = new Metadata.MetadataBuilder(new long[] { 2, 1 }).bitPerPixel(PixelTypes.UINT_16).build();

        Image<UINT16> image1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        _setPixel(image1, new long[] { 0, 0 }, UINT16.MAX_VAL);

        IPixelRandomAccess<RGB16> monochromeImage = GrayImagesToMonoColorConverter.convert(image1, Color.Green);
        RGB16 color = _getPixel(monochromeImage, new long[] { 0, 0 });

        assertTrue(color.getB() == 0 && color.getG() == 255 && color.getR() == 0);
    }

    @Test
    public void merge_CreateRed3DImage_ReturnsCorrectImage() {
        IMetadata metadata1 = new Metadata.MetadataBuilder(new long[] { 2, 1, 2 }).bitPerPixel(PixelTypes.UINT_16)
                .build();

        Image<UINT16> image1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        long[] position0 = { 0, 0, 0 };
        _setPixel(image1, position0, 0);

        long[] position1 = { 1, 0, 0 };
        _setPixel(image1, position1, UINT16.MAX_VAL);

        long[] position2 = { 0, 0, 1 };
        _setPixel(image1, position2, UINT16.MAX_VAL);

        IPixelRandomAccess<RGB16> monochromeImage = GrayImagesToMonoColorConverter.convert(image1, Color.Red);
        RGB16 color0 = _getPixel(monochromeImage, position0);
        assertTrue(color0.getB() == 0 && color0.getG() == 0 && color0.getR() == 0);

        RGB16 color1 = _getPixel(monochromeImage, position1);
        assertTrue(color1.getB() == 0 && color1.getG() == 0 && color1.getR() == 255);

        RGB16 color2 = _getPixel(monochromeImage, position2);
        assertTrue(color2.getB() == 0 && color2.getG() == 0 && color2.getR() == 255);
    }

    private void _setPixel(Image<UINT16> image, long[] position, int value) {
        IPixelRandomAccess<UINT16> ra = image.getRandomAccess();
        ra.setPosition(position);
        ra.getPixel();

        Pixel<UINT16> pixel = new Pixel<>(new UINT16(value));
        ra.setPixel(pixel);
    }

    private RGB16 _getPixel(IPixelRandomAccess<RGB16> ra, long[] position) {
        ra.setPosition(position);

        return ra.getPixel().value();
    }
}