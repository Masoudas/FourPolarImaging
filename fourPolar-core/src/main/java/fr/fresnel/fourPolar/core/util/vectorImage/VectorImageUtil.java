package fr.fresnel.fourPolar.core.util.vectorImage;

import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;

/**
 * Provides a set of static utility methods for {@link VectorImage}.
 */
public class VectorImageUtil {
    private VectorImageUtil(){
        throw new AssertionError();
    }

    /**
     * Returns true if the given vector image implements {@link ImagePlaneAccessor} and that its backend planes
     * are of type T.
     * @param <T> is the expected backend plane type of the image.
     * @param vectorImage is the vector image.
     * @param t is the class of T.
     */
    public static <T> boolean hasBackEndPlanes(VectorImage vectorImage, Class<T> t){
        boolean implementsPlaneAccessor = vectorImage instanceof ImagePlaneAccessor<?>;
        boolean hasTAsBackend = ((ImagePlaneAccessor<?>)vectorImage).getImagePlane(1).getClass() == t;
        
        return implementsPlaneAccessor && hasTAsBackend;
    }
}