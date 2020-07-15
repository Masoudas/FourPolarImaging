package fr.fresnel.fourPolar.core.util.image;

import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * A set of static utility methods for {@link Image}.
 */
public class ImageUtil {
    private ImageUtil() {
        throw new AssertionError();
    }

    /**
     * Copies the source image to destination image, using the cursor of the source
     * and the random acess of the destination.
     * 
     * @param <T>       is the pixel type
     * @param srcImage  is the source image
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

    /**
     * Creates a cursor over the demaned plane.
     * 
     * @param <T>        is the pixel type of the image.
     * @param image      is the desired image.
     * @param planeIndex is the plane index. @see {@link MetadataUtil#getNPlanes()}.
     *                   index should start from 1.
     * @return cursor over the demanded plane.
     * 
     * @throws IndexOutOfBoundsException if the image is 1D, or if the planeIndex is
     *                                   less than one or greater than number of
     *                                   planes.
     */
    public static <T extends PixelType> IPixelCursor<T> getPlaneCursor(Image<T> image, int planeIndex) {
        long[][] planeCoords = MetadataUtil.getPlaneCoordinates(image.getMetadata(), planeIndex);

        long[] len = IntStream.range(0, planeCoords[0].length).mapToLong(i -> planeCoords[1][i] - planeCoords[0][i] + 1)
                .toArray();
        return image.getCursor(planeCoords[0], len);
    }

}