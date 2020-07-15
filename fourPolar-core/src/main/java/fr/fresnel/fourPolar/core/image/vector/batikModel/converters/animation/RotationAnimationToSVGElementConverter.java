package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.animation;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.animation.RotationAnimation;

/**
 * Converts an instance {@link RotationAnimation} to an SVG element.
 */
class RotationAnimationToSVGElementConverter {
    private static final String _ANIMATION_TAG = "animateTransform";

    /**
     * List of attributes that are written with this converter
     */
    private static final String _ATTRIBUTE_NAME_ATTR = "attributeName";
    private static final String _TYPE_ATTR = "type";
    private static final String _VALUES_ATTR = "values";
    private static final String _BEGIN_ATTR = "begin";
    private static final String _DUR_ATTR = "dur";
    private static final String _REPEAT_COUNT_ATTR = "repeatCount";
    private static final String _END_ATTR = "end";

    /**
     * Converts the given rotation animation to a proper svg element, and writes it
     * as a child element of the given element.
     * 
     * @param rotationAnimation is rotation animation.
     * @param vectorElement     is the vector element to which we want to add this
     *                          animation.
     * @param svgDocument       is the document.
     */
    public static void convert(RotationAnimation rotationAnimation, Element vectorElement, SVGDocument svgDocument) {
        Element animationElement = _createChildAnimationElement(svgDocument, svgDocument.getNamespaceURI());
        _setAnimationElementAttributes(rotationAnimation, animationElement);
        _appendAnimationElementToVectorElement(vectorElement, animationElement);
    }

    private static void _appendAnimationElementToVectorElement(Element vectorElement, Element animationElement) {
        vectorElement.appendChild(animationElement);
    }

    private static void _setAnimationElementAttributes(RotationAnimation rotationAnimation,
            Element animationElement) {
        animationElement.setAttributeNS(null, _ATTRIBUTE_NAME_ATTR, rotationAnimation.attributeName());
        animationElement.setAttributeNS(null, _TYPE_ATTR, rotationAnimation.type());
        animationElement.setAttributeNS(null, _VALUES_ATTR, rotationAnimation.values());
        animationElement.setAttributeNS(null, _BEGIN_ATTR, rotationAnimation.begin());
        animationElement.setAttributeNS(null, _DUR_ATTR, rotationAnimation.dur());
        rotationAnimation.repeatCount()
                .ifPresent(attr -> animationElement.setAttributeNS(null, _REPEAT_COUNT_ATTR, attr));
        rotationAnimation.end().ifPresent(attr -> animationElement.setAttributeNS(null, _END_ATTR, attr));
    }

    /**
     * @return an element with the animation name tag.
     */
    private static Element _createChildAnimationElement(SVGDocument svgDocument, String namespaceURI) {
        return svgDocument.createElementNS(namespaceURI, _ANIMATION_TAG);
    }

}