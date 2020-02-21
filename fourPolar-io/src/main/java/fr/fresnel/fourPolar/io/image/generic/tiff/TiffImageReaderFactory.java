package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoReaderFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.float32ImgLib2TiffImageReader;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.uint16ImgLib2TiffImageReader;

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
    public static <T extends PixelType> ImageReader<T> getReader(ImageFactory<T> factory, T pixelType)
            throws NoReaderFoundForImage {
        ImageReader<T> reader;

        if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == Type.UINT_16){
            reader = (ImageReader<T>) new uint16ImgLib2TiffImageReader((ImgLib2ImageFactory<UINT16>)factory);
        }
        else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == Type.FLOAT_32){
            reader = (ImageReader<T>) new float32ImgLib2TiffImageReader((ImgLib2ImageFactory<Float32>)factory); 
        } 
        else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == Type.RGB_16){
            reader = null;
        }
        else{
            throw new NoReaderFoundForImage();
        }
        
        return reader;
    }

}