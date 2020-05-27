package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class BeadSingleChannelPolarizationSegmenterTest {

    @Test
    public void segment_OneSingleChannelXYImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new BSCPSDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 }, new int[] { 1 }) };

        Image<UINT16>[] image_pol0 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        assertTrue(BSCPSImageChecker._checkImage(image_pol0[0], 0));
        assertTrue(BSCPSImageChecker._checkImage(image_pol45[0], 1));
        assertTrue(BSCPSImageChecker._checkImage(image_pol90[0], 2));
        assertTrue(BSCPSImageChecker._checkImage(image_pol135[0], 3));
    }

    @Test
    public void segment_TwoSingleChannelXYImageOneSingleChannel_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new BSCPSDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 }, new int[] { 1 }),
                new BSCPSDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 }, new int[] { 2 }) };

        Image<UINT16>[] image_pol0 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        for (int i = 0; i < image_pol135.length; i++) {
            assertTrue(BSCPSImageChecker._checkImage(image_pol0[i], 0));
            assertTrue(BSCPSImageChecker._checkImage(image_pol45[i], 1));
            assertTrue(BSCPSImageChecker._checkImage(image_pol90[i], 2));
            assertTrue(BSCPSImageChecker._checkImage(image_pol135[i], 3));
        }
    }

    @Test
    public void segment_TwoSingleChannelXYCZTImageOneSingleChannel_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new BSCPSDummyCapturedImage(AxisOrder.XYCZT, new long[] { 4, 4, 1, 3, 2 }, new int[] { 1 }),
                new BSCPSDummyCapturedImage(AxisOrder.XYCZT, new long[] { 4, 4, 1, 3, 2 }, new int[] { 2 }) };

        Image<UINT16>[] image_pol0 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new BeadSingleChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        for (int i = 0; i < image_pol135.length; i++) {
            assertTrue(BSCPSImageChecker._checkImage(image_pol0[i], 0));
            assertTrue(BSCPSImageChecker._checkImage(image_pol45[i], 1));
            assertTrue(BSCPSImageChecker._checkImage(image_pol90[i], 2));
            assertTrue(BSCPSImageChecker._checkImage(image_pol135[i], 3));
        }
    }

}

class BSCPSImageChecker {
    /**
     * For each plane, sets (0,0)-> 0, (1,0)-> 1, (0,1)-> 2, (1,1)-> 3.
     */
    public static void _setImage(Image<UINT16> image) {
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            if (position[0] < 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(0)));
            } else if (position[0] >= 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(1)));
            } else if (position[0] < 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(2)));
            } else if (position[0] >= 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(3)));
            }
        }
    }

    /**
     * Checks all values equal value.
     */
    public static boolean _checkImage(Image<UINT16> image, int value) {
        if (!MetadataUtil.isImagePlanar(image.getMetadata())) {
            return false;
        }

        if (!image.getCursor().hasNext()) {
            return false;
        }

        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {
            IPixel<UINT16> pixel = cursor.next();
            equals &= pixel.value().get() == value;
        }
        return equals;
    }
}

class BSCPSDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public BSCPSDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
        metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();
        image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        BSCPSImageChecker._setImage(image);
        this.channels = channels;
    }

    @Override
    public ICapturedImageFile getCapturedImageFile() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public Image<UINT16> getImage() {
        return this.image;
    }

    @Override
    public int numChannels() {
        return this.channels.length;
    }

    @Override
    public int[] channels() {
        return this.channels;
    }

}