package fr.fresnel.fourPolar.core.image.vector.batikModel;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlaneSupplier;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.ToSVGDefsElementConverter;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.ToSVGDocumentElementConverter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * 
 * Note that the namespaceURI parameter of all documents is set to
 * {@link SVGDOMImplementation.SVG_NAMESPACE_URI}
 */
class BatikVectorImagePlaneSupplier implements ImagePlaneSupplier<SVGDocument> {
    private static final String _NAMESPACE_URI = SVGDOMImplementation.SVG_NAMESPACE_URI;

    private final ToSVGDefsElementConverter _toDefsElementConverter = new ToSVGDefsElementConverter();
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

    /**
     * Defines an array of filter composites, which will be added to the document
     * element, that can subsequently be used for all elements of the document.
     * 
     * @param composites is the composite filter.
     */
    public void setFilterComposite(FilterComposite[] composites) {
        _toDefsElementConverter.setFilterComposite(composites);
    }

    @Override
    public SVGDocument get() {
        SVGDocument svgDocument = _createSVGDocument();
        _setDefsElement(svgDocument, _NAMESPACE_URI);
        _setDocumentElementAttributes(svgDocument, _NAMESPACE_URI);

        return svgDocument;
    }

    private SVGDocument _createSVGDocument() {
        return (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(_NAMESPACE_URI, "svg", null);
    }

    /**
     * Set the properties that should be written as attributes of svg document.
     */
    private void _setDocumentElementAttributes(SVGDocument svgDocument, String namespaceURI) {
        _toDocsElementConverter.convert(svgDocument, namespaceURI);
    }

    /**
     * Set the elements that should be written as children of definition element.
     */
    private void _setDefsElement(SVGDocument svgDocument, String namespaceURI) {
        Element defsElement = ToSVGDefsElementConverter.createDefsElement(svgDocument, namespaceURI);
        _toDefsElementConverter.convert(svgDocument, defsElement);
    }

}