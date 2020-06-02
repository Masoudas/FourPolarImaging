package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.ImageJ1.ImageJ1RGB16TiffWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOFloat32TiffWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.SCIFIOUINT16TiffWriter;

/**
 * A factory to get a proper writer for the given implementation of
 * {@code Image}.
 */
public class TiffImageWriterFactory {
    /**
     * A factory to get a proper writer for the given implementation of
     * {@code Image}. The writers can be used to write different instances of image.
     * 
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> ImageWriter<T> getWriter(Image<T> image, T pixelType)
            throws NoWriterFoundForImage {
        ImageWriter<T> writer;

        if (image.getFactory() instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.UINT_16) {
            writer = (ImageWriter<T>) new SCIFIOUINT16TiffWriter();
        } else if (image instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.FLOAT_32) {
            writer = (ImageWriter<T>) new SCIFIOFloat32TiffWriter();
        } else if (image instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.RGB_16) {
            writer = (ImageWriter<T>) new ImageJ1RGB16TiffWriter();
        } else {
            throw new NoWriterFoundForImage();
        }

        return writer;
    }

    /**
     * A factory to get a proper writer for a given {@code ImageFactory}. The
     * writers can be used to write different instances of image.
     * 
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> ImageWriter<T> getWriter(ImageFactory factory, T pixelType)
            throws NoWriterFoundForImage {
        ImageWriter<T> writer;

        if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.UINT_16) {
            writer = (ImageWriter<T>) new SCIFIOUINT16TiffWriter();
        } else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.FLOAT_32) {
            writer = (ImageWriter<T>) new SCIFIOFloat32TiffWriter();
        } else if (factory instanceof ImgLib2ImageFactory && pixelType.getType() == PixelTypes.RGB_16) {
            writer = (ImageWriter<T>) new ImageJ1RGB16TiffWriter();
        } else {
            throw new NoWriterFoundForImage();
        }

        return writer;
    }

}