package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types.ConverterNotFound;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverter;
import fr.fresnel.fourPolar.core.image.generic.imglib2Model.types.TypeConverterFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Type;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.NativeType;

/**
 * Implementation of {@code Image} for the ImgLib2 image.
 * 
 * @param <U>
 * @param <V>
 */
class ImgLib2Image<U extends PixelType, V extends NativeType<V>> implements Image<U>, Img<V> {
    private final Img<V> _img;

    private final TypeConverter<V> _tConverter;

    private final U _pixelType;

    /**
     * This constructor is works as a wrapper from ImgLib2 type to our type.
     * 
     * @param img       is the ImgLib2 interface.
     * @param type      is the data type of ImgLib2.
     * @param pixelType is the pixel type associated with the {@code Image}.
     * @throws ConverterNotFound is thrown in case conversion to our data types is
     *                           not supported.
     */
    public ImgLib2Image(final Img<V> img, V type, U pixelType) throws ConverterNotFound {
        this._img = img;
        this._tConverter = TypeConverterFactory.create(type);
        this._pixelType = pixelType;
    }

    @Override
    public RandomAccess<V> randomAccess() {
        return this._img.randomAccess();
    }

    @Override
    public RandomAccess<V> randomAccess(Interval interval) {
        return this._img.randomAccess(interval);
    }

    @Override
    public int numDimensions() {
        return this._img.numDimensions();
    }

    @Override
    public long min(int d) {
        return this._img.min(d);
    }

    @Override
    public long max(int d) {
        return this._img.max(d);
    }

    @Override
    public Cursor<V> cursor() {
        return this._img.cursor();
    }

    @Override
    public Cursor<V> localizingCursor() {
        return this._img.localizingCursor();
    }

    @Override
    public long size() {
        return this._img.size();
    }

    @Override
    public V firstElement() {
        return this._img.firstElement();
    }

    @Override
    public Object iterationOrder() {
        return this._img.iterationOrder();
    }

    @Override
    public Iterator<V> iterator() {
        return this._img.iterator();
    }

    @Override
    public ImgFactory<V> factory() {
        return _img.factory();
    }

    @Override
    public Img<V> copy() {
        return this._img.copy();
    }

    @Override
    public long[] getDims() {
        long[] dim = new long[this._img.numDimensions()];

        this._img.dimensions(dim);
        return dim;
    }

    @Override
    public IPixelRandomAccess getRandomAccess() {
        return new ImgLib2RandomAccess<V>(this._img.randomAccess(), this._tConverter);
    }

    @Override
    public IPixelCursor getCursor() {
        return new ImgLib2PixelCursor<V>(this._img.cursor(), this.numDimensions(), this._tConverter);
    }

    @Override
    public Type getPixelType() {
        return _pixelType.getType();
    }

}