package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.RandomAccess;
import net.imglib2.type.NativeType;

/**
 * Implementation of {@code IPixelRandomAccess} for the ImgLib2 image.
 * 
 * @param <T> is the generic native type of ImgLib2. Note that only a handful of
 *            datatypes are supported. See {@code TypeConverterFactory} .
 */
class ImgLib2RandomAccess<T extends NativeType<T>> implements IPixelRandomAccess {
    final private RandomAccess<T> _rAccess;
    final private TypeConverter<T> _tConverter;

    /**
     * Implementation of {@code IPixelRandomAccess} for the ImgLib2 image.
     * 
     * @param randomAccess is the RandomAccess class of ImgLib2.
     * @param converter is the converter between ImgLib2 data types and our data types.
     */
    public ImgLib2RandomAccess(final RandomAccess<T> randomAccess, final TypeConverter<T> converter) {
        _rAccess = randomAccess;
        _tConverter = converter;
    }

    @Override
    public void setPosition(long[] position) {
        this._rAccess.setPosition(position);
    }

    @Override
    public void setPixel(IPixel<PixelType> pixel) {
        this._tConverter.setNativeType(pixel.value(), this._rAccess.get());
    }

    @Override
    public IPixel<PixelType> getPixel() {
        PixelType pixel = this._tConverter.getPixel(this._rAccess.get());
        return new Pixel<PixelType>(pixel);
    }

}