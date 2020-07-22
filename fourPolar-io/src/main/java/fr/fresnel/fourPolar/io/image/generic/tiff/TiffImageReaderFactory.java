package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIORGB16TiffReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffReader;

/**
 * A factory to get a proper writer for the given implementation of
 * {@code Image}.
 */
public class TiffImageReaderFactory {
    /**
     * A factory to get a proper reader. The reader would be the proper reader for
     * the given implementation of {@code ImageFactory}.
     * 
     * @param <T>     is the {@code PixelType}
     * @param factory is the factory for {@code Image}
     * 
     * @throws IllegalArgumentException in case no reader is found for the given
     *                                  image type.
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> ImageReader<T> getReader(ImageFactory factory, T pixelType) {
        ImageReader<T> reader;

        if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.UINT_16) {
            reader = (ImageReader<T>) new SCIFIOUINT16TiffReader((ImgLib2ImageFactory) factory);
        } else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.FLOAT_32) {
            reader = (ImageReader<T>) new SCIFIOFloat32TiffReader((ImgLib2ImageFactory) factory);
        } else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.ARGB_8) {
            // TODO: Which reader here?
            reader = (ImageReader<T>) new SCIFIORGB16TiffReader((ImgLib2ImageFactory) factory);
        } else {
            throw new IllegalArgumentException("No reader was found for the given image type.");
        }

        return reader;
    }

}