package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import net.imglib2.RandomAccess;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Converts a random access of ImgLib2 to a {@link IPixelRandomAccess}. 
 */
public class ImgLib2RandomAccessConverter {
      public static IPixelRandomAccess<RGB16> convertARGBType(RandomAccess<ARGBType> ra) throws ConverterNotFound {
            TypeConverter<RGB16, ARGBType> typeConverter = TypeConverterFactory.getConverter(RGB16.zero(),
                        new ARGBType());
            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }

      public static IPixelRandomAccess<UINT16> convertUnsignedShortType(RandomAccess<UnsignedShortType> ra)
                  throws ConverterNotFound {
            TypeConverter<UINT16, UnsignedShortType> typeConverter = TypeConverterFactory.getConverter(UINT16.zero(),
                        new UnsignedShortType());
            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }

      public static IPixelRandomAccess<Float32> convertFloat32Type(RandomAccess<FloatType> ra)
                  throws ConverterNotFound {
            TypeConverter<Float32, FloatType> typeConverter = TypeConverterFactory.getConverter(Float32.zero(),
                        new FloatType());
            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }
}