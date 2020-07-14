package fr.fresnel.fourPolar.core.image.vector;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * An interface for defining a vector image, that can be stored as an SVG image.
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
     * @return the random access associated with this image.
     */
    public VectorRandomAccess randomAccess();

    /**
     * Sets the background color of the image. i.e, all pixels are set to the given
     * color.
     */
    public void setBackground(ARGB8 color);

    /**
     * Adds the given composite filter as a child of svg defs element.
     * 
     * @param composite is the composite filter.
     */
    public void addFilterComposite(FilterComposite composite);

    /**
     * Puts the given image inside the given vector image. Note that with this
     * method, the pixels are set as is (i.e as bytes).
     * 
     * @throws IllegalArgumentException in case the given image does not have same
     *                                  axis order and dimension as this vector
     *                                  image.
     */
    public <T extends PixelType> void setImage(Image<T> image, T pixelType);

}