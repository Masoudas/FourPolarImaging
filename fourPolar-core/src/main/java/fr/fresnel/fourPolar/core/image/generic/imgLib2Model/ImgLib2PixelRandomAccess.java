package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.RandomAccess;
import net.imglib2.type.NativeType;

/**
 * Implementation of {@code IPixelRandomAccess} for the ImgLib2 image.
 * 
 * @param <T> extens {@link PixelType}.
 * @param <T> is the generic native type of ImgLib2. Note that only a handful of
 *            datatypes are supported. See {@code TypeConverterFactory} .
 * 
 */
class ImgLib2PixelRandomAccess<U extends PixelType, T extends NativeType<T>> implements IPixelRandomAccess<U> {
    final private RandomAccess<T> _rAccess;
    final private TypeConverter<U, T> _tConverter;
    final private IPixel<U> _pixel;

    /**
     * Implementation of {@code IPixelRandomAccess} for the ImgLib2 image.
     * 
     * @param randomAccess is the RandomAccess class of ImgLib2.
     * @param converter    is the converter between ImgLib2 data types and our data
     *                     types.
     */
    public ImgLib2PixelRandomAccess(final RandomAccess<T> randomAccess, final TypeConverter<U, T> converter) {
        _rAccess = randomAccess;
        _tConverter = converter;
        _pixel = new Pixel<U>((U)converter.getPixelType().create(converter.getPixelType()));
    }

    @Override
    public void setPosition(long[] position) {
        this._rAccess.setPosition(position);
    }

    @Override
    public void setPixel(IPixel<U> pixel) throws ArrayIndexOutOfBoundsException {
        try {
            this._tConverter.setNativeType(pixel.value(), this._rAccess.get());
        } catch (Exception e) {
            throw new ArrayIndexOutOfBoundsException("The given pixel position does not exist");
        }

    }

    @Override
    public IPixel<U> getPixel() throws ArrayIndexOutOfBoundsException {
        try {
            this._tConverter.setPixelType(this._rAccess.get(), _pixel.value());
            return _pixel;
        } catch (Exception e) {
            throw new ArrayIndexOutOfBoundsException("The given pixel position does not exist");
        }
    }

}