package fr.fresnel.fourPolar.core.util.image;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * A set of static utility methods for {@link Image}.
 */
public class ImageUtil {
    private ImageUtil(){
        throw new AssertionError();
    }

    /**
     * Copies the source image to destination image, using the cursor of the source and the
     * random acess of the destination.
     * 
     * @param <T> is the pixel type
     * @param srcImage is the source image
     * @param destImage is the destination image.
     */
	public static <T extends PixelType> void copy(Image<T> srcImage, Image<T> destImage) {
	    IPixelRandomAccess<T> destImageRA = destImage.getRandomAccess();
	    for (IPixelCursor<T> srcCursor = srcImage.getCursor(); srcCursor.hasNext();) {
	        IPixel<T> pixel = srcCursor.next();
	
	        destImageRA.setPosition(srcCursor.localize());
	        destImageRA.setPixel(pixel);
	    }
	}
    
}