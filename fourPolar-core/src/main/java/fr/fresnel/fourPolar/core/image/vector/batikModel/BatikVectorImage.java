package fr.fresnel.fourPolar.core.image.vector.batikModel;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlane;
import fr.fresnel.fourPolar.core.image.ImagePlaneSupplier;
import fr.fresnel.fourPolar.core.image.PlanarImageModel;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.VectorRandomAccess;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.ToSVGDefsElementConverter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * A vector image that has an {@link SVGDocument} as backend for each plane, and
 * extends {@link PlanarImageModel} to manage access to each plane.
 */
class BatikVectorImage extends PlanarImageModel<SVGDocument> implements VectorImage {
    private final ToSVGDefsElementConverter _toDefsElementConverter = new ToSVGDefsElementConverter();

    private final VectorImageFactory _factory;
    private final IMetadata _metadata;

    private static ImagePlaneSupplier<SVGDocument> _createPlaneSupplier(IMetadata metadata) {
        int xdim = (int) metadata.getDim()[0];
        int ydim = (int) metadata.getDim()[1];

        return new BatikVectorImagePlaneSupplier(xdim, ydim);
    }

    public BatikVectorImage(IMetadata metadata, VectorImageFactory factory){
        super(metadata, _createPlaneSupplier(metadata));

        _factory = factory;
        _metadata = metadata;
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
    public VectorRandomAccess randomAccess() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void addFilterComposite(FilterComposite composite) {
        _toDefsElementConverter.setFilterComposite(new FilterComposite[]{composite});
        for (ImagePlane<SVGDocument> imagePlane : _planes) {
            SVGDocument svgDocument = imagePlane.getPlane();

            Element defsElement = ToSVGDefsElementConverter.createDefsElement(svgDocument, svgDocument.getNamespaceURI());
            _toDefsElementConverter.convert(svgDocument, defsElement);    
        }
    }

    @Override
    public <T extends PixelType> void setImage(Image<T> image, T pixelType) {
        
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
    
}