package fr.fresnel.fourPolar.io.image.vector.svg;

import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.io.image.vector.VectorImageReader;
import fr.fresnel.fourPolar.io.image.vector.svg.batik.BatikSVGVectorImageReader;

/**
 * An static factory for creating SVG readers.
 */
public class SVGVectorImageReaderFactory {
    private SVGVectorImageReaderFactory() {

    }

    /**
     * Create a reader for the given vector image (factory) type.
     * 
     * @param factory is the factory that creates the given vector image.
     * @return an instance of vector image reader.
     * 
     * @throws IllegalArgumentException in case no reader implementation is found
     *                                  for the given type.
     */
    public static VectorImageReader create(VectorImageFactory factory) {
        if (factory instanceof BatikVectorImageFactory) {
            return new BatikSVGVectorImageReader();
        } else {
            throw new IllegalArgumentException("No reader is implemented for the given vector image type.");
        }
    }
}