package fr.fresnel.fourPolar.core.image.vector.batikModel.converters.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.util.Base64EncoderStream;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.AWTModel.AWTBufferedImage;
import fr.fresnel.fourPolar.core.image.generic.AWTModel.ImageToAWTBufferedImageConverter;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

/**
 * Using this class, we can convert a particular plane of a {@link Image} to an
 * SVG "image" element, For this end, the given plane is first converted to an
 * {@link AWTBufferedImage}, and is then converted to an string representation
 * using {@link Base64EncoderStream}.
 * 
 */
public class ImageToSVGElementConverter {
    private static final String _IMG_TAG = "image";

    private static final String _X_ATTR = "x";
    private static final String _Y_ATTR = "y";
    private static final String _WIDTH_ATTR = "width";
    private static final String _HEIGHT_ATTR = "height";
    private static final String _ASPECT_RATIO_ATTR = "preserveAspectRatio";
    private static final String _HREF_ATTR = "xlink:href";

    private static final String _START_X_PIXEL = "0";
    private static final String _START_Y_PIXEL = "0";
    private static final String _ASPECT_RATIO = "none";
    private static final String _HREF_START_STRING = "data:image/png;base64, ";

    /**
     * Convert the demanded plane of the image to an SVG "image" element.
     * 
     * @param image       is the image instance.
     * @param pixelType   is the pixel type of the image.
     * @param planeIndex  is the plane index to be inverted to image element.
     * @param svgDocument is the svg document for which the element will be created.
     * 
     * @throws IllegalArgumentException if the image is not at least 2D, or if the
     *                                  plane index does not exist.
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> Element convert(Image<T> image, T pixelType, long planeIndex,
            SVGDocument svgDocument) {
        AWTBufferedImage<UINT16> imagePlane = null;
        switch (pixelType.getType()) {
            case UINT_16:
                imagePlane = _getPlaneAsUINT16BufferedImage((Image<UINT16>) image, planeIndex);
                break;

            default:
                throw new IllegalArgumentException("No converter to plane is found for this pixel type.");
        }

        Element imgElement = _createBaseImageElement(imagePlane, svgDocument);
        _addImageToImageElementAsString(imagePlane, imgElement);

        return imgElement;
    }

    private static Element _createBaseImageElement(AWTBufferedImage<UINT16> imagePlane, SVGDocument svgDocument) {
        long[] dim = MetadataUtil.getPlaneDim(imagePlane.getMetadata());

        Element imgElement = svgDocument.createElementNS(svgDocument.getNamespaceURI(), _IMG_TAG);
        imgElement.setAttributeNS(null, _X_ATTR, _START_X_PIXEL);
        imgElement.setAttributeNS(null, _Y_ATTR, _START_Y_PIXEL);
        imgElement.setAttributeNS(null, _WIDTH_ATTR, String.valueOf(dim[0] - 1));
        imgElement.setAttributeNS(null, _HEIGHT_ATTR, String.valueOf(dim[1] - 1));
        imgElement.setAttributeNS(null, _ASPECT_RATIO_ATTR, _ASPECT_RATIO);

        return imgElement;
    }

    private static <T extends PixelType> void _addImageToImageElementAsString(AWTBufferedImage<T> imagePlane,
            Element imgElement) {
        ByteArrayOutputStream imgAsStream = _getImageAsByteArrayStream(imagePlane);
        imgElement.setAttributeNS(null, _HREF_ATTR, _HREF_START_STRING + imgAsStream.toString());
    }

    private static AWTBufferedImage<UINT16> _getPlaneAsUINT16BufferedImage(Image<UINT16> image, long planeIndex) {
        return ImageToAWTBufferedImageConverter.convertPlane(image, UINT16.zero(), planeIndex);
    }

    private static <T extends PixelType> ByteArrayOutputStream _getImageAsByteArrayStream(
            AWTBufferedImage<T> imagePlane) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Base64EncoderStream b64Encoder = new Base64EncoderStream(os);
        ImageWriter writerImage = ImageWriterRegistry.getInstance().getWriterFor("image/png");

        // The following exception should not be caught, because we're not writing to
        // disk. But we catch and rethrow it just in case.
        try {
            writerImage.writeImage(imagePlane.getImagePlane(1).getPlane(), b64Encoder);
            b64Encoder.close();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't convert image due to IO issues!");
        }
        return os;
    }

}