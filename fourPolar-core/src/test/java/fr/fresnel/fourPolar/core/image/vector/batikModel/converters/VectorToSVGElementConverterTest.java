package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.Filter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;
import fr.fresnel.fourPolar.core.util.shape.IShapeIterator;
import fr.fresnel.fourPolar.core.util.shape.Rotation3DOrder;

public class VectorToSVGElementConverterTest {
    private static final String _FILL_ATTR = "fill";
    private static final String _STROKE_ATTR = "stroke";
    private static final String _STROKE_WIDTH_ATTR = "stroke-width";
    private static final String _OPACITY_ATTR = "opacity";
    private static final String _FILTER_ATTR = "filter";

    String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;

    @Test
    public void convert_VectorWithOnlyShape_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        Vector vector = Vector.createLineVector(lineShape);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);
    }

    @Test
    public void convert_VectorWithShapeColor_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        ARGB8 color = new ARGB8(1, 2, 3, 4);
        Vector vector = Vector.createLineVector(lineShape);
        vector.setColor(color);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);

        Element vectorElement = (Element) lineNodes.item(0);
        assertTrue(vectorElement.getAttributeNS(namespaceURI, _STROKE_ATTR).equals(_getRGBColorAsString(color)));
    }

    @Test
    public void convert_VectorWithShapeFill_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        ARGB8 fillColor = new ARGB8(1, 2, 3, 4);
        Vector vector = Vector.createLineVector(lineShape);
        vector.setFill(fillColor);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);

        Element vectorElement = (Element) lineNodes.item(0);
        assertTrue(vectorElement.getAttributeNS(namespaceURI, _FILL_ATTR).equals(_getRGBColorAsString(fillColor)));
    }

    @Test
    public void convert_VectorWithShapeOpacity_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        ARGB8 color = new ARGB8(1, 2, 3, 4);
        Vector vector = Vector.createLineVector(lineShape);
        vector.setColor(color);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);

        Element vectorElement = (Element) lineNodes.item(0);
        assertTrue(vectorElement.getAttributeNS(namespaceURI, _OPACITY_ATTR).equals(String.valueOf(4 / 255f)));
    }

    @Test
    public void convert_VectorWithShapeStrokeWidth_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        int strokeWidth = 5;
        Vector vector = Vector.createLineVector(lineShape);
        vector.setStrokeWidth(strokeWidth);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);

        Element vectorElement = (Element) lineNodes.item(0);
        assertTrue(vectorElement.getAttributeNS(namespaceURI, _STROKE_WIDTH_ATTR).equals(String.valueOf(strokeWidth)));
    }

    @Test
    public void convert_VectorWithShapeFilter_CreatesSVGElement() {
        Dummy2DLineShape lineShape = new Dummy2DLineShape();
        String id = "DummyFilter";
        DummyFilterComposite composite = new DummyFilterComposite(id);

        Vector vector = Vector.createLineVector(lineShape);
        vector.setFilter(composite);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(namespaceURI,
                "svg", null);
        VectorToSVGElementConverter.convert(vector, svgDocument, namespaceURI);

        Element documentElement = svgDocument.getDocumentElement();
        NodeList lineNodes = documentElement.getElementsByTagName("line");

        // Only one line element is added.
        assertTrue(lineNodes.getLength() == 1);

        Element vectorElement = (Element) lineNodes.item(0);
        assertTrue(vectorElement.getAttributeNS(namespaceURI, _FILTER_ATTR).equals(_getFilterURL(id)));
    }

    private String _getRGBColorAsString(ARGB8 color) {
        return "rgb(" + String.valueOf(color.getR()) + "," + String.valueOf(color.getG()) + ","
                + String.valueOf(color.getB()) + ")";
    }

    private String _getFilterURL(String filterId){
        return "url(#" + filterId + ")";
    }

}

class Dummy2DLineShape implements ILineShape {
    @Override
    public IShapeIterator getIterator() {
        return null;
    }

    @Override
    public AxisOrder axisOrder() {
        return null;
    }

    @Override
    public int shapeDim() {
        return 2;
    }

    @Override
    public IShape rotate3D(double angle1, double angle2, double angle3, Rotation3DOrder rotation3dOrder) {
        return null;
    }

    @Override
    public IShape rotate2D(double angle) {
        return null;
    }

    @Override
    public IShape translate(long[] translation) {
        return null;
    }

    @Override
    public boolean isInside(long[] point) {
        return false;
    }

    @Override
    public IShape and(IShape shape) {
        return null;
    }

    @Override
    public long[] lineStart() {
        return new long[] { 0, 0 };
    }

    @Override
    public long[] lineEnd() {
        return new long[] { 1, 1 };
    }
}

class DummyFilterComposite implements FilterComposite {
    private String id;
    DummyFilterComposite(String id){
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Optional<String> xStart() {
        return null;
    }

    @Override
    public Optional<String> yStart() {
        return null;
    }

    @Override
    public Optional<String> widthPercent() {
        return null;
    }

    @Override
    public Optional<String> heightPercent() {
        return null;
    }

    @Override
    public Iterator<Filter> filters() {
        return null;
    }

}