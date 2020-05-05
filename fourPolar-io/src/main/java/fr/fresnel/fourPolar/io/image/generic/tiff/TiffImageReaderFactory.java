package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.Float32ImgLib2TiffImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.RGB16ImgLib2TiffImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.UINT16ImgLib2TiffImageReader;

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
     * @return
     * @throws NoReaderFoundForImage
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> ImageReader<T> getReader(ImageFactory factory, T pixelType)
            throws NoReaderFoundForImage {
        ImageReader<T> reader;

        if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.UINT_16){
            reader = (ImageReader<T>) new UINT16ImgLib2TiffImageReader((ImgLib2ImageFactory)factory);
        }
        else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.FLOAT_32){
            reader = (ImageReader<T>) new Float32ImgLib2TiffImageReader((ImgLib2ImageFactory)factory); 
        } 
        else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.RGB_16){
            reader = (ImageReader<T>) new RGB16ImgLib2TiffImageReader((ImgLib2ImageFactory)factory); 
        }
        else{
            throw new NoReaderFoundForImage();
        }
        
        return reader;
    }

}