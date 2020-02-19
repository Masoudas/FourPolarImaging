package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverter;
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
public class ImgLib2PixelCursor<T extends NativeType<T>> implements IPixelCursor {
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
    public IPixel<PixelType> next() {
        PixelType pixelValue = _tConverter.getPixel(this._cursor.next());
        return new Pixel<PixelType>(pixelValue);
    }

    @Override
    public long[] localize() {
        this._cursor.localize(this._position);
        return this._position;
    }

    @Override
    public void setPixel(IPixel<PixelType> pixel) {
        _tConverter.setNativeType(pixel.value(), this._cursor.get());
    }

}