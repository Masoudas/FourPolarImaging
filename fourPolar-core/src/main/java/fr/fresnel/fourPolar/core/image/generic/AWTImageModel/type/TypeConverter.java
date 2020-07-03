package fr.fresnel.fourPolar.core.image.generic.AWTImageModel.type;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An interface for converting a {@link PixelType} to an RGB integer value suitable
 * for {@link BufferedImage}.
 * 
 * @param <T> is a pixel type.
 */
public interface TypeConverter<T extends PixelType> {
    /**
     * Sets the value of the given pixel type with the equivalent of the value in
     * the buffered image.
     * 
     * @param pixelType is the pixel type
     * @param value     is the RGB pixel value of Buffered image.
     */
    public void setPixelType(T pixelType, int rgb);

    /**
     * Returns the equivalent RGB int of the buffered image from a pixel type.
     * 
     * @param pixelType is the pixel type.
     * @return is the int equivalent of the pixel suitable for buffered image.
     */
    public int getRGB(T pixelType);

}