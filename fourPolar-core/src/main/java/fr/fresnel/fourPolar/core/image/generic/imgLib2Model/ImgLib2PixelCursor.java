package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
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
    final private TypeConverter<U, T> _tConverter;
    final private long _size;
    final private IPixel<U> _pixel;

    /**
     * Creates a cursor, which wraps the behavior of the Cursor. Note that only one
     * instance of {@link IPixel} and {@link IPixelType} are created for each
     * iteration.
     * 
     * @param cursor     is the cursor of the ImgLib2.
     * @param ndim       is the dimension of the associated image.
     * @param cursor     is the ImgLib2 cursor.
     * @param imageDim   is the dimension of the underlying image.
     * @param converter is the appropriate converter (@see TypeConverter).
     */
    @SuppressWarnings("unchecked")
    public ImgLib2PixelCursor(final Cursor<T> cursor, final long[] imageDim, final TypeConverter<U, T> converter) {
        this._cursor = cursor;
        this._position = new long[imageDim.length];
        this._tConverter = converter;
        this._size = _computeSizeOfCursor(imageDim);
        U pixelValue = (U)PixelTypes.create(converter.getPixelType());
        this._pixel = new Pixel<U>(pixelValue);

    }

    @Override
    public boolean hasNext() {
        return this._cursor.hasNext();
    }

    @Override
    public IPixel<U> next() {
        _tConverter.setPixelType(this._cursor.next(), this._pixel.value());

        return _pixel;
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

    private long _computeSizeOfCursor(long[] imageDim) {
        long size = 1;

        for (long dim : imageDim) {
            size *= dim;
        }
        return size;
    }

    @Override
    public long size() {
        return this._size;
    }

}