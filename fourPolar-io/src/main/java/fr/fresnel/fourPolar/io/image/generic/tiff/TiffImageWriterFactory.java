package fr.fresnel.fourPolar.io.image.generic.tiff;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import fr.fresnel.fourPolar.io.exceptions.image.generic.NoWriterFoundForImage;
import fr.fresnel.fourPolar.io.image.generic.ImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.float32ImgLib2TiffImageWriter;
import fr.fresnel.fourPolar.io.image.generic.tiff.scifio.uint16ImgLib2TiffImageWriter;
import net.imglib2.img.Img;

/**
 * A factory to get a proper writer for the given implementation of
 * {@code Image}.
 */
public class TiffImageWriterFactory {
    /**
     * A factory to get a proper writer for the given implementation of
     * {@code Image}. The writers can be used to write different instances of image.
     * 
     * @param <T>
     * @param image
     * @return
     */
    public static <T extends PixelType> ImageWriter<T> getWriter(Image<T> image, T pixelType) throws NoWriterFoundForImage{
        ImageWriter<T> writer;

        if (image instanceof Img && pixelType.getType() == Type.UINT_16){
            writer = (ImageWriter<T>)new uint16ImgLib2TiffImageWriter();
        }
        else if (image instanceof Img && pixelType.getType() == Type.FLOAT_32){
            writer = (ImageWriter<T>)new float32ImgLib2TiffImageWriter();
        }
        else if (image instanceof Img && pixelType.getType() == Type.RGB_16){
            writer = null;
        }
        else{
            throw new NoWriterFoundForImage();
        }
        
        return writer;
    }

}