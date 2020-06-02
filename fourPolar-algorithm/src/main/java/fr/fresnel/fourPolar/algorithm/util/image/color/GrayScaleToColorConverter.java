package fr.fresnel.fourPolar.algorithm.util.image.color;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link RGB16}.
 */
public class GrayScaleToColorConverter {
    public enum Color {
        Red, Green, Blue
    };

    /**
     * Convertes the given channel of {@link UINT16} image to an {@link RGB16}
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
    public static <T extends RealType> Image<RGB16> colorUsingMaxEachPlane(final Image<T> grayImage)
            throws ConverterToImgLib2NotFound {
        return MaxPlaneGrayScaleToColorConverter.convert(grayImage);
    }

    /**
     * Merge to images, by coloring each image with a single color from
     * {@link Color}.
     * 
     * @param image1 is the first image.
     * @param color1 is the mono color for image 1.
     * @param image2 is the second image.
     * @param color2 is the mono color for image 1.
     * @return is the merged colord images.
     */
    public static Image<RGB16> mergeAsMonoColor(Image<UINT16> image1, Color color1, Image<UINT16> image2, Color color2) {
        return GrayImagesToMonoColorMerger.convert(image1, color1, image2, color2);
    }
}