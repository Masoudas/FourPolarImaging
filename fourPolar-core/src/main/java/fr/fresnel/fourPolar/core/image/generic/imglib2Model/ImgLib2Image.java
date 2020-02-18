package fr.fresnel.fourPolar.core.image.generic.imglib2Model;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.image.generic.IPixelIterator;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.type.NativeType;


class ImgLib2Image<U, V extends NativeType<V>> implements Image<U>, Img<V> {
    /**
     * This is the threshold for the number of pixels, after which we opt
     * for a cell image rather than an array image.
     * This is roughly 256MB for an image of type float.
     */
    private static long _staticImageThr = 256 * 256 * 1024; 

    private final Img<V> _img;

    public ImgLib2Image(final Img<V> img) {
        _img = img;
    }

    public ImgLib2Image(final long[] dim, V v) {
        long nPixels = _getNPixels(dim);

        if (nPixels < _staticImageThr){
            _img = new ArrayImgFactory<V>(v).create(dim);
        }
        else{
            _img = new CellImgFactory<V>(v).create(dim);
        }
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
    public IPixelIterator getIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPixelRandomAccess getRandomAccess() {
        // TODO Auto-generated method stub
        return null;
    }

    private long _getNPixels(long[] dim) {
        long nPixels = 1;
        for (long size : dim) {
            nPixels = nPixels * size;
        }

        return nPixels;
    }

}