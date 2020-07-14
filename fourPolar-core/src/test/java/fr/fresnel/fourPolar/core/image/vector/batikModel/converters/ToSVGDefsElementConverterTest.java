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

import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.Filter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;

public class ToSVGDefsElementConverterTest {
    private static final String _NAME_SPACE = SVGDOMImplementation.SVG_NAMESPACE_URI;

    @Test
    public void convert_CompositeFilterContainingOneBlender_WritesFilterAsChildElementOfDefs() {
        FilterComposite composite = new TSDECDummyFilterComposite("DummyFilter", new DummyBlenderFilter());

        SVGDocument svgDocument = _createDocument();

        new ToSVGDefsElementConverter().setFilterComposite(new FilterComposite[] { composite })
                .convert(svgDocument, ToSVGDefsElementConverter.createDefsElement(svgDocument, _NAME_SPACE));

        Element documentElement = svgDocument.getDocumentElement();

        NodeList nodes = documentElement.getChildNodes();
        // Only one node is added.
        assertTrue(nodes.getLength() == 1);
        Element defsNode = (Element)nodes.item(0);
        assertTrue(defsNode.getTagName().equals("defs"));

        NodeList nodesOfDefs = defsNode.getChildNodes();
        // Only one node is added.
        assertTrue(nodesOfDefs.getLength() == 1);
        Element filterNode = (Element)nodesOfDefs.item(0);
        assertTrue(filterNode.getTagName().equals("filter"));

    }

    private SVGDocument _createDocument() {
        return (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(_NAME_SPACE, "svg", null);
    }
}

class DummyBlenderFilter extends BlenderFilter {

    public DummyBlenderFilter() {
        super(IN.SOURCE_GRAPHIC, BlenderFilter.Mode.MULTIPLY, null);

    }

}

class TSDECDummyFilterComposite implements FilterComposite {
    private String id;
    ArrayList<Filter> _filters = new ArrayList<>();

    TSDECDummyFilterComposite(String id, Filter filter) {
        this.id = id;
        this._filters.add(filter);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Optional<String> xStart() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> yStart() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> widthPercent() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> heightPercent() {
        return Optional.ofNullable(null);
    }

    @Override
    public Iterator<Filter> filters() {
        return _filters.iterator();
    }

}
