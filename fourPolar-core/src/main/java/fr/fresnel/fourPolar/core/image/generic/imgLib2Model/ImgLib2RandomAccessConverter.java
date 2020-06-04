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
      public static IPixelRandomAccess<RGB16> convertARGBType(RandomAccess<ARGBType> ra) {
            TypeConverter<RGB16, ARGBType> typeConverter = null;
            try {
                  typeConverter = TypeConverterFactory.getConverter(RGB16.zero(), new ARGBType());
            } catch (ConverterNotFound e) {
                  // Never caught.
            }
            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }

      public static IPixelRandomAccess<UINT16> convertUnsignedShortType(RandomAccess<UnsignedShortType> ra) {
            TypeConverter<UINT16, UnsignedShortType> typeConverter = null;
            try {
                  typeConverter = TypeConverterFactory.getConverter(UINT16.zero(), new UnsignedShortType());
            } catch (ConverterNotFound e) {
                  // Never caught
            }

            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }

      public static IPixelRandomAccess<Float32> convertFloat32Type(RandomAccess<FloatType> ra) {
            TypeConverter<Float32, FloatType> typeConverter = null;
            try {
                  typeConverter = TypeConverterFactory.getConverter(Float32.zero(), new FloatType());
            } catch (ConverterNotFound e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
            }
            return new ImgLib2PixelRandomAccess<>(ra, typeConverter);
      }
}