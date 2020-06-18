package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Wraps an interval {@link IPixelCursor<UINT16>} in an image interface to hold
 * a segmented image as a quasi-image interface. The interval iterator and
 * random access methods of this implementation throw exception. The cursor is a
 * single instance, and copies are not made in each call.
 */
class PolarizationView implements Image<UINT16> {
    private final IPixelCursor<UINT16> _cursor;
    private final ImageFactory _factory;
    private final IMetadata _metadata;

    public PolarizationView(IPixelCursor<UINT16> cursor, ImageFactory factory, IMetadata metadata) {
        cursor.next();
        if (cursor.localize().length != metadata.getDim().length) {
            throw new IllegalArgumentException("cursor and metadata should be of the same dimension.");
        }

        this._cursor = cursor;
        this._factory = factory;
        this._metadata = metadata;
    }

    @Override
    public IPixelCursor<UINT16> getCursor() {
        this._cursor.reset();
        return this._cursor;
    }

    @Override
    public IPixelCursor<UINT16> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException {
        throw new AssertionError();
    }

    @Override
    public IPixelRandomAccess<UINT16> getRandomAccess() {
        throw new AssertionError();
    }

    @Override
    public ImageFactory getFactory() {
        return this._factory;
    }

    @Override
    public IMetadata getMetadata() {
        return this._metadata;
    }

}