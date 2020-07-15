package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.filter;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;

class BlenderFilterToSVGElementConverter {
    private static final String _FILTER_TAG = "feBlend";

    private static final String _IN_ATTR = "in";
    private static final String _IN2_ATTR = "in2";
    private static final String _MODE_ATTR = "mode";
    private static final String _RESULT_ATTR = "result";

    /**
     * Convert the blender filter to an svg element, and write it as a child of the
     * filter composite element.
     * 
     * @param blenderFilter          is the blender filter.
     * @param filterCompositeElement is the filter composite.
     * @param document               is the svg document the filter composite
     *                               element belongs to.
     */
    public static void convert(BlenderFilter blenderFilter, Element filterCompositeElement, SVGDocument document) {
        Objects.requireNonNull(blenderFilter, "blenderFilter can't be null");
        Objects.requireNonNull(filterCompositeElement, "filterCompositeElement can't be null");
        Objects.requireNonNull(document, "document can't be null");
        
        Element filterElement = _createChildBlenderFilterElement(document);
        _setFilterElementAttrs(blenderFilter, filterElement);
        _appendAnimationElementToFilterCompositeElement(filterCompositeElement, filterElement);
    }

    private static Element _createChildBlenderFilterElement(SVGDocument document) {
        return document.createElementNS(document.getNamespaceURI(), _FILTER_TAG);
    }

    private static void _setFilterElementAttrs(BlenderFilter filter, Element filterElement) {
        filterElement.setAttributeNS(null, _IN_ATTR, filter.in());
        filterElement.setAttributeNS(null, _MODE_ATTR, filter.mode());
        filter.in2().ifPresent(in2 -> filterElement.setAttributeNS(null, _IN2_ATTR, in2));
        filter.resultName()
                .ifPresent(resultName -> filterElement.setAttributeNS(null, _RESULT_ATTR, resultName));
    }

    private static void _appendAnimationElementToFilterCompositeElement(Element filterCompositeElement,
            Element filterElement) {
        filterCompositeElement.appendChild(filterElement);
    }

}