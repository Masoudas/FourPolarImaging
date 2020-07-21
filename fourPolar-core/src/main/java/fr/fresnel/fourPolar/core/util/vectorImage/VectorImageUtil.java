package fr.fresnel.fourPolar.core.util.vectorImage;

import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
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
     * are of type T. Checks backend plane types by trying to cast to type T.
     * @param <T> is the expected backend plane type of the image.
     * @param vectorImage is the vector image.
     * @param t is the class of T.
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasBackEndPlanes(VectorImage vectorImage, Class<T> t){
        if (!(vectorImage instanceof ImagePlaneAccessor<?>)){
            return false;
        }

        try{
            T t_prime = (T)((ImagePlaneAccessor<?>)vectorImage).getImagePlane(1).getPlane();
            return true;
        } catch (ClassCastException e){
            
        }
        
        return false;
    }
}