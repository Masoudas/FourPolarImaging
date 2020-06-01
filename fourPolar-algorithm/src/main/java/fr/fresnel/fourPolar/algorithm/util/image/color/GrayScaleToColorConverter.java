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
    public static <T extends RealType> Image<RGB16> useMaxEachPlane(final Image<T> grayImage)
            throws ConverterToImgLib2NotFound {
        return MaxPlaneGrayScaleToColorConverter.convert(grayImage);
    }
}