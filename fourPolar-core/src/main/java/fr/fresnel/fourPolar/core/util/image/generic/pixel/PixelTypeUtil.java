package fr.fresnel.fourPolar.core.util.image.generic.pixel;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;

/**
 * A set of utility methods for the {@PixelType}
 */
public class PixelTypeUtil {
    private PixelTypeUtil(){
        throw new AssertionError();
    }

    /**
     * @return the pixel type of the image
     */
    public static <T extends PixelType> PixelType getPixelType(Image<T> image){
        PixelTypes type = image.getCursor().next().value().getType();
        return PixelTypes.create(type);
    }
}