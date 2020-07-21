package fr.fresnel.fourPolar.core.image.vector.batikModel.accessors;

import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;

/**
 * An static class for accessing the planes of a batik image.
 */
public class BatikImagePlaneAccessor {
    private BatikImagePlaneAccessor(){
        throw new AssertionError();
    }

    /**
     * Returns the plane accessor by direct cast of a batik image.
     * @param batikImage is batik vector image.
     * @return
     * 
     * @throws ClassCastException if the given vector image is not batik.
     */
    @SuppressWarnings("unchecked")
    public static ImagePlaneAccessor<SVGDocument> get(VectorImage batikImage) {
        if (!(batikImage.getFactory() instanceof BatikVectorImageFactory)){
            throw new ClassCastException("The given vector image is not batik.");
        }

        return (ImagePlaneAccessor<SVGDocument>)batikImage;
    }
}