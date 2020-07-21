package fr.fresnel.fourPolar.core.image.vector.batikModel;

import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;

public class BatikVectorImageFactory implements VectorImageFactory {

    @Override
    public VectorImage create(IMetadata metadata) {
        return new BatikVectorImage(metadata, this);
    }

    /**
     * Create a vector image by directly providing the planes as SVG document
     * instances. Each plane must have its size defind in the document element using
     * the with and height attribute. The position of a plane in the array plus one
     * is considered as its plane index. @see {@link PlanarImageModel} for how the
     * plane index and plane coordinates are related.
     * 
     * 
     * @param metadata is the metadata corresponding to the given planes.
     * @param planes   are the planes of the image as an array.
     * @return a concrete vector image instance.
     * 
     * @throws IllegalArgumentException if plane size of a plane is not equal to
     *                                  plane size specified by metadata, or the
     *                                  number of planes is not equal to the number
     *                                  of planes specified by metadata.
     */
    public VectorImage createFromPlanes(IMetadata metadata, SVGDocument[] planes) {
        return new BatikVectorImage(metadata, this, planes);
    }

}