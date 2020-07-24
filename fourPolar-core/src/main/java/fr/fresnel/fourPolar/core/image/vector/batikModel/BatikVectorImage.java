package fr.fresnel.fourPolar.core.image.vector.batikModel;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlane;
import fr.fresnel.fourPolar.core.image.ImagePlaneSupplier;
import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.ToSVGDefsElementConverter;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.VectorToSVGElementConverter;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.image.ImageToSVGElementConverter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.shape.ILineShape;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;

/**
 * A vector image that has an {@link SVGDocument} as backend for each plane, and
 * extends {@link PlanarImageModel} to manage access to each plane.
 */
class BatikVectorImage extends PlanarImageModel<SVGDocument> implements VectorImage {
    private static final String _SVG_WIDTH_ATTR = "width";
    private static final String _SVG_HEIGHT_ATTR = "height";

    private final ToSVGDefsElementConverter _toDefsElementConverter = new ToSVGDefsElementConverter();

    private final VectorImageFactory _factory;
    private final IMetadata _metadata;

    private final int _imageDimension;

    private static ImagePlaneSupplier<SVGDocument> _createPlaneSupplier(IMetadata metadata) {
        long[] dim = metadata.getDim();
        if (dim.length < 2) {
            throw new IllegalArgumentException("Can't create buffered image plane with 1D metadata.");
        }

        int xdim = (int) dim[0];
        int ydim = (int) dim[1];

        return new BatikVectorImagePlaneSupplier(xdim, ydim);
    }

    /**
     * Create an empty image instance.
     * 
     * @param metadata is the metadata associated with the image.
     * @param factory  is the factory that creates said image.
     * 
     * @throws IllegalArgumentException if the dimension vector in metadata not have
     *                                  at least two dimensions
     */
    public BatikVectorImage(IMetadata metadata, VectorImageFactory factory) {
        super(metadata, _createPlaneSupplier(metadata));
        Objects.requireNonNull(factory, "factory can't be null");

        _factory = factory;
        _metadata = metadata;
        _imageDimension = metadata.getDim().length;
    }

    /**
     * Create an image by directly providing each plane. Note that the planes are
     * indexed in the order they'are provided.
     * 
     * @param metadata is the metadata of the image.
     * @param factory  is the factory for creating such images.
     * @param planes   are the image planes.
     * 
     * @throws IllegalArgumentException if number of planes in metadata does not
     *                                  match the number of planes, or a plane size
     *                                  is not equal to the metadata size.
     */
    public BatikVectorImage(IMetadata metadata, VectorImageFactory factory, SVGDocument[] planes) {
        super(metadata, planes);

        _metadata = metadata;
        _factory = factory;
        _imageDimension = metadata.getDim().length;
    }

    @Override
    protected void _checkPlanesHaveSameDimensionAsMetadata(IMetadata metadata, SVGDocument[] planes)
            throws IllegalArgumentException {
        long[] planeSize = MetadataUtil.getPlaneDim(metadata);
        String widthAsString = String.valueOf(planeSize[0]);
        String heightAsString = String.valueOf(planeSize[1]);

        for (SVGDocument svgDocument : planes) {
            Element docElement = svgDocument.getDocumentElement();

            String planeWidth = docElement.getAttributeNS(null, _SVG_WIDTH_ATTR);
            String planeHeight = docElement.getAttributeNS(null, _SVG_HEIGHT_ATTR);
            if (!planeWidth.equals(widthAsString) || !planeHeight.equals(heightAsString)) {
                throw new IllegalArgumentException("plane dimension is not equal to metadata plane size.");
            }
        }
    }

    @Override
    public IMetadata metadata() {
        return _metadata;
    }

    @Override
    public VectorImageFactory getFactory() {
        return _factory;
    }

    @Override
    public void addFilterComposite(FilterComposite composite) {
        _toDefsElementConverter.setFilterComposite(new FilterComposite[] { composite });
        for (ImagePlane<SVGDocument> imagePlane : _planes) {
            SVGDocument svgDocument = imagePlane.getPlane();

            Element defsElement = ToSVGDefsElementConverter.createDefsElement(svgDocument,
                    svgDocument.getNamespaceURI());
            _toDefsElementConverter.convert(svgDocument, defsElement);
        }
    }

    @Override
    public <T extends PixelType> void setImage(Image<T> image, T pixelType) {
        if (!MetadataUtil.isDimensionEqual(this.metadata(), image.getMetadata())) {
            throw new IllegalArgumentException("The given image does not have same dimension as this image");
        }

        for (int planeIndex = 1; planeIndex <= _planes.length; planeIndex++) {
            SVGDocument vectorPlane = getImagePlane(planeIndex).getPlane();
            Element imgElement = ImageToSVGElementConverter.convert(image, pixelType, planeIndex, vectorPlane);
            vectorPlane.getDocumentElement().appendChild(imgElement);
        }

    }

    @Override
    public ImagePlane<SVGDocument> getImagePlane(int planeNumber) {
        return super.getImagePlane(planeNumber);
    }

    @Override
    public int getPlaneIndex(long[] position) {
        return super.getPlaneIndex(position);
    }

    @Override
    public int numPlanes() {
        return super.numPlanes();
    }

    @Override
    public void addVector(Vector vector) {
        if (vector.shape().spaceDim() != this._imageDimension) {
            throw new IllegalArgumentException("vector and image don't have the same axis order");
        }
        int planeIndexOfVector = _getVectorPlane(vector);
        VectorToSVGElementConverter.convert(vector, getImagePlane(planeIndexOfVector).getPlane());
    }

    /**
     * @return the plane where this vector should be put.
     */
    private int _getVectorPlane(Vector vector) {
        IShape shape = vector.shape();
        if (shape instanceof IBoxShape) {
            return getPlaneIndex(((IBoxShape) shape).min());
        } else if (shape instanceof ILineShape) {
            return getPlaneIndex(((ILineShape) shape).lineStart());
        } else {
            throw new IllegalArgumentException("Can't compute the plane where shape should be set.");
        }
    }

}