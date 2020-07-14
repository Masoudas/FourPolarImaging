package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.filter.FilterCompositeToSVGElementConverter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * Adds a given set of objects to svg defintion element. The svg definition
 * element contains svg objects that can't be used directly, but should rather
 * be used through reference calls inside the document (like filters, shapes,
 * etc).
 */
public class ToSVGDefsElementConverter {
    private static final String _DEFS_TAG = "defs";

    private FilterComposite[] _filterComposite = new FilterComposite[0];

    /**
     * Creates the defs element (an element with tag defs) as the child of document
     * element.
     * 
     * @param svgDocument  is the svg document instance.
     * @param namespaceURI is the name space in which the defs will be defined.
     * @return the defs element.
     */
    public static Element createDefsElement(SVGDocument svgDocument, String namespaceURI) {
        Element defsElement = svgDocument.createElementNS(namespaceURI, _DEFS_TAG);
        svgDocument.getDocumentElement().appendChild(defsElement);

        return defsElement;
    }

    /**
     * Set the filter composite definitions.
     * 
     * @param composites is the filter composites.
     */
    public ToSVGDefsElementConverter setFilterComposite(FilterComposite[] composites) {
        _filterComposite = composites;
        return this;
    }

    /**
     * Add the properties introduced by the set methods to the defs element of the
     * svg document.
     * 
     * @param svgDocument is the svg document.
     * @param defsElement is the defs element of this document. @see
     *                    {@link #createDefsElement()};
     * 
     */
    public void convert(SVGDocument svgDocument, Element defsElement) {
        Objects.requireNonNull(svgDocument, "svgDocument can't be null");
        _addFilterComposite(svgDocument, defsElement);

    }

    private void _addFilterComposite(SVGDocument svgDocument, Element defsElement) {
        for (FilterComposite filterComposite : _filterComposite) {
            FilterCompositeToSVGElementConverter.convert(filterComposite, svgDocument, defsElement);
        }
    }
}