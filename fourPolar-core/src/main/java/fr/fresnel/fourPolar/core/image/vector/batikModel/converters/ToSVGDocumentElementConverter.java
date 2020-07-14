package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * Adds the given properties as attributes to the svg document element.
 */
public class ToSVGDocumentElementConverter {
    /**
     * Indicates the width attribute of the svg document element.
     */
    private static final String _WIDTH_ATTR = "width";

    /**
     * Indicates the height attribute of the svg document element.
     */
    private static final String _HEIGHT_ATTR = "height";

    private int _xdim = 0;
    private int _ydim = 0;

    /**
     * Set image dimension of the document.
     * 
     * @param xdim is the x dimension (or width).
     * @param ydim is the y dimension (or height).
     */
    public ToSVGDocumentElementConverter setImageDim(int xdim, int ydim) {
        _xdim = xdim;
        _ydim = ydim;

        return this;
    }

    /**
     * Convert the parameters given by the set method to attributes of the document
     * element.
     * 
     * @param svgDocument  is the svg document instance.
     * @param namespaceURI is the name space to which the properties should be
     *                     written.
     */
    public void convert(SVGDocument svgDocument, String namespaceURI) {
        _setSVGDocumentSize(svgDocument, namespaceURI);
    }

    private void _setSVGDocumentSize(SVGDocument svgDocument, String namespaceURI) {
        Element documentElement = svgDocument.getDocumentElement();

        documentElement.setAttributeNS(namespaceURI, _WIDTH_ATTR, String.valueOf(_xdim));
        documentElement.setAttributeNS(namespaceURI, _HEIGHT_ATTR, String.valueOf(_ydim));
    }

}