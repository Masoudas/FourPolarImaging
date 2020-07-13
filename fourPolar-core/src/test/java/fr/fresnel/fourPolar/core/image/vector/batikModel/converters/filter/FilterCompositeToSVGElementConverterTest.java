package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterCompositeBuilder;

public class FilterCompositeToSVGElementConverterTest {
    private final static String _ID_ATTR = "id";
    private final static String _WIDTH_ATTR = "width";
    private final static String _HEIGHT_ATTR = "height";
    private final static String _X_START_ATTR = "x";
    private final static String _Y_START_ATTR = "y";

    @Test
    public void convert_CompositeWithIDAndOneBlenderFilter_CreatesFilterCompositeWithOneChildFilter() {
        DummyBlenderFilter filter = new DummyBlenderFilter();
        String filterID = "testFilter";
        FilterComposite composite = new FilterCompositeBuilder(filterID, filter).build();

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        FilterCompositeToSVGElementConverter.convert(composite, document, SVGDOMImplementation.SVG_NAMESPACE_URI);

        Element documentElement = document.getDocumentElement();
        NodeList nodes = documentElement.getChildNodes();

        assertTrue(nodes.getLength() == 1);
        Element filterCompositeElement = (Element) nodes.item(0);

        assertTrue(filterCompositeElement.getTagName().equals("filter"));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _ID_ATTR)
                .equals(filterID));

        // The following attributes don't exist
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _X_START_ATTR)
                .equals(""));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _Y_START_ATTR)
                .equals(""));
        assertTrue(
                filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _WIDTH_ATTR).equals(""));
        assertTrue(
                filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _HEIGHT_ATTR).equals(""));

        // Check that the child dummy blend filter has been added as well.
        NodeList compositeChildNodes = filterCompositeElement.getChildNodes();
        assertTrue(compositeChildNodes.getLength() == 1); // Only one child filter is added.

        Element filterElement = (Element) compositeChildNodes.item(0);
        assertTrue(filterElement.getTagName().equals("feBlend"));
    }

    @Test
    public void convert_CompositeWithIDAndTwoBlenderFilters_CreatesFilterCompositeWithOneChildFilter() {
        DummyBlenderFilter filter = new DummyBlenderFilter();
        String filterID = "testFilter";
        FilterComposite composite = new FilterCompositeBuilder(filterID, filter).addFilter(new DummyBlenderFilter())
                .build();

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        FilterCompositeToSVGElementConverter.convert(composite, document, SVGDOMImplementation.SVG_NAMESPACE_URI);

        Element documentElement = document.getDocumentElement();
        NodeList nodes = documentElement.getChildNodes();

        Element filterCompositeElement = (Element) nodes.item(0);

        // Check that the child dummy blend filter has been added as well.
        NodeList compositeChildNodes = filterCompositeElement.getChildNodes();
        assertTrue(compositeChildNodes.getLength() == 2); // Only one child filter is added.
    }

    @Test
    public void convert_CompositeWithIDXYWidthHeightAndOneBlenderFilter_CreatesFilterCompositeWithOneChildFilter() {
        DummyBlenderFilter filter = new DummyBlenderFilter();

        String filterID = "testFilter";
        int x = 0;
        int y = 1;
        int width = 2;
        int height = 3;
        FilterComposite composite = new FilterCompositeBuilder(filterID, filter).xStart(x).yStart(y).widthPercent(width)
                .heightPercent(height).build();

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        FilterCompositeToSVGElementConverter.convert(composite, document, SVGDOMImplementation.SVG_NAMESPACE_URI);

        Element documentElement = document.getDocumentElement();
        NodeList nodes = documentElement.getChildNodes();

        assertTrue(nodes.getLength() == 1);
        Element filterCompositeElement = (Element) nodes.item(0);

        assertTrue(filterCompositeElement.getTagName().equals("filter"));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _ID_ATTR)
                .equals(filterID));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _X_START_ATTR)
                .equals(String.valueOf(x)));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _Y_START_ATTR)
                .equals(String.valueOf(y)));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _WIDTH_ATTR)
                .equals(String.valueOf(width) + "%"));
        assertTrue(filterCompositeElement.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _HEIGHT_ATTR)
                .equals(String.valueOf(height) + "%"));

        // Check that the child dummy blend filter has been added as well.
        NodeList compositeChildNodes = filterCompositeElement.getChildNodes();
        assertTrue(compositeChildNodes.getLength() == 1); // Only one child filter is added.

        Element filterElement = (Element) compositeChildNodes.item(0);
        assertTrue(filterElement.getTagName().equals("feBlend"));
    }

}

class DummyBlenderFilter extends BlenderFilter {

    public DummyBlenderFilter() {
        super(IN.SOURCE_GRAPHIC, BlenderFilter.Mode.MULTIPLY, null);

    }

}