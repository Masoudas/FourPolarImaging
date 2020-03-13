package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import net.imglib2.img.Img;
import net.imglib2.type.NativeType;

/**
 * Implementation of {@code Image} for the ImgLib2 image.
 * 
 * @param <U> is our pixel data type.
 * @param <V> is the ImgLib2 data type.
 */
public class ImgLib2Image<U extends PixelType, V extends NativeType<V>> implements Image<U> {
    private final Img<V> _img;

    private final TypeConverter<V> _tConverter;

    private final Type _pixelType;

    /**
     * This constructor is works as a wrapper from ImgLib2 type to our type.
     * 
     * @param img       is the ImgLib2 interface.
     * @param type      is the data type of ImgLib2.
     * @throws ConverterNotFound is thrown in case conversion to our data types is
     *                           not supported.
     */
    ImgLib2Image(final Img<V> img, V type) throws ConverterNotFound {
        this._img = img;
        this._tConverter = TypeConverterFactory.getConverter(type);
        this._pixelType = _tConverter.getPixelType(type).getType();
    }

    @Override
    public long[] getDimensions() {
        long[] dim = new long[this._img.numDimensions()];

        this._img.dimensions(dim);
        return dim;
    }

    @Override
    public IPixelRandomAccess<U> getRandomAccess() {
        return new ImgLib2PixelRandomAccess<U, V>(this._img.randomAccess(), this._tConverter);
    }

    @Override
    public IPixelCursor<U> getCursor() {
        return new ImgLib2PixelCursor<U, V>(this._img.cursor(), this.getDimensions(), this._tConverter);
    }

    @Override
    public Type getPixelType() {
        return _pixelType;
    }

    @Override
    public String toString() {
       return _img.toString();
    }

    /**
     * This method returns the {@link Img} interface associated with this implementation.
     */
    public Img<V> getImg() {
        return this._img;
    }

}