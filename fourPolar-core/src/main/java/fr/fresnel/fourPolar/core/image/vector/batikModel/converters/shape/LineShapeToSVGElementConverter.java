package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape;

import java.text.DecimalFormat;

import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.shape.ILineShape;

public class LineShapeToSVGElementConverter {
    private final static String _ELEMENT_TAG = "line";

    private final static String _X1_ATTR = "x1";
    private final static String _Y1_ATTR = "y1";
    private final static String _X2_ATTR = "x2";
    private final static String _Y2_ATTR = "y2";

    private final static DecimalFormat _COORDINATE_FORMATTER = new DecimalFormat("#.#");
    
    /**
     * Converts a line shape to a {@link Line2D}. Only the first two coordinates *
     * are used to create the rectangle element.
     * 
     * @param lineShape    is the line shape.
     * @param svgDocument  is the source svg document.
     * @return an svg element that is a line.
     */
    public static Element convert(ILineShape lineShape, SVGDocument svgDocument) {
        Element lineElement = svgDocument.createElementNS(svgDocument.getNamespaceURI(), _ELEMENT_TAG);

        double[] start = lineShape.lineStartAsDouble();
        double[] end = lineShape.lineEndAsDouble();

        lineElement.setAttributeNS(null, _X1_ATTR, _COORDINATE_FORMATTER.format(start[0]));
        lineElement.setAttributeNS(null, _Y1_ATTR, _COORDINATE_FORMATTER.format(start[1]));
        lineElement.setAttributeNS(null, _X2_ATTR, _COORDINATE_FORMATTER.format(end[0]));
        lineElement.setAttributeNS(null, _Y2_ATTR, _COORDINATE_FORMATTER.format(end[1]));

        return lineElement;
    }
}