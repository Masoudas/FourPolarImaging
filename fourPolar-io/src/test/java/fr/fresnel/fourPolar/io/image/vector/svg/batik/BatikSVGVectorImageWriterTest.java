package fr.fresnel.fourPolar.io.image.vector.svg.batik;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.junit.jupiter.api.Test;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.ImagePlane;
import fr.fresnel.fourPolar.core.image.ImagePlaneAccessor;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.io.exceptions.image.vector.VectorImageIOIssues;

public class BatikSVGVectorImageWriterTest {
    private String qualifiedName = "svg";
    private String strokeWidthAttr = "stroke-width";
    private SAXSVGDocumentFactory _svgReader = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

    @Test
    public void write_ImageWithNoAxisOrder_ThrowsVectorImageIOIssues() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.NoOrder).build();
        DummyBatikImage vectorImage = new DummyBatikImage(metadata, _createPlainSVGDocuments(1));

        File root = new File("");
        String imageName = "";

        VectorImageIOIssues exception = assertThrows(VectorImageIOIssues.class, () -> {
            new BatikSVGVectorImageWriter().write(root, imageName, vectorImage);
        });
        assertTrue(exception.getMessage().equals("Can't write a vector image with no axis-order."));
    }

    @Test
    public void write_XYImage_WritesPlaneAndMetadata() throws VectorImageIOIssues {
        int numPlanes = 1;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY).build();
        DummyBatikImage vectorImage = new DummyBatikImage(metadata, _createPlainSVGDocuments(numPlanes));

        File root = _createRoot("BatikWriterXYImage");
        String imageName = "xyImage";

        new BatikSVGVectorImageWriter().write(root, imageName, vectorImage);
        assertTrue(_isMetadataWrittenAsYaml(root, imageName));
        assertTrue(_arePlanesWritten(metadata, root, imageName, numPlanes));

    }

    @Test
    public void write_XYZImageWithDifferentStrokeWidthPerPlane_WritesPlaneAndMetadata()
            throws MalformedURLException, IOException {
        int numPlanes = 3;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 3 }).axisOrder(AxisOrder.XYZ).build();
        DummyBatikImage vectorImage = new DummyBatikImage(metadata,
                _createSVGDocumentsWithStrokeWidthEqualToPlaneIndex(numPlanes));

        File root = _createRoot("BatikWriterXYZImage");
        String imageName = "xyzImage";

        new BatikSVGVectorImageWriter().write(root, imageName, vectorImage);
        assertTrue(_isMetadataWrittenAsYaml(root, imageName));
        assertTrue(_arePlanesWritten(metadata, root, imageName, numPlanes));
        assertTrue(_isStrokeWidthEqualPlaneIndex(metadata, root, imageName, numPlanes));

    }

    @Test
    public void write_XYCZTImageWithDifferentStrokeWidthPerPlane_WritesPlaneAndMetadata()
            throws MalformedURLException, IOException {
        int numPlanes = 10;
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 1, 1, 1, 2, 5 }).axisOrder(AxisOrder.XYCZT)
                .build();
        DummyBatikImage vectorImage = new DummyBatikImage(metadata,
                _createSVGDocumentsWithStrokeWidthEqualToPlaneIndex(numPlanes));

        File root = _createRoot("BatikWriterXYCZTImage");
        String imageName = "xycztImage";

        new BatikSVGVectorImageWriter().write(root, imageName, vectorImage);
        assertTrue(_isMetadataWrittenAsYaml(root, imageName));
        assertTrue(_arePlanesWritten(metadata, root, imageName, numPlanes));
        assertTrue(_isStrokeWidthEqualPlaneIndex(metadata, root, imageName, numPlanes));

    }

    private boolean _isMetadataWrittenAsYaml(File root, String imageName) {
        return new File(root, imageName + ".yaml").exists();
    }

    private boolean _arePlanesWritten(IMetadata metadata, File root, String imageName, int numPlanes) {
        BatikSVGVectorImagePathCreator creator = new BatikSVGVectorImagePathCreator(metadata, root, imageName);

        boolean planeIsWritten = true;
        for (int planeIndex = 1; planeIndex <= numPlanes && planeIsWritten; planeIndex++) {
            planeIsWritten = creator.createPlaneImageFile(planeIndex).exists();
        }

        return planeIsWritten;
    }

    private boolean _isStrokeWidthEqualPlaneIndex(IMetadata metadata, File root, String imageName, int numPlanes)
            throws MalformedURLException, IOException {
        BatikSVGVectorImagePathCreator creator = new BatikSVGVectorImagePathCreator(metadata, root, imageName);

        boolean swEqual = true;
        for (int planeIndex = 1; planeIndex <= numPlanes && swEqual; planeIndex++) {
            SVGDocument plane = _readSVGDocument(creator.createPlaneImageFile(planeIndex));
            String sw = plane.getDocumentElement().getAttributeNS(null, strokeWidthAttr);
            swEqual = sw.equals(String.valueOf(planeIndex));
        }

        return swEqual;
    }

    private SVGDocument _readSVGDocument(File path) throws MalformedURLException, IOException {
        return (SVGDocument) _svgReader.createDocument(path.toURI().toURL().getPath());
    }

    private File _createRoot(String rootName) {
        String testResource = BatikSVGVectorImageWriterTest.class.getResource("").getPath();
        File root = new File(testResource, rootName);
        root.mkdirs();
        return root;
    }

    private SVGDocument[] _createPlainSVGDocuments(int nDocs) {
        SVGDocument[] docs = new SVGDocument[nDocs];

        for (int i = 0; i < docs.length; i++) {
            docs[i] = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                    .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, qualifiedName, null);
        }

        return docs;
    }

    /**
     * Just to set a random property different for each plane.
     */
    private SVGDocument[] _createSVGDocumentsWithStrokeWidthEqualToPlaneIndex(int nDocs) {
        SVGDocument[] docs = new SVGDocument[nDocs];

        for (int i = 0; i < docs.length; i++) {
            docs[i] = (SVGDocument) SVGDOMImplementation.getDOMImplementation()
                    .createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
            docs[i].getDocumentElement().setAttributeNS(null, strokeWidthAttr, String.valueOf(i + 1));
        }

        return docs;
    }

}

class DummyBatikImage implements ImagePlaneAccessor<SVGDocument>, VectorImage {
    IMetadata metadata;
    SVGDocument[] planes;

    public DummyBatikImage(IMetadata metadata, SVGDocument[] planes) {
        this.metadata = metadata;
        this.planes = planes;
    }

    @Override
    public int getPlaneIndex(long[] position) {
        return -1;
    }

    @Override
    public ImagePlane<SVGDocument> getImagePlane(int planeIndex) {
        return new DummyWriterImagePlane(planeIndex, planes[planeIndex - 1]);
    }

    @Override
    public int numPlanes() {
        return planes.length;
    }

    @Override
    public IMetadata metadata() {
        return metadata;
    }

    @Override
    public VectorImageFactory getFactory() {
        return null;
    }

    @Override
    public void addFilterComposite(FilterComposite composite) {

    }

    @Override
    public <T extends PixelType> void setImage(Image<T> image, T pixelType) {

    }

    @Override
    public void addVector(Vector vector) {

    }

}

class DummyWriterImagePlane implements ImagePlane<SVGDocument> {
    int planeIndex;
    SVGDocument plane;

    public DummyWriterImagePlane(int planeIndex, SVGDocument plane) {
        this.planeIndex = planeIndex;
        this.plane = plane;
    }

    @Override
    public int planeIndex() {
        return planeIndex;
    }

    @Override
    public SVGDocument getPlane() {
        return plane;
    }

}