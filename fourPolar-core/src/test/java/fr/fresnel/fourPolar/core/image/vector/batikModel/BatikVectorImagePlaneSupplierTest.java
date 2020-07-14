package fr.fresnel.fourPolar.core.image.vector.batikModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class BatikVectorImagePlaneSupplierTest {
    private static final String _WIDTH_ATTR = "width";
    private static final String _HEIGHT_ATTR = "height";

    @Test
    public void get_Imagexdim1yDim2_CreatesSVGDocWithCorrectWidthHeightAttribute() {
        int xdim = 1;
        int ydim = 2;
        BatikVectorImagePlaneSupplier supplier = new BatikVectorImagePlaneSupplier(xdim, ydim);

        SVGDocument svgDocument = supplier.get();
        Element documentElement = svgDocument.getDocumentElement();
        
        String namespaceURI = documentElement.getNamespaceURI();
        assertTrue(documentElement.getAttributeNS(namespaceURI, _WIDTH_ATTR).equals(String.valueOf(xdim)));
        assertTrue(documentElement.getAttributeNS(namespaceURI, _HEIGHT_ATTR).equals(String.valueOf(ydim)));

    }
}