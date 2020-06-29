package fr.fresnel.fourPolar.algorithm.util.image.color;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link ARGB8}.
 */
public class GrayScaleToColorConverter {
    public enum Color {
        Red, Green, Blue
    };

    /**
     * Convertes the given channel of {@link UINT16} image to an {@link ARGB8}
     * image. Note that an 8 bit lookup table is used for the conversion, hence
     * there are only 256 white pixels. Note that each image plane is scaled with
     * respect to it's minimum and maximum (not the maximum of the entire image.).
     * If a plane has no maximum, all pixels will be black.
     * 
     * 
     * @throws ConverterToImgLib2NotFound if the image model can't be converted to
     *                                    ImgLib2 model.
     * 
     */
    public static <T extends RealType> Image<ARGB8> colorUsingMaxEachPlane(final Image<T> grayImage)
            throws ConverterToImgLib2NotFound {
        return MaxPlaneGrayScaleToColorConverter.convert(grayImage);
    }

    /**
     * Returns a mono color view of the given image. Note that no new image instance
     * is created with this method.
     * 
     * @param image is the first image.
     * @param color is the mono color for image 1.
     * @return a random accessible mono color view of the gray image.
     */
    public static IPixelRandomAccess<ARGB8> createMonochromeView(Image<UINT16> image, Color color) {
        return GrayImagesToMonoColorConverter.convert(image, color);
    }
}