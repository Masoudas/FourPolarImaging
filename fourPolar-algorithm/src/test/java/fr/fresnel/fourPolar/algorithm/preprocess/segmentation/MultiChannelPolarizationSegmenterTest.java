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
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class MultiChannelPolarizationSegmenterTest {
    @Test
    public void segment_OneMultiChannelXYCImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 3, 3 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 5, 3 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 3, 5 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 5, 5 }, AxisOrder.XY);

        int numChannels = 3;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels }, new int[] { 1, 2, 3 }) };

        Image<UINT16>[] image_pol0 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0,
                numChannels);
        Image<UINT16>[] image_pol45 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45,
                numChannels);
        Image<UINT16>[] image_pol90 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90,
                numChannels);
        Image<UINT16>[] image_pol135 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135,
                numChannels);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(SCPSImageChecker._checkImage(image_pol0[i], 0));
            assertTrue(SCPSImageChecker._checkImage(image_pol45[i], 1));
            assertTrue(SCPSImageChecker._checkImage(image_pol90[i], 2));
            assertTrue(SCPSImageChecker._checkImage(image_pol135[i], 3));

        }
    }

    @Test
    public void segment_TwoMultiChannelXYCImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 3, 3 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 5, 3 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 3, 5 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 5, 5 }, AxisOrder.XY);

        int numChannels = 6;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels / 2 }, new int[] { 1, 2, 3 }),
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels / 2 },
                        new int[] { 4, 5, 6 }) };

        Image<UINT16>[] image_pol0 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0,
                numChannels);
        Image<UINT16>[] image_pol45 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45,
                numChannels);
        Image<UINT16>[] image_pol90 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90,
                numChannels);
        Image<UINT16>[] image_pol135 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135,
                numChannels);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(SCPSImageChecker._checkImage(image_pol0[i], 0));
            assertTrue(SCPSImageChecker._checkImage(image_pol45[i], 1));
            assertTrue(SCPSImageChecker._checkImage(image_pol90[i], 2));
            assertTrue(SCPSImageChecker._checkImage(image_pol135[i], 3));

        }
    }

    @Test
    public void segment_TwoMultiChannelXYZCTImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 3, 3 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 5, 3 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 3, 5 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 5, 5 }, AxisOrder.XY);

        int numChannels = 6;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYZCT, new long[] { 4, 4, 3, numChannels / 2, 1 },
                        new int[] { 1, 2, 3 }),
                new MCPSDummyCapturedImage(AxisOrder.XYZCT, new long[] { 4, 4, 3, numChannels / 2, 4 },
                        new int[] { 4, 5, 6 }) };

        Image<UINT16>[] image_pol0 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0,
                numChannels);
        Image<UINT16>[] image_pol45 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45,
                numChannels);
        Image<UINT16>[] image_pol90 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90,
                numChannels);
        Image<UINT16>[] image_pol135 = new MultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135,
                numChannels);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(SCPSImageChecker._checkImage(image_pol0[i], 0));
            assertTrue(SCPSImageChecker._checkImage(image_pol45[i], 1));
            assertTrue(SCPSImageChecker._checkImage(image_pol90[i], 2));
            assertTrue(SCPSImageChecker._checkImage(image_pol135[i], 3));

        }
    }

}

class MCPSImageChecker {
    /**
     * For each plane, sets (0,0)-> 0 + channelNo, (1,0)-> 1 + channelNo, (0,1)-> 2
     * + channelNo, (1,1)-> 3 + channelNo.
     */
    public static void _setImage(Image<UINT16> image) {
        int c_axis = image.getMetadata().axisOrder().c_axis;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            int channel = (int) position[c_axis];
            if (position[0] < 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(0 + channel)));
            } else if (position[0] >= 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(1 + channel)));
            } else if (position[0] < 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(2 + channel)));
            } else if (position[0] >= 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(3 + channel)));
            }
        }
    }

    /**
     * Checks all values equal value.
     */
    public static boolean _checkImage(Image<UINT16> image, int value) {
        if (!image.getCursor().hasNext()) {
            return false;
        }

        int c_axis = image.getMetadata().axisOrder().c_axis;
        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {
            long[] position = cursor.localize();
            IPixel<UINT16> pixel = cursor.next();
            equals &= pixel.value().get() == value + position[c_axis];
        }
        return equals;
    }
}

class MCPSDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public MCPSDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
        metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();
        image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        SCPSImageChecker._setImage(image);
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
        return this.metadata.numChannels();
    }

    @Override
    public int[] channels() {
        return this.channels;
    }

}