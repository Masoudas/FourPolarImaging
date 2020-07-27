package fr.fresnel.fourPolar.io.image.vector.svg;

import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.BatikVectorImageFactory;
import fr.fresnel.fourPolar.io.image.vector.VectorImageWriter;
import fr.fresnel.fourPolar.io.image.vector.svg.batik.BatikSVGVectorImageWriter;

/**
 * An static factory for creating SVG writers.
 */
public class SVGVectorImageWriterFactory {
    private SVGVectorImageWriterFactory() {

    }

    /**
     * Create a writer for the given vector image (factory) type.
     * 
     * @param factory is the factory that creates the given vector image.
     * @return an instance of vector image writer.
     * 
     * @throws IllegalArgumentException in case no writer implementation is found
     *                                  for the given type.
     */
    public static VectorImageWriter create(VectorImageFactory factory) {
        if (factory instanceof BatikVectorImageFactory) {
            return new BatikSVGVectorImageWriter();
        } else {
            throw new IllegalArgumentException("No reader is implemented for the given vector image type.");
        }

    }

}