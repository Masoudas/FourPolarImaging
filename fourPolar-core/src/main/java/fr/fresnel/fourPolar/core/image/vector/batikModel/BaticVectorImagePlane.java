package fr.fresnel.fourPolar.core.image.vector.batikModel;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 * A class that holds one plane of a {@link BatikVectorImage}, together with its
 * {@link SVGGraphics2D}.
 */
class BaticVectorImagePlane {
    final private int _planeIndex;
    final private SVGGraphics2D _imagePlane;

    public BaticVectorImagePlane(int planeIndex, int xdim, int ydim) {
        assert planeIndex > 0 : "planeIndex should be greater than zero";
        assert xdim > 0 && ydim > 0 : "image dimension should be greater than zero";

        _planeIndex = planeIndex;
        _imagePlane = _createSVGCanvas();
        _setCanvasProperties(xdim, ydim, graphics2d);
    }

    /**
     * Create the SVG canvas.
     */
    private SVGGraphics2D _createSVGCanvas() {
        SVGDocument document = _createSVGDocument();
        return new SVGGraphics2D(document);

    }

    /**
     * Sets the properties of the canvas.
     * 
     * @param xdim is the horizontal dimension of the canvas.
     * @param ydim is the vertical dimension of the canvas.
     */
    private void _setCanvasProperties(int xdim, int ydim) {
        Element root = _imagePlane.getRoot();

        root.setAttributeNS(null, "width", String.valueOf(xdim));
        root.setAttributeNS(null, "height", String.valueOf(ydim));
    }

    /**
     * @return a backend document model used for the svg graphics canvas.
     */
    private SVGDocument _createSVGDocument() {
        DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
        return (SVGDocument) dom.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
    }

    /**
     * @return the plane index associated with this image.
     */
    public int planeIndex() {
        return _planeIndex;
    }

    /**
     * @return the underlying image.
     */
    public SVGGraphics2D getImage() {
        return _imagePlane;
    }
}