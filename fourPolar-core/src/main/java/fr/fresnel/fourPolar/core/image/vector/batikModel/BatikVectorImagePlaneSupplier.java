package fr.fresnel.fourPolar.core.image.vector.batikModel;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlaneSupplier;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.ToSVGDocumentElementConverter;

/**
 * 
 * Note that the namespaceURI parameter of all documents is set to
 * {@link SVGDOMImplementation.SVG_NAMESPACE_URI}
 */
class BatikVectorImagePlaneSupplier implements ImagePlaneSupplier<SVGDocument> {
    private static final String _NAMESPACE_URI = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private final ToSVGDocumentElementConverter _toDocsElementConverter = new ToSVGDocumentElementConverter();

    /**
     * Initialize the supplier to provide instances of SVG document for the given x
     * and y size. To set the size of DOM, we set the "width" and "height" attributes of
     * the svg document element.
     * 
     * @param xdim is the x dimension of the plane.
     * @param ydim is the y dimension of the plane.
     */
    public BatikVectorImagePlaneSupplier(int xdim, int ydim) {
        _toDocsElementConverter.setImageDim(xdim, ydim);
    }

    @Override
    public SVGDocument get() {
        SVGDocument svgDocument = _createSVGDocument();
        _setDocumentElementAttributes(svgDocument);

        return svgDocument;
    }

    private SVGDocument _createSVGDocument() {
        return (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(_NAMESPACE_URI, "svg", null);
    }

    /**
     * Set the properties that should be written as attributes of svg document.
     */
    private void _setDocumentElementAttributes(SVGDocument svgDocument) {
        _toDocsElementConverter.convert(svgDocument);
    }

}