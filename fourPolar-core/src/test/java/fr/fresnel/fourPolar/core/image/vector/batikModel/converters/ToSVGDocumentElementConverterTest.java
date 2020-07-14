package fr.fresnel.fourPolar.core.image.vector.batikModel.converters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class ToSVGDocumentElementConverterTest {
    private static final String _WIDTH_ATTR = "width";
    private static final String _HEIGHT_ATTR = "height";

    private static final String _NAME_SPACE = SVGDOMImplementation.SVG_NAMESPACE_URI;

    @Test
    public void convert_ImageXdimAndYDim_CreatesWidthAndHeightAttributesInDocumentElement() {
        int xdim = 10;
        int ydim = 20;

        SVGDocument document = _createDocument();
        
        ToSVGDocumentElementConverter converter = new ToSVGDocumentElementConverter();
        converter.setImageDim(xdim, ydim);
        converter.convert(document, _NAME_SPACE);

        Element documentElement = document.getDocumentElement();

        assertTrue(documentElement.getAttributeNS(_NAME_SPACE, _WIDTH_ATTR).equals(String.valueOf(xdim)));
        assertTrue(documentElement.getAttributeNS(_NAME_SPACE, _HEIGHT_ATTR).equals(String.valueOf(ydim)));
    }

    private SVGDocument _createDocument() {
        return (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(_NAME_SPACE, "svg", null);
    }
}