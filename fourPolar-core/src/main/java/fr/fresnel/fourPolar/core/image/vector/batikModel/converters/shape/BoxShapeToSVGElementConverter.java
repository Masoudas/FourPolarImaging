package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * Converts a box shape to a "rect" svg element.
 */
class BoxShapeToSVGElementConverter {
    private final static String _ELEMENT_TAG = "rect";

    private final static String _X_ATTR = "x";
    private final static String _Y_ATTR = "y";
    private final static String _WIDTH_ATTR = "width";
    private final static String _HEIGHT_ATTR = "height";

    private BoxShapeToSVGElementConverter() {
        throw new AssertionError();
    }

    /**
     * Converts a box shape to a {@link Rectangle}. Only the first two coordinates
     * are used to create the rectangle element.
     * 
     * @param svgDocument  is the source svg document
     * @param namespaceURI is the name space of the document
     * @param boxShape     is the box shape.
     * 
     * @return an svg element that is a rectangle.
     */
    public static Element convert(IBoxShape boxShape, SVGDocument svgDocument, String namespaceURI) {
        Element rectangleElement = svgDocument.createElementNS(namespaceURI, _ELEMENT_TAG);

        long[] rectangleMin = boxShape.min();
        long[] rectangleMax = boxShape.max();

        rectangleElement.setAttributeNS(namespaceURI, _X_ATTR, String.valueOf(rectangleMin[0]));
        rectangleElement.setAttributeNS(namespaceURI, _Y_ATTR, String.valueOf(rectangleMin[1]));
        rectangleElement.setAttributeNS(namespaceURI, _WIDTH_ATTR, String.valueOf(rectangleMax[0] - rectangleMin[0]));
        rectangleElement.setAttributeNS(namespaceURI, _HEIGHT_ATTR, String.valueOf(rectangleMax[1] - rectangleMin[1]));

        return rectangleElement;
    }
}