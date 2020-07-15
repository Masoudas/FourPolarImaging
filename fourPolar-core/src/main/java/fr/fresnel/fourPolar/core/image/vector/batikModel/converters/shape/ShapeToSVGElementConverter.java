package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

/**
 * A class for converting {@link IShape} interfaces to a proper SVG
 * {@link Element} that can be written to svg. The generated element would have
 * the shape tag and the fundamental coordinates. All other features including
 * aesthetic features must be set separately for this element.
 */
public class ShapeToSVGElementConverter {
    private ShapeToSVGElementConverter() {
        throw new AssertionError();
    }

    /**
     * Create the element based on the shape interface that is at the backend of
     * this vector
     * 
     * @param svgDocument  is the source svg document
     * @param vectorShape  is the backend shape of this vector
     * @return an SVG element corresponding to this shape.
     * 
     * @throws IllegalArgumentException in case no converter is found for the shape
     *                                  of the vector.
     */
    public static Element convert(IShape shape, SVGDocument svgDocument) {
        if (shape instanceof ILineShape) {
            return LineShapeToSVGElementConverter.convert((ILineShape) shape, svgDocument);
        } else if (shape instanceof IBoxShape) {
            return BoxShapeToSVGElementConverter.convert((IBoxShape) shape, svgDocument);
        } else {
            throw new IllegalArgumentException(
                    "Can't convert the given shape because no suitable shape converter is found.");
        }

    }
}