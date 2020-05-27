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

public class SampleMultiChannelPolarizationSegmenterTest {
    @Test
    public void segment_OneMultiChannelXYCImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        int numChannels = 3;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels }, new int[] { 1, 2, 3 }) };

        Image<UINT16>[] image_pol0 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(MCPSImageChecker._checkImage(image_pol0[i], 0, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol45[i], 1, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol90[i], 2, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol135[i], 3, i + 1));

        }
    }

    @Test
    public void segment_TwoMultiChannelXYCImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        int numChannels = 6;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels / 2 }, new int[] { 1, 2, 3 }),
                new MCPSDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, numChannels / 2 },
                        new int[] { 4, 5, 6 }) };

        Image<UINT16>[] image_pol0 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(MCPSImageChecker._checkImage(image_pol0[i], 0, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol45[i], 1, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol90[i], 2, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol135[i], 3, i + 1));

        }
    }

    @Test
    public void segment_TwoMultiChannelXYZCTImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        int numChannels = 6;
        ICapturedImage[] capturedImage = new ICapturedImage[] {
                new MCPSDummyCapturedImage(AxisOrder.XYZCT, new long[] { 4, 4, 3, numChannels / 2, 1 },
                        new int[] { 1, 2, 3 }),
                new MCPSDummyCapturedImage(AxisOrder.XYZCT, new long[] { 4, 4, 3, numChannels / 2, 4 },
                        new int[] { 4, 5, 6 }) };

        Image<UINT16>[] image_pol0 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol0);
        Image<UINT16>[] image_pol45 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol45);
        Image<UINT16>[] image_pol90 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol90);
        Image<UINT16>[] image_pol135 = new SampleMultiChannelPolarizationSegmenter().segment(capturedImage, fov_pol135);

        for (int i = 0; i < numChannels; i++) {
            assertTrue(MCPSImageChecker._checkImage(image_pol0[i], 0, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol45[i], 1, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol90[i], 2, i + 1));
            assertTrue(MCPSImageChecker._checkImage(image_pol135[i], 3, i + 1));

        }
    }

}

class MCPSImageChecker {
    /**
     * For each plane, sets (0,0)-> 0 + channelNo, (1,0)-> 1 + channelNo, (0,1)-> 2
     * + channelNo, (1,1)-> 3 + channelNo.
     */
    public static void _setImage(Image<UINT16> image, int[] channels) {
        int c_axis = image.getMetadata().axisOrder().c_axis;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            int channel = channels[(int)position[c_axis]];
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
    public static boolean _checkImage(Image<UINT16> image, int value, int channel) {
        if (!image.getCursor().hasNext()) {
            return false;
        }

        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {
            IPixel<UINT16> pixel = cursor.next();
            equals &= pixel.value().get() == value + channel;
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
        MCPSImageChecker._setImage(image, channels);
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