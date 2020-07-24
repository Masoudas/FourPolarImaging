package fr.fresnel.fourPolar.core.image.generic.AWTModel;

import java.awt.image.BufferedImage;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.IPixelRandomAccess;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.AWTModel.type.BufferedImageTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;

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

    /**
     * Creates an empty image of the given type.
     * 
     * @param metadata  is the metadata of the image.
     * @param factory   is the factory that creates such image.
     * @param pixelType is the pixel type of this image.
     * 
     * @throws IllegalArgumentException if the dimension vector in metadata not have
     *                                  at least two dimensions
     */
    protected AWTBufferedImage(IMetadata metadata, ImageFactory factory, T pixelType) {
        super(metadata, _createBuffreredImageArray(metadata, pixelType));
        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(factory, "factory can't be null");
        Objects.requireNonNull(pixelType, "pixelType can't be null");

        _metadata = metadata;
        _factory = factory;
    }

    /**
     * Construct by directly providing the image planes.
     * 
     * @param metadata    is the metadata of the image.
     * @param factory     is the factory instance.
     * @param pixelType   is the pixel type associated with the image.
     * @param imagePlanes are the image planes.
     * 
     * @throws IllegalArgumentException if a plane does not have the same dimension
     *                                  as given by the metadata.
     */
    protected AWTBufferedImage(IMetadata metadata, ImageFactory factory, T pixelType, BufferedImage[] imagePlanes) {
        super(metadata, imagePlanes);

        Objects.requireNonNull(metadata, "Metadata can't be null");
        Objects.requireNonNull(factory, "factory can't be null");
        Objects.requireNonNull(pixelType, "pixelType can't be null");

        _metadata = metadata;
        _factory = factory;
    }

    @Override
    public void _checkPlanesHaveSameDimensionAsMetadata(IMetadata metadata, BufferedImage[] imagePlanes) {
        long[] planeSize = MetadataUtil.getPlaneDim(metadata);

        for (int i = 0; i < imagePlanes.length; i++) {
            BufferedImage imagePlane = imagePlanes[i];
            if (imagePlane.getWidth() != planeSize[0] || imagePlane.getHeight() != planeSize[1]) {
                throw new IllegalArgumentException(
                        "Image plane" + i + "does not have the same dimension as the metadata.");
            }
        }
    }

    public void _checkPlanesHaveSamePixelTypeAsInputType(T pixelType, BufferedImage[] imagePlanes) {
        BufferedImageTypes bufferedType = BufferedImageTypes.convertPixelTypes(pixelType.getType());

        for (int i = 0; i < imagePlanes.length; i++) {
            BufferedImage imagePlane = imagePlanes[i];
            if (imagePlane.getType() != bufferedType.getBufferedType()) {
                throw new IllegalArgumentException("Image plane" + i + "does not have the same type as input type.");
            }
        }

    }

    /**
     * @return an instance of supplier of buffered image that can be used to create
     *         planes of required size.
     */
    private static AWTBufferedImagePlaneSupplier _createBuffreredImageArray(IMetadata metadata, PixelType pixelType) {
        long[] dim = metadata.getDim();
        if (dim.length < 2){
            throw new IllegalArgumentException("Can't create buffered image plane with 1D metadata.");
        }

        int xdim = (int)dim[0];
        int ydim = (int)dim[1];

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