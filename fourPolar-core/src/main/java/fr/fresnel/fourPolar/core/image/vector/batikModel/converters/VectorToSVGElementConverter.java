package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.vector.FilterComposite;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * Using this class, we can convert an {@link Vector} to an {@link Element} of
 * batik, which is equivalent to an SVG element.
 * 
 * As we can't create an element without the presence of a document, the convert
 * method directly asks for the svg document.
 */
class VectorToSVGElementConverter {

    public void convert(SVGDocument svgDocument, String namespaceURI, Vector vector) {
        Element vectorElement = _createElementBasedOnVectorShape(svgDocument, namespaceURI, vector);

        _setElementFillColor(vectorElement, namespaceURI, vector);
        _setElementOpacity(vectorElement, namespaceURI, vector);
        _setElementStrokeColor(vectorElement, namespaceURI, vector);

        vector.filter().ifPresent((filter) -> _setElementFilter(vectorElement, namespaceURI, filter));

        svgDocument.getDocumentElement().appendChild(vectorElement);
    }

    /**
     * Create the element based on the shape interface that is at the backend of
     * this vector
     * 
     * @param svgDocument  is the source svg document
     * @param namespaceURI is the name space of the document
     * @param vectorShape  is the backend shape of this vector
     * @return an SVG element corresponding to this shape.
     */
    private Element _createElementBasedOnVectorShape(SVGDocument svgDocument, String namespaceURI, IShape vectorShape) {
        if (vectorShape instanceof ILineShape) {
            return _createLineGraphicElement(svgDocument, namespaceURI, (ILineShape) vectorShape);
        } else if (vectorShape instanceof IBoxShape) {
            return _createRectangleGraphicElement(svgDocument, namespaceURI, (IBoxShape) vectorShape);
        } else {
            throw new IllegalArgumentException(
                    "Can't convert the given shape because no suitable svg element is found.");
        }

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
    private Element _createRectangleGraphicElement(SVGDocument svgDocument, String namespaceURI, IBoxShape boxShape) {
        Element rectangleElement = svgDocument.createElementNS(namespaceURI, "rect");

        long[] rectangleMin = boxShape.min();
        long[] rectangleMax = boxShape.max();

        rectangleElement.setAttributeNS(namespaceURI, "x", String.valueOf(rectangleMin[0]));
        rectangleElement.setAttributeNS(namespaceURI, "y", String.valueOf(rectangleMin[1]));
        rectangleElement.setAttributeNS(namespaceURI, "width", String.valueOf(rectangleMax[0] - rectangleMin[0]));
        rectangleElement.setAttributeNS(namespaceURI, "height", String.valueOf(rectangleMax[1] - rectangleMin[1]));

        return rectangleElement;
    }

    /**
     * Converts a line shape to a {@link Line2D}. Only the first two coordinates *
     * are used to create the rectangle element.
     * 
     * @param svgDocument  is the source svg document.
     * @param namespaceURI is the name space of the document.
     * @param lineShape    is the line shape.
     * @return an svg element that is a line.
     */
    private Element _createLineGraphicElement(SVGDocument svgDocument, String namespaceURI, ILineShape lineShape) {
        Element lineElement = svgDocument.createElementNS(namespaceURI, "line");

        long[] start = lineShape.lineStart();
        long[] end = lineShape.lineEnd();

        lineElement.setAttributeNS(namespaceURI, "x1", String.valueOf(start[0]));
        lineElement.setAttributeNS(namespaceURI, "y1", String.valueOf(start[1]));
        lineElement.setAttributeNS(namespaceURI, "x2", String.valueOf(end[0]));
        lineElement.setAttributeNS(namespaceURI, "y2", String.valueOf(end[1]));

        return lineElement;
    }

    /**
     * Sets the fill color, from the {@link Vector#fillColor()} of vector.
     * 
     * @param element      is the SVG document element.
     * @param namespaceURI is the document name space.
     * @param vector       is the vector to be converted.
     */
    private void _setElementFillColor(Element element, String namespaceURI, Vector vector) {
        String fillColorAsString = _getRGBColorAsString(vector.fill());
        element.setAttributeNS(namespaceURI, "fill", fillColorAsString);
    }

    /**
     * Sets the stroke color, from the {@link Vector#color()} of vector.
     * 
     * @param element      is the SVG document element.
     * @param namespaceURI is the document name space.
     * @param vector       is the vector to be converted.
     */
    private void _setElementStrokeColor(Element element, String namespaceURI, Vector vector) {
        String colorAsString = _getRGBColorAsString(vector.color());
        element.setAttributeNS(namespaceURI, "stroke", colorAsString);
    }

    /**
     * Sets the element opacity, using the {@link Vector#color()} alpha value.
     * 
     * @param element      is the SVG document element.
     * @param namespaceURI is the document name space.
     * @param vector       is the vector to be converted.
     */
    private void _setElementOpacity(Element element, String namespaceURI, Vector vector) {
        String opacityAsString = _getOpacityAsString(vector.color());
        element.setAttributeNS(namespaceURI, "opacity", opacityAsString);
    }

    /**
     * Sets the element filter.
     * 
     * @param element      is the SVG document element.
     * @param namespaceURI is the document name space.
     * @param vector       is the vector to be converted.
     */
    private void _setElementFilter(Element element, String namespaceURI, FilterComposite filterComposite) {
        String filterId = filterComposite.id();
        element.setAttributeNS(namespaceURI, "filter", "url(#" + filterId + ")");

    }

    /**
     * @return the string representation of {@link ARGB8} color part suitable for
     *         SVG color attribute
     */
    private String _getRGBColorAsString(ARGB8 color) {
        return "rgb(" + String.valueOf(color.getR()) + "," + String.valueOf(color.getG()) + ","
                + String.valueOf(color.getB()) + ")";
    }

    /**
     * @return the alpha value of {@link ARGB8} suitable for SVG opacity attribute.
     *         The returned string has a value between zero and one.
     */
    private String _getOpacityAsString(ARGB8 color) {
        return String.valueOf(color.getAlpha() / 255f);
    }
}