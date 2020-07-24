package fr.fresnel.fourPolar.core.image.vector.batikModel;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.vector.Vector;
import fr.fresnel.fourPolar.core.image.vector.VectorImage;
import fr.fresnel.fourPolar.core.image.vector.VectorImageFactory;
import fr.fresnel.fourPolar.core.image.vector.animation.Animation;
import fr.fresnel.fourPolar.core.image.vector.filter.BlenderFilter;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterComposite;
import fr.fresnel.fourPolar.core.image.vector.filter.FilterCompositeBuilder;
import fr.fresnel.fourPolar.core.shape.IBoxShape;
import fr.fresnel.fourPolar.core.shape.ILineShape;
import fr.fresnel.fourPolar.core.shape.IShape;
import fr.fresnel.fourPolar.core.shape.ShapeFactory;
import fr.fresnel.fourPolar.core.util.image.generic.ImageUtil;

public class BatikVectorImageTest {
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

    @Test
    public void init_1DImage_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2 }).axisOrder(AxisOrder.NoOrder).build();

        assertThrows(IllegalArgumentException.class, () -> {
            new BatikVectorImage(metadata, new DummyBatikFactory());
        });

    }

    @Test
    public void getImagePlane_XYImage_ImageHasOnlyOnePlane() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 1);

    }

    @Test
    public void getImagePlane_XYZImage223_ImageHasThreePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 3);

        // Check planes are unique
        TreeSet<Integer> planes = new TreeSet<>();
        for (int planeIndex = 1; planeIndex <= 3; planeIndex++) {
            planes.add(vectorImage.getImagePlane(planeIndex).getPlane().hashCode());
        }
        assertTrue(planes.size() == 3);
    }

    @Test
    public void getImagePlane_XYZTImage2213_ImageHasThreePlanes() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1, 3 }).axisOrder(AxisOrder.XYZT).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        assertTrue(vectorImage.numPlanes() == 3);

        // Check planes are unique
        TreeSet<Integer> planes = new TreeSet<>();
        for (int planeIndex = 1; planeIndex <= 3; planeIndex++) {
            planes.add(vectorImage.getImagePlane(planeIndex).getPlane().hashCode());
        }
        assertTrue(planes.size() == 3);

    }

    @Test
    public void setFilterComposite_FilterCompositeWithDummyFilterInXYImage_AddsFilterToDefs() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        FilterComposite composite = new FilterCompositeBuilder("DummyFilter", new DummyFilter()).build();

        vectorImage.addFilterComposite(composite);

        SVGDocument plane = vectorImage.getImagePlane(1).getPlane();
        Element defsElement = (Element) plane.getDocumentElement()
                .getElementsByTagNameNS(plane.getNamespaceURI(), "defs").item(0);

        NodeList defsChildren = defsElement.getChildNodes();

        // Only one filter element exists for the child.
        assertTrue(defsChildren.getLength() == 1);

        Element filter = (Element) defsChildren.item(0);
        assertTrue(filter.getTagName().equals("filter"));
    }

    @Test
    public void setFilterComposite_FilterCompositeWithDummyFilterInXYZImage_AddsFilterToDefs() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).axisOrder(AxisOrder.XYZ).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        FilterComposite composite = new FilterCompositeBuilder("DummyFilter", new DummyFilter()).build();
        vectorImage.addFilterComposite(composite);

        for (int planeIndex = 1; planeIndex <= 3; planeIndex++) {
            SVGDocument plane = vectorImage.getImagePlane(planeIndex).getPlane();
            Element defsElement = (Element) plane.getDocumentElement()
                    .getElementsByTagNameNS(plane.getNamespaceURI(), "defs").item(0);

            NodeList defsChildren = defsElement.getChildNodes();

            // Only one filter element exists for the child.
            assertTrue(defsChildren.getLength() == 1);

            Element filter = (Element) defsChildren.item(0);
            assertTrue(filter.getTagName().equals("filter"));

        }

    }

    @Test
    public void setImage_ImagePlaneNotSameSizeAsBatikImage_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        IMetadata plane_metadata = new Metadata.MetadataBuilder(new long[] { 1, 1 }).axisOrder(AxisOrder.XY).build();
        Image<UINT16> imagePlane = new ImgLib2ImageFactory().create(plane_metadata, UINT16.zero());
        assertThrows(IllegalArgumentException.class, () -> {
            vectorImage.setImage(imagePlane, UINT16.zero());
        });
    }

    @Test
    public void setImage_ImageHasUnequalPlanesAsBatikImage_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 1 }).axisOrder(AxisOrder.XYZ).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        IMetadata plane_metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2 }).axisOrder(AxisOrder.XYZ)
                .build();
        Image<UINT16> imagePlane = new ImgLib2ImageFactory().create(plane_metadata, UINT16.zero());
        assertThrows(IllegalArgumentException.class, () -> {
            vectorImage.setImage(imagePlane, UINT16.zero());
        });
    }

    @Test
    public void setImage_22UINT16XYImage_AddsImageElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).axisOrder(AxisOrder.XY).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        Image<UINT16> imagePlane = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(imagePlane, 1, 1);
        vectorImage.setImage(imagePlane, UINT16.zero());

        SVGDocument plane = vectorImage.getImagePlane(1).getPlane();
        NodeList defsElement = plane.getDocumentElement().getElementsByTagNameNS(plane.getNamespaceURI(), "image");

        assertTrue(defsElement.getLength() == 1);

        Element imageElement = (Element) defsElement.item(0);
        String hrefAsString = imageElement.getAttributeNS(null, "xlink:href")
                .replace(System.getProperty("line.separator"), "");
        assertTrue(hrefAsString.equals(img_UINT16_1111_asPNGString));
    }

    @Test
    public void setImage_222UINT16XYImage_AddsImageElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 2 }).axisOrder(AxisOrder.XYZ).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        Image<UINT16> imagePlane = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        _setPlaneToValue(imagePlane, 1, 1);
        _setPlaneToValue(imagePlane, 2, 2);
        vectorImage.setImage(imagePlane, UINT16.zero());

        String[] planesAsString = { img_UINT16_1111_asPNGString, img_UINT16_2222_asPNGString };
        for (int planeIndex = 1; planeIndex <= 2; planeIndex++) {
            SVGDocument plane = vectorImage.getImagePlane(planeIndex).getPlane();
            NodeList defsElement = plane.getDocumentElement().getElementsByTagNameNS(plane.getNamespaceURI(), "image");

            assertTrue(defsElement.getLength() == 1);

            Element imageElement = (Element) defsElement.item(0);
            String hrefAsString = imageElement.getAttributeNS(null, "xlink:href")
                    .replace(System.getProperty("line.separator"), "");
            hrefAsString.equals(planesAsString[planeIndex - 1]);
        }
    }

    @Test
    public void setVector_ShapeWithUnequalSpaceDim_ThrowsIllegalArgumentException() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        DummyVector vector = new DummyVector();
        vector.setShape(
                ShapeFactory.closedBox(new long[] { 0, 0, 0 }, new long[] { 1, 1, 1 }, AxisOrder.NoOrder));

        assertThrows(IllegalArgumentException.class, () -> {
            vectorImage.addVector(vector);
        });
    }

    @Test
    public void setVector_2DVectorIn2DImage_AddsVectorElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2 }).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        DummyVector vector = new DummyVector();
        vector.setShape(
                ShapeFactory.closedBox(new long[] { 0, 0 }, new long[] { 1, 1 }, AxisOrder.NoOrder));

        vectorImage.addVector(vector);

        SVGDocument plane = vectorImage.getImagePlane(1).getPlane();
        NodeList vectElement = plane.getDocumentElement().getElementsByTagNameNS(plane.getNamespaceURI(), "rect");

        assertTrue(vectElement.getLength() == 1);
    }

    @Test
    public void setVector_A2DVectorPerPlaneIn3DImage_AddsVectorElement() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 2, 2, 3 }).build();
        BatikVectorImage vectorImage = new BatikVectorImage(metadata, new DummyBatikFactory());

        for (int thirdDim = 0; thirdDim < 3; thirdDim++) {
            DummyVector vector = new DummyVector();
            vector.setShape(
                    ShapeFactory.closedBox(new long[] { thirdDim, 0, thirdDim }, new long[] { 4, 4, thirdDim }, AxisOrder.NoOrder));
            vectorImage.addVector(vector);        
        }

        for (int planeIndex = 1; planeIndex <= 3; planeIndex++){
            SVGDocument plane = vectorImage.getImagePlane(planeIndex).getPlane();
            NodeList vectElement = plane.getDocumentElement().getElementsByTagNameNS(plane.getNamespaceURI(), "rect");
    
            assertTrue(vectElement.getLength() == 1);
            Element rectElement = (Element)(vectElement.item(0));
            assertTrue(rectElement.getAttributeNS(null, "x").equals(String.valueOf(planeIndex - 1)));
        }

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

class DummyBatikFactory implements VectorImageFactory {

    @Override
    public VectorImage create(IMetadata metadata) {
        return null;
    }
}

/**
 * A dummy filter, which is just a wrapper for blender filter. This is to ensure
 * that a converter to svg element exists for the filter
 */
class DummyFilter extends BlenderFilter {

    public DummyFilter() {
        super(IN.SOURCE_GRAPHIC, Mode.MULTIPLY, null);
    }

}

class DummyVector implements Vector {
    private IBoxShape _shape;

    @Override
    public Optional<FilterComposite> filter() {
        return Optional.ofNullable(null);
    }

    @Override
    public IShape shape() {
        return _shape;
    }

    @Override
    public Optional<ARGB8> color() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<ARGB8> fill() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<Integer> strokeWidth() {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<Animation> animation() {
        return Optional.ofNullable(null);
    }

    @Override
    public void setShape(IBoxShape shape) {
        _shape = shape;
    }

    @Override
    public void setShape(ILineShape shape) {
        throw new NoSuchMethodError();

    }

    @Override
    public void setColor(ARGB8 color) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setFill(ARGB8 color) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setStrokeWidth(int width) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setFilter(FilterComposite composite) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setAnimation(Animation animation) {
        throw new NoSuchMethodError();
    }

}