package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import net.imglib2.Cursor;
import net.imglib2.type.NativeType;

/**
 * Implementation of {@code IPixelCursor} for the ImgLib2 image.
 * 
 * @param <T> is the generic native type of ImgLib2. Note that only a handful of
 *            datatypes are supported. See {@code TypeConverterFactory} .
 * 
 */
class ImgLib2PixelCursor<U extends PixelType, T extends NativeType<T>> implements IPixelCursor<U> {
    final private Cursor<T> _cursor;
    private long[] _position;
    final private TypeConverter<T> _tConverter;

    /**
     * 
     * @param cursor is the cursor of the ImgLib2.
     * @param ndim   is the
     * @throws ConverterNotFound
     */
    public ImgLib2PixelCursor(final Cursor<T> cursor, final int ndim, final TypeConverter<T> tConverter){
        this._cursor = cursor;
        this._position = new long[ndim];
        this._tConverter = tConverter;
    }

    @Override
    public boolean hasNext() {
        return this._cursor.hasNext();
    }

    @Override
    public IPixel<U> next() {
        PixelType pixelValue = _tConverter.getPixelType(this._cursor.next());
        return new Pixel<U>((U)pixelValue);
    }

    @Override
    public long[] localize() {
        this._position = this._position.clone();
        this._cursor.localize(this._position);
        return this._position;
    }

    @Override
    public void setPixel(IPixel<U> pixel) {
        _tConverter.setNativeType(pixel.value(), this._cursor.get());
    }

    @Override
    public void reset() {
        this._cursor.reset();

    }

}