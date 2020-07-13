package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.Filter;

public class BlenderFilterToSVGElementConverterTest {
    private static final String _FILTER_TAG = "feBlend";

    private static final String _IN_ATTR = "in";
    private static final String _IN2_ATTR = "in2";
    private static final String _MODE_ATTR = "mode";
    private static final String _RESULT_ATTR = "result";

    @Test
    public void convert_INSourceGraphicsModeMultiply_CreatesBlenderFilterElementWithCorrectAttrs() {
        BlenderFilter blenderFilter = new BlenderFilter(Filter.IN.SOURCE_GRAPHIC, BlenderFilter.Mode.MULTIPLY, null);

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element filterCompositeElement = document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "filter");

        BlenderFilterToSVGElementConverter.convert(blenderFilter, filterCompositeElement, document);

        NodeList nodes = filterCompositeElement.getChildNodes();

        // Has only one child filter.
        assertTrue(nodes.getLength() == 1);

        Element blender = (Element) nodes.item(0);
        assertTrue(blender.getTagName().equals(_FILTER_TAG));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _IN_ATTR).equals("SourceGraphic"));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _MODE_ATTR)
                .equals(BlenderFilter.Mode.MULTIPLY.modeAsString()));

        // These attributes don't exist
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _IN2_ATTR)
        .equals(""));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _RESULT_ATTR)
        .equals(""));

    }

    @Test
    public void convert_INSourceGraphicsIN2BackGroundImageModeMultiplyResultR_CreatesBlenderFilterElementWithCorrectAttrs() {
        BlenderFilter blenderFilter = new BlenderFilter(
            Filter.IN.SOURCE_GRAPHIC, Filter.IN.BACKGROUND_IMAGE, BlenderFilter.Mode.MULTIPLY, "R");

        SVGDocument document = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
        Element filterCompositeElement = document.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "filter");

        BlenderFilterToSVGElementConverter.convert(blenderFilter, filterCompositeElement, document);

        NodeList nodes = filterCompositeElement.getChildNodes();

        // Has only one child filter.
        assertTrue(nodes.getLength() == 1);

        Element blender = (Element) nodes.item(0);
        assertTrue(blender.getTagName().equals(_FILTER_TAG));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _IN_ATTR).equals("SourceGraphic"));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _MODE_ATTR)
                .equals(BlenderFilter.Mode.MULTIPLY.modeAsString()));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _IN2_ATTR)
        .equals("BackgroundImage"));
        assertTrue(blender.getAttributeNS(SVGDOMImplementation.SVG_NAMESPACE_URI, _RESULT_ATTR)
        .equals("R"));

    }
}