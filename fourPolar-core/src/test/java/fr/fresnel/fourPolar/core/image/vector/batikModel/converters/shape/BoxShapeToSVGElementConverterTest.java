package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class BoxShapeToSVGElementConverterTest {
    @Test
    public void conver_boxFrom00to11_createsRectangleElementWithX_Y_Width_Height_Attrs() {
        IBoxShape shape = new ShapeFactory().closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.XY);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element box = BoxShapeToSVGElementConverter.convert(shape, svgDocument);

        assertTrue(box.getTagName().equals("rect"));
        assertTrue(box.getAttributeNS(null, "x").equals(String.valueOf(0)));
        assertTrue(box.getAttributeNS(null, "y").equals(String.valueOf(0)));
        assertTrue(box.getAttributeNS(null, "width").equals(String.valueOf(1)));
        assertTrue(box.getAttributeNS(null, "height").equals(String.valueOf(1)));
    }

    @Test
    public void conver_boxFrom0000to1100_createsRectangleElementWithX_Y_Width_Height_Attrs() {
        IBoxShape shape = new ShapeFactory().closedBox(new long[] { 0, 0, 0, 0 }, new long[] { 1, 1, 0, 0 },
                AxisOrder.XYCT);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element box = BoxShapeToSVGElementConverter.convert(shape, svgDocument);

        assertTrue(box.getTagName().equals("rect"));
        assertTrue(box.getAttributeNS(null, "x").equals(String.valueOf(0)));
        assertTrue(box.getAttributeNS(null, "y").equals(String.valueOf(0)));
        assertTrue(box.getAttributeNS(null, "width").equals(String.valueOf(1)));
        assertTrue(box.getAttributeNS(null, "height").equals(String.valueOf(1)));

    }
}