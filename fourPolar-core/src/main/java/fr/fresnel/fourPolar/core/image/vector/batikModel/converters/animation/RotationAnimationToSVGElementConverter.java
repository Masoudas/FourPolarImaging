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
    private static final String _BEGIN_ATTR = "mouseover";
    private static final String _DUR_ATTR = "dur";
    private static final String _REPEAT_COUNT_ATTR = "repeatCount";
    private static final String _END_ATTR = "end";

    /**
     * Converts the given rotation animation to a proper svg element, and writes it
     * as a child element of the given element.
     * 
     * @param svgDocument       is the document.
     * @param vectorElement     is the vector element to which we want to add this
     *                          animation.
     * @param rotationAnimation is rotation animation.
     */
    public static void convert(SVGDocument svgDocument, Element vectorElement, RotationAnimation rotationAnimation) {
        String nameSpaceURI = vectorElement.getNamespaceURI();

        Element animationElement = _createChildAnimationElement(svgDocument, vectorElement.getNamespaceURI());
        _setAnimationElementAttributes(rotationAnimation, nameSpaceURI, animationElement);
        _appendAnimationElementToVectorElement(vectorElement, animationElement);
    }

    private static void _appendAnimationElementToVectorElement(Element vectorElement, Element animationElement) {
        vectorElement.appendChild(animationElement);
    }

    private static void _setAnimationElementAttributes(RotationAnimation rotationAnimation, String nameSpaceURI,
            Element animationElement) {
        animationElement.setAttributeNS(nameSpaceURI, _ATTRIBUTE_NAME_ATTR, rotationAnimation.attributeName());
        animationElement.setAttributeNS(nameSpaceURI, _TYPE_ATTR, rotationAnimation.type());
        animationElement.setAttributeNS(nameSpaceURI, _VALUES_ATTR, rotationAnimation.values());
        animationElement.setAttributeNS(nameSpaceURI, _BEGIN_ATTR, rotationAnimation.begin());
        animationElement.setAttributeNS(nameSpaceURI, _DUR_ATTR, rotationAnimation.dur());
        rotationAnimation.repeatCount()
                .ifPresent(attr -> animationElement.setAttributeNS(nameSpaceURI, _REPEAT_COUNT_ATTR, attr));
        rotationAnimation.end().ifPresent(attr -> animationElement.setAttributeNS(nameSpaceURI, _END_ATTR, attr));
    }

    /**
     * @return an element with the animation name tag.
     */
    private static Element _createChildAnimationElement(SVGDocument svgDocument, String namespaceURI) {
        return svgDocument.createElementNS(namespaceURI, _ANIMATION_TAG);
    }

}