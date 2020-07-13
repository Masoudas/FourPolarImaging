package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.filter;

import java.util.Iterator;
import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.Filter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

/**
 * Using this class, we can convert a {@link FilterComposite} to an SVG element
 * that would be written in the root node of the SVG.
 */
class FilterCompositeToSVGElementConverter {
    private final static String _ID_ATTR = "id";
    private final static String _WIDTH_ATTR = "width";
    private final static String _HEIGHT_ATTR = "height";
    private final static String _X_START_ATTR = "x";
    private final static String _Y_START_ATTR = "y";

    private FilterCompositeToSVGElementConverter() {
        throw new AssertionError();
    }

    /**
     * Converts the given filter composite to an svg document element, and write it
     * as a child of the documnt element (i.e, on top of the document, for all other
     * elements to use.)
     * 
     * @param composite    is the filter composite.
     * @param svgDocument  is the svg document instance.
     * @param namespaceURI is the name space of where this filter should be added in
     *                     the document.
     * @throws IllegalArgumentException if no converter is found for the filter.
     */
    public static void convert(FilterComposite composite, SVGDocument svgDocument, String namespaceURI) {
        Objects.requireNonNull(composite, "composite can't be null");
        Objects.requireNonNull(svgDocument, "svgDocument can't be null");
        Objects.requireNonNull(namespaceURI, "namespaceURI can't be null");

        Element filterCompositeElement = _createFilterElementAsDocumentElementChild(svgDocument, namespaceURI);
        _addFilterAttributes(composite, filterCompositeElement, namespaceURI);
        _addFilterAttributes(composite, filterCompositeElement, namespaceURI);
    }

    private static Element _createFilterElementAsDocumentElementChild(SVGDocument svgDocument, String namespaceURI) {
        Element filterCompositeElement = svgDocument.createElementNS(namespaceURI, "filter");
        Element documentElement = svgDocument.getDocumentElement();

        documentElement.appendChild(filterCompositeElement);

        return filterCompositeElement;
    }

    /**
     * Adds the general attributes of the filter composite to the filter element.
     */
    private static void _addFilterAttributes(FilterComposite composite, Element filterCompositeElement,
            String namespaceURI) {
        filterCompositeElement.setAttributeNS(namespaceURI, _ID_ATTR, composite.id());
        composite.xStart()
                .ifPresent(xStart -> filterCompositeElement.setAttributeNS(namespaceURI, _X_START_ATTR, xStart));
        composite.yStart()
                .ifPresent(yStart -> filterCompositeElement.setAttributeNS(namespaceURI, _Y_START_ATTR, yStart));
        composite.widthPercent()
                .ifPresent(width -> filterCompositeElement.setAttributeNS(namespaceURI, _WIDTH_ATTR, width));
        composite.widthPercent()
                .ifPresent(height -> filterCompositeElement.setAttributeNS(namespaceURI, _HEIGHT_ATTR, height));

    }

    /**
     * Add filters of this composite to the filter element.
     */
    private void _addFiltersOfComposite(FilterComposite composite, Element filterCompositeElement,
            SVGDocument document) {
        for (Iterator<Filter> filterItr = composite.filters(); filterItr.hasNext();) {
            _addFilterAsFilterElementChild(filterItr.next(), filterCompositeElement, document);
        }
    }

    private static void _addFilterAsFilterElementChild(Filter filter, Element filterCompositeElement,
            SVGDocument document) {
        if (filter instanceof BlenderFilter) {
            BlenderFilterToSVGElementConverter.convert(filter, filterCompositeElement, document);
        } else {
            throw new IllegalArgumentException("No converter to svg element was found for the given filter");
        }
    }
}