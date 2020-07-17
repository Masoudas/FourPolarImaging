package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.image;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.ImageUtil;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

public class ImageToSVGElementConverterTest {
    /**
     * A 2*2 image with pixels [[1 1][1 1]] represented as string.
     */
    private static String img_UINT16_1111_asPNGString = "data:image/png;base64, "
            + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAACEAAAAAAHTY67AAAADklEQVR42mNgYARCEAEAABwABeTegy4AAAAASUVORK5CYII=";

    /**
     * A 2*2 image with pixels [[2 2][2 2]] represented as string.
     */
    private static String img_UINT16_2222_asPNGString = "data:image/png;base64, "
            + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAACEAAAAAAHTY67AAAADklEQVR42mNgYAJCEAEAAC4ACbmdh3EAAAAASUVORK5CYII=";

    private static final String _IMG_TAG = "image";

    private static final String _X_ATTR = "x";
    private static final String _Y_ATTR = "y";
    private static final String _WIDTH_ATTR = "width";
    private static final String _HEIGHT_ATTR = "height";
    private static final String _ASPECT_RATIO_ATTR = "preserveAspectRatio";
    private static final String _HREF_ATTR = "xlink:href";

    @Test
    public void convert_UINT16XYImage_CreatesCorrectImageElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(image, 1, 1);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element imageElement = ImageToSVGElementConverter.convert(image, UINT16.zero(), 1, svgDocument);

        assertTrue(_isAttributesCorrect(imageElement, metadata, img_UINT16_1111_asPNGString));
    }

    @Test
    public void convert_UINT16XYZImageThirdPlane_CreatesCorrectImageElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int imagePlane = 3;
        _setPlaneToValue(image, imagePlane, 1);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element imageElement = ImageToSVGElementConverter.convert(image, UINT16.zero(), imagePlane, svgDocument);

        assertTrue(_isAttributesCorrect(imageElement, metadata, img_UINT16_1111_asPNGString));
    }

    @Test
    public void convert_UINT16XYZTImageFifthPlane_CreatesCorrectImageElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3, 4 }).axisOrder(AxisOrder.XYZT).build();
        Image<UINT16> image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());

        int imagePlane = 5;
        _setPlaneToValue(image, imagePlane, 1);

        SVGDocument svgDocument = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);

        Element imageElement = ImageToSVGElementConverter.convert(image, UINT16.zero(), imagePlane, svgDocument);

        assertTrue(_isAttributesCorrect(imageElement, metadata, img_UINT16_1111_asPNGString));
    }

    private boolean _isAttributesCorrect(Element imageElement, IMetadata metadata, String imgAsString) {
        long[] dim = MetadataUtil.getPlaneDim(metadata);

        boolean attrsEqual = true;
        attrsEqual &= imageElement.getTagName().equals(_IMG_TAG);
        attrsEqual &= imageElement.getAttributeNS(null, _X_ATTR).equals("0");
        attrsEqual &= imageElement.getAttributeNS(null, _Y_ATTR).equals("0");
        attrsEqual &= imageElement.getAttributeNS(null, _WIDTH_ATTR).equals(String.valueOf(dim[0] - 1));
        attrsEqual &= imageElement.getAttributeNS(null, _HEIGHT_ATTR).equals(String.valueOf(dim[1] - 1));
        attrsEqual &= imageElement.getAttributeNS(null, _ASPECT_RATIO_ATTR).equals("none");

        String hrefAsString = imageElement.getAttributeNS(null, _HREF_ATTR).replace(System.getProperty("line.separator"), "");
        attrsEqual &= hrefAsString.equals(imgAsString);

        return attrsEqual;
    }

    private void _setPlaneToValue(Image<UINT16> image, long planeIndex, int value) {
        IPixelCursor<UINT16> pixelCursor = ImageUtil.getPlaneCursor(image, planeIndex);
        while (pixelCursor.hasNext()) {
            IPixel<UINT16> pixel = pixelCursor.next();
            pixel.value().set(value);
            pixelCursor.setPixel(pixel);
        }
    }
}