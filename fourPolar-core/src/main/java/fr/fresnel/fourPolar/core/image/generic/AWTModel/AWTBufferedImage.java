package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;

/**
 * An abstract class for implementing the image interface using
 * {@link BufferedImage}. This class shall be extended for each
 * {@link PixelType}.
 * <p>
 * Note that the system for accessing the buffered images is through a 1D array,
 * where each element of the array corresponds to one plane, as discussed
 * further in {@link #_createBuffreredImageArray()}.
 * 
 * @param <T> is the pixel type.
 */
public abstract class AWTBufferedImage<T extends PixelType> extends PlanarImageModel<BufferedImage>
        implements Image<T> {
    protected final IMetadata _metadata;
    private final ImageFactory _factory;

    protected AWTBufferedImage(IMetadata metadata, ImageFactory factory, T pixelType) {
        super(metadata, _createBuffreredImageArray(metadata, pixelType));
        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(factory, "factory can't be null");

        _metadata = metadata;
        _factory = factory;
    }

    /**
     * @return an instance of supplier of buffered image that can be used to create planes of required size.
     */
    private static AWTBufferedImagePlaneSupplier _createBuffreredImageArray(IMetadata metadata, PixelType pixelType) {
        int xdim = (int) metadata.getDim()[0];
        int ydim = (int) metadata.getDim()[1];

        return new AWTBufferedImagePlaneSupplier(xdim, ydim, pixelType);
    }

    @Override
    abstract public IPixelCursor<T> getCursor();

    @Override
    abstract public IPixelCursor<T> getCursor(long[] bottomCorner, long[] len) throws IllegalArgumentException;

    @Override
    abstract public IPixelRandomAccess<T> getRandomAccess();

    @Override
    public ImageFactory getFactory() {
        return _factory;
    }

    @Override
    public IMetadata getMetadata() {
        return _metadata;
    }

}