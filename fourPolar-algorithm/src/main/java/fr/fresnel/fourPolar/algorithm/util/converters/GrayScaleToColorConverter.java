package fr.fresnel.fourPolar.algorithm.util.converters;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;

/**
 * Using this class, we can convert GrayScale types (e.g, {@link UINT16}) to
 * color types {@link RGB16}.
 */
public class GrayScaleToColorConverter {
    /**
     * Convertes an {@link UINT16} to an {@link RGB16} image.
     * 
     * @throws ConverterToImgLib2NotFound
     */
    public static Image<RGB16> convertImage(Image<UINT16> image) throws ConverterToImgLib2NotFound {
        // We use ImgLib2 classes, i.e, we convert to Img. Then we convert the Image.
        // Finaly, we create a new Image instance and fill it, and return it.
        Img<UnsignedShortType> imgLib2Model = ImageToImgLib2Converter.getImg(image, UINT16.zero());

        image.getFactory().create(image.getDimensions(), UINT16.zero());

    }
}