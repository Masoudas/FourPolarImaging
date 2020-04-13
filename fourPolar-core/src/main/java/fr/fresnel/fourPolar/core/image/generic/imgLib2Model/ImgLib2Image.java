package fr.fresnel.fourPolar.core.image.generic.imgLib2Model;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.types.TypeConverter;
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

    private final TypeConverter<U, V> _tConverter;

    private final ImageFactory _factory;

    /**
     * This constructor is works as a wrapper from ImgLib2 type to our type.
     * 
     * @param img        is the ImgLib2 interface.
     * @param tConverter is the appropriate converter from ImgLib2 type to
     *                   {@link PixelType}.
     * @param factory    is the associated {@link ImageFactory}.
     */
    ImgLib2Image(final Img<V> img, final TypeConverter<U, V> tConverter, final ImageFactory factory)
            throws ConverterNotFound {
        this._img = img;
        this._tConverter = tConverter;
        this._factory = factory;
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
        return this._tConverter.getPixelType();
    }

    @Override
    public String toString() {
        return _img.toString();
    }

    /**
     * This method returns the {@link Img} interface associated with this
     * implementation.
     */
    public Img<V> getImg() {
        return this._img;
    }

    @Override
    public ImageFactory getFactory() {
        return _factory;
    }

}