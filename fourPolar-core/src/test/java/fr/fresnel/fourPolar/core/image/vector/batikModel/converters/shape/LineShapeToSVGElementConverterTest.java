package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class LineShapeToSVGElementConverterTest {
    @Test
    public void convert_lineFrom00to11_createsLineElementWithX1Y1X2Y2Attrs() {
        ILineShape lineShape = new ShapeFactory().line2DShape(new long[] { 1, 1 }, Math.PI / 4, 2, 1, AxisOrder.XY);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element line = LineShapeToSVGElementConverter.convert(lineShape, svgDocument);

        assertTrue(line.getTagName().equals("line"));
        assertTrue(line.getAttributeNS(null, "x1").equals(String.valueOf(0)));
        assertTrue(line.getAttributeNS(null, "y1").equals(String.valueOf(0)));
        assertTrue(line.getAttributeNS(null, "x2").equals(String.valueOf(1)));
        assertTrue(line.getAttributeNS(null, "y2").equals(String.valueOf(1)));
    }

    @Test
    public void convert_lineFrom0000to1100_createsLineElementWithX1Y1X2Y2Attrs() {
        ILineShape lineShape = new ShapeFactory().line2DShape(new long[] { 1, 1, 0, 0 }, Math.PI / 4, 2, 1,
                AxisOrder.XYCT);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element line = LineShapeToSVGElementConverter.convert(lineShape, svgDocument);

        assertTrue(line.getTagName().equals("line"));
        assertTrue(line.getAttributeNS(null, "x1").equals(String.valueOf(0)));
        assertTrue(line.getAttributeNS(null, "y1").equals(String.valueOf(0)));
        assertTrue(line.getAttributeNS(null, "x2").equals(String.valueOf(1)));
        assertTrue(line.getAttributeNS(null, "y2").equals(String.valueOf(1)));
    }

}