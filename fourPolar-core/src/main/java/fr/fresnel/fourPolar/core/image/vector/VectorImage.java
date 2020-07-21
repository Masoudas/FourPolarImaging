package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * An interface for defining a vector image, that can be stored as an SVG image.
 * 
 */
public interface VectorImage {
    /**
     * @return get metadata of the image.
     */
    public IMetadata metadata();

    /**
     * @return get the factory associated with this image.
     */
    public VectorImageFactory getFactory();

    /**
     * Adds the given composite filter as a child of svg defs element of all the
     * planes in this image.
     * 
     * @param composite is the composite filter.
     */
    public void addFilterComposite(FilterComposite composite);

    /**
     * Puts the given image inside the given vector image. Note that with this
     * method, the pixels are set as is (i.e as bytes). Note that the vector
     * image will be independent of the image interface. That is, later changes in
     * the image interface does not affect the background image.
     * 
     * @throws IllegalArgumentException in case the given image does not have same
     *                                  dimension as this vector image.
     */
    public <T extends PixelType> void setImage(Image<T> image, T pixelType);

    /**
     * Adds a vector to a plane of this image.
     * <p>
     * Note that the vector will be written in the coordinate specified by its
     * underlying shape. for example, if the underlying shape is a line form [0,0,0]
     * to [1,1,0], then this vector will be added to the [x,x,0] plane, as a line
     * from [0,0] to [1,1].
     * <p>
     * It's the responsibility of the caller to ensure that the underlying vector is
     * a sensible vector presentation in a plane (for example, a box from [0,0,0] to
     * [0,1,1]) would be written as a box from [0,0] to [0,1] in the first plane).
     * 
     * @param vector is the vector to be added to this image.
     * @throws IllegalArgumentException if the shape dimension is not equal to image
     *                                  dimension.
     */
    public void addVector(Vector vector);

}