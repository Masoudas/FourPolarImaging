package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;

public class BatikSVGVectorImageReaderTest {
    private String strokeWidthAttr = "stroke-width";

    @Test
    public void read_ImageWithNoYamlMetadata_ThrowsVectorImageIOIssues() {
        File root = _createRoot("BatikReaderNoMetadata");
        String imageName = "xyImage";

        VectorImageIOIssues exception = assertThrows(VectorImageIOIssues.class, () -> {
            new BatikSVGVectorImageReader().read(root, imageName);
        });

        assertTrue(exception.getMessage().equals("Can't read vector image because metadata can't be read."));
    }

    @Test
    public void read_ImageWithInconsistentMetadata_ThrowsVectorImageIOIssues() {
        File root = _createRoot("BatikReaderInconsistentMetadata");
        String imageName = "xyImage";

        VectorImageIOIssues exception = assertThrows(VectorImageIOIssues.class, () -> {
            new BatikSVGVectorImageReader().read(root, imageName);
        });

        assertTrue(exception.getMessage()
                .equals("Can't read vector image due to inconsistency between metadata and planes."));
    }

    @Test
    public void read_ImageWithMissingPlane_ThrowsVectorImageIOIssues() {
        File root = _createRoot("BatikReaderMissingPlane");
        String imageName = "xyImage";

        VectorImageIOIssues exception = assertThrows(VectorImageIOIssues.class, () -> {
            new BatikSVGVectorImageReader().read(root, imageName);
        });

        assertTrue(exception.getMessage().equals("Can't read " + imageName + ".svg" + " plane of the vector image"));
    }

    @Test
    public void read_ImageWithUndefinedWidthAndHeight_ThrowsVectorImageIOIssues() {
        File root = _createRoot("BatikReaderMissingWidthAndHeight");
        String imageName = "xyImage";

        VectorImageIOIssues exception = assertThrows(VectorImageIOIssues.class, () -> {
            new BatikSVGVectorImageReader().read(root, imageName);
        });

        assertTrue(exception.getMessage()
                .equals("Can't read vector image due to inconsistency between metadata and planes."));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void read_XYImageWithStrokeWidthEqualToPlaneIndex_ReturnsCorrectPlanes() throws VectorImageIOIssues {
        File root = _createRoot("BatikReaderXYImage");
        String imageName = "xyImage";

        VectorImage vectorImage = new BatikSVGVectorImageReader().read(root, imageName);

        assertTrue(_isStrokeWidthEqualPlaneIndex((ImagePlaneAccessor<SVGDocument>) vectorImage));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void read_XYCZTImageWithStrokeWidthEqualToPlaneIndex_ReturnsCorrectPlanes() throws VectorImageIOIssues {
        File root = _createRoot("BatikReaderXYCZTImage");
        String imageName = "xycztImage";

        VectorImage vectorImage = new BatikSVGVectorImageReader().read(root, imageName);

        assertTrue(_isStrokeWidthEqualPlaneIndex((ImagePlaneAccessor<SVGDocument>) vectorImage));
    }

    private boolean _isStrokeWidthEqualPlaneIndex(ImagePlaneAccessor<SVGDocument> planeAccessor) {
        boolean swEqual = true;
        for (int planeIndex = 1; planeIndex <= planeAccessor.numPlanes() && swEqual; planeIndex++) {
            SVGDocument plane = planeAccessor.getImagePlane(planeIndex).getPlane();
            String sw = plane.getDocumentElement().getAttributeNS(null, strokeWidthAttr);
            swEqual = sw.equals(String.valueOf(planeIndex));
        }

        return swEqual;
    }

    private File _createRoot(String rootName) {
        String testResource = BatikSVGVectorImageReaderTest.class.getResource("").getPath();
        File root = new File(testResource, rootName);
        root.mkdirs();
        return root;
    }
}