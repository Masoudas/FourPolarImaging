package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.animation.AnimationToSVGElementConverter;
import fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape.ShapeToSVGElementConverter;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * Using this class, we can convert an {@link Vector} to an {@link Element} of
 * batik, which is equivalent to an SVG element.
 * 
 * As we can't create an element without the presence of a document, the convert
 * method directly asks for the svg document.
 */
class VectorToSVGElementConverter {
    private static final String _FILL_ATTR = "fill";
    private static final String _STROKE_ATTR = "stroke";
    private static final String _STROKE_WIDTH_ATTR = "stroke-width";
    private static final String _OPACITY_ATTR = "opacity";
    private static final String _FILTER_ATTR = "filter";

    private VectorToSVGElementConverter() {
        throw new AssertionError();
    }

    /**
     * Convert the given vector to an svg element (including all its properties),
     * and set it as a child of document element.
     * 
     * @param vector       is the vector to be converted.
     * @param svgDocument  is the instance of the document to which this document
     *                     should be added.
     * @param namespaceURI is the name space inside the document to which the
     *                     element equivalent of vector must be added.
     * 
     * @throws IllegalArgumentException if at least one component of the vector
     *                                  can't be converted to an svg element.
     */
    public static void convert(Vector vector, SVGDocument svgDocument, String namespaceURI) {
        Element vectorElement = _createElementBasedOnVectorShape(vector.shape(), svgDocument);

        _setVectorAttributes(vectorElement, vector);
        _setVectorAnimation(vector, vectorElement, svgDocument);
        _appendVectorElementToDocumentElement(svgDocument, vectorElement);
    }

    private static void _appendVectorElementToDocumentElement(SVGDocument svgDocument, Element vectorElement) {
        svgDocument.getDocumentElement().appendChild(vectorElement);
    }

    /**
     * Adds all the attributes related to the vector element, such as fill, stroke
     * width and so forth.
     */
    private static void _setVectorAttributes(Element vectorElement, Vector vector) {
        _setElementFillColor(vectorElement, vector);
        _setElementOpacity(vectorElement, vector);
        _setElementStrokeColor(vectorElement, vector);
        _setElementStrokeWidth(vectorElement, vector);
        _setElementFilter(vectorElement, vector);
    }

    /**
     * Set the vector animation, if such an animation exists.
     */
    private static void _setVectorAnimation(Vector vector, Element vectorElement, SVGDocument svgDocument) {
        vector.animation()
                .ifPresent(anim -> AnimationToSVGElementConverter.addToVectorElement(anim, vectorElement, svgDocument));
    }

    /**
     * Create the element based on the shape interface that is at the backend of
     * this vector using the shape converter.
     */
    private static Element _createElementBasedOnVectorShape(IShape vectorShape, SVGDocument svgDocument) {
        return ShapeToSVGElementConverter.convert(vectorShape, svgDocument);
    }

    /**
     * Sets the fill color, from the {@link Vector#fillColor()} of vector.
     * 
     * @param vectorElement is the SVG document element.
     * @param namespaceURI  is the document name space.
     * @param vector        is the vector to be converted.
     */
    private static void _setElementFillColor(Element vectorElement, Vector vector) {
        if (vector.fill().isPresent()) {
            String fillColorAsString = _getRGBColorAsString(vector.fill().get());
            vectorElement.setAttributeNS(null, _FILL_ATTR, fillColorAsString);
        }
    }

    /**
     * Sets the stroke color, from the {@link Vector#color()} of vector.
     * 
     * @param vectorElement is the SVG document element.
     * @param vector        is the vector to be converted.
     */
    private static void _setElementStrokeColor(Element vectorElement, Vector vector) {
        if (vector.color().isPresent()) {
            String colorAsString = _getRGBColorAsString(vector.color().get());
            vectorElement.setAttributeNS(null, _STROKE_ATTR, colorAsString);
        }
    }

    /**
     * Sets the stroke width, from the {@link Vector#strokeWidth()} of vector.
     * 
     * @param vectorElement is the SVG document element.
     * @param vector        is the vector to be converted.
     */
    private static void _setElementStrokeWidth(Element vectorElement, Vector vector) {
        if (vector.strokeWidth().isPresent()) {
            String strokeWidthAsString = String.valueOf(vector.strokeWidth().get());
            vectorElement.setAttributeNS(null, _STROKE_WIDTH_ATTR, strokeWidthAsString);
        }
    }

    /**
     * Sets the element opacity, using the {@link Vector#color()} alpha value.
     * 
     * @param vectorElement is the SVG document element.
     * @param vector        is the vector to be converted.
     */
    private static void _setElementOpacity(Element vectorElement, Vector vector) {
        if (vector.color().isPresent()) {
            String opacityAsString = _getOpacityAsString(vector.color().get());
            vectorElement.setAttributeNS(null, _OPACITY_ATTR, opacityAsString);
        }
    }

    /**
     * Sets the element filter.
     * 
     * @param vectorElement is the SVG document element.
     * @param vector        is the vector to be converted.
     */
    private static void _setElementFilter(Element vectorElement, Vector vector) {
        if (vector.filter().isPresent()) {
            String filterId = vector.filter().get().id();
            vectorElement.setAttributeNS(null, _FILTER_ATTR, "url(#" + filterId + ")");
        }
    }

    /**
     * @return the string representation of {@link ARGB8} color part suitable for
     *         SVG color attribute
     */
    private static String _getRGBColorAsString(ARGB8 color) {
        return "rgb(" + String.valueOf(color.getR()) + "," + String.valueOf(color.getG()) + ","
                + String.valueOf(color.getB()) + ")";
    }

    /**
     * @return the alpha value of {@link ARGB8} suitable for SVG opacity attribute.
     *         The returned string has a value between zero and one.
     */
    private static String _getOpacityAsString(ARGB8 color) {
        return String.valueOf(color.getAlpha() / 255f);
    }
}