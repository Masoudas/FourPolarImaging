package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.Pixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * For the sake of simplicity of test, we use a intersection point for
 * polarizations.
 * 
 * Note that presence of one or multiple cameras essentially does not affect how
 * images are segmented (it's just a matter of getting the right image that
 * contains the polarization of the channel). Hence, we perform all tests as if
 * only camera is present.
 */
public class SampleImagePolarizationViewCreatorTest {
    @Test
    public void segment_OneSingleChannelXYImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4 };
        long[] viewDimAfterSegment = new long[] { 2, 2 };

        AxisOrder capImage_axisOrder = AxisOrder.XY;
        int[] capImage_channels = new int[] { 1 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RIPVCDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RIPVCDummyCapturedImageSet capturedImageSet = new RIPVCDummyCapturedImageSet(false);
        capturedImageSet.setFileSet(capturedImage);

        SampleImagePolarizationViewCreator creator = new SampleImagePolarizationViewCreator(fov);
        Map<Polarization, PolarizationView> views = creator.create(capturedImageSet, capImage_channels[0]);

        _checkViews(views, viewDimAfterSegment, capImage_channels[0]);

    }

    @Test
    public void segment_OneSingleChannelXYZImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 3 };
        long[] viewDimAfterSegment = new long[] { 2, 2, 3 };

        AxisOrder capImage_axisOrder = AxisOrder.XYZ;
        int[] capImage_channels = new int[] { 1 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RIPVCDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RIPVCDummyCapturedImageSet capturedImageSet = new RIPVCDummyCapturedImageSet(false);
        capturedImageSet.setFileSet(capturedImage);

        SampleImagePolarizationViewCreator creator = new SampleImagePolarizationViewCreator(fov);
        Map<Polarization, PolarizationView> views = creator.create(capturedImageSet, capImage_channels[0]);

        _checkViews(views, viewDimAfterSegment, capImage_channels[0]);

    }

    @Test
    public void segment_OneSingleChannelXYCZTImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 1, 3, 4 };
        long[] viewDimAfterSegment = new long[] { 2, 2, 1, 3, 4 };

        AxisOrder capImage_axisOrder = AxisOrder.XYCZT;
        int[] capImage_channels = new int[] { 1 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RIPVCDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RIPVCDummyCapturedImageSet capturedImageSet = new RIPVCDummyCapturedImageSet(false);
        capturedImageSet.setFileSet(capturedImage);

        SampleImagePolarizationViewCreator creator = new SampleImagePolarizationViewCreator(fov);
        Map<Polarization, PolarizationView> views = creator.create(capturedImageSet, capImage_channels[0]);

        _checkViews(views, viewDimAfterSegment, capImage_channels[0]);

    }

    @Test
    public void segment_OneTwoChannelXYCImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 2 };
        long[] viewDimAfterSegment = new long[] { 2, 2, 1 };

        AxisOrder capImage_axisOrder = AxisOrder.XYC;
        int[] capImage_channels = new int[] { 1, 2 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RIPVCDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RIPVCDummyCapturedImageSet capturedImageSet = new RIPVCDummyCapturedImageSet(true);
        capturedImageSet.setFileSet(capturedImage);

        SampleImagePolarizationViewCreator creator = new SampleImagePolarizationViewCreator(fov);
        Map<Polarization, PolarizationView> views_channel1 = creator.create(capturedImageSet, capImage_channels[0]);
        _checkViews(views_channel1, viewDimAfterSegment, capImage_channels[0]);

        Map<Polarization, PolarizationView> views_channel2 = creator.create(capturedImageSet, capImage_channels[1]);
        _checkViews(views_channel2, viewDimAfterSegment, capImage_channels[1]);
    }

    @Test
    public void segment_OneTwoChannelXYCZTImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 2, 3, 5 };
        long[] viewDimAfterSegment = new long[] { 2, 2, 1, 3, 5 };

        AxisOrder capImage_axisOrder = AxisOrder.XYCZT;
        int[] capImage_channels = new int[] { 1, 2 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RIPVCDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RIPVCDummyCapturedImageSet capturedImageSet = new RIPVCDummyCapturedImageSet(true);
        capturedImageSet.setFileSet(capturedImage);

        SampleImagePolarizationViewCreator creator = new SampleImagePolarizationViewCreator(fov);
        Map<Polarization, PolarizationView> views_channel1 = creator.create(capturedImageSet, capImage_channels[0]);
        _checkViews(views_channel1, viewDimAfterSegment, capImage_channels[0]);

        Map<Polarization, PolarizationView> views_channel2 = creator.create(capturedImageSet, capImage_channels[1]);
        _checkViews(views_channel2, viewDimAfterSegment, capImage_channels[1]);
    }

    private void _checkViews(Map<Polarization, PolarizationView> views, long[] viewDimAfterSegment, int channel) {
        assertArrayEquals(views.get(Polarization.pol0).getMetadata().getDim(), viewDimAfterSegment);
        assertArrayEquals(views.get(Polarization.pol45).getMetadata().getDim(), viewDimAfterSegment);
        assertArrayEquals(views.get(Polarization.pol90).getMetadata().getDim(), viewDimAfterSegment);
        assertArrayEquals(views.get(Polarization.pol135).getMetadata().getDim(), viewDimAfterSegment);

        assertTrue(RIPVCImageChecker._checkImage(views.get(Polarization.pol0), 0, channel));
        assertTrue(RIPVCImageChecker._checkImage(views.get(Polarization.pol45), 1, channel));
        assertTrue(RIPVCImageChecker._checkImage(views.get(Polarization.pol90), 2, channel));
        assertTrue(RIPVCImageChecker._checkImage(views.get(Polarization.pol135), 3, channel));
    }

    private FieldOfView _createFoV(long[] intersectionPoint, long[] capImage_dimension) {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 0, 0 }, intersectionPoint, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { intersectionPoint[0] + 1, 0 },
                new long[] { capImage_dimension[0] - 1, intersectionPoint[1] }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 0, intersectionPoint[1] + 1 },
                new long[] { intersectionPoint[0], capImage_dimension[1] - 1 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(
                new long[] { intersectionPoint[0] + 1, intersectionPoint[1] + 1 },
                new long[] { capImage_dimension[0] - 1, capImage_dimension[1] - 1 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);
        return fov;
    }

}

class SIPVCImageChecker {
    /**
     * For each plane, sets (0,0)-> 0 + channelNo, (1,0)-> 1 + channelNo, (0,1)-> 2
     * + channelNo, (1,1)-> 3 + channelNo. If no channel, add zero.
     */
    public static void _setImage(Image<UINT16> image, int[] channels, long[] intersectionPoint) {
        int c_axis = image.getMetadata().axisOrder().c_axis;
        int t_axis = image.getMetadata().axisOrder().t_axis;
        int z_axis = image.getMetadata().axisOrder().z_axis;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            int offset_c = c_axis > 0 ? channels[(int) position[c_axis]] : channels[0];
            int offset_z = z_axis > 0 ? (int) position[z_axis] : 0;
            int offset_t = t_axis > 0 ? (int) position[t_axis] : 0;

            if (position[0] < intersectionPoint[0] + 1 && position[1] < intersectionPoint[1] + 1) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(0 + offset_c + offset_z + offset_t)));
            } else if (position[0] >= intersectionPoint[0] + 1 && position[1] < intersectionPoint[1] + 1) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(1 + offset_c + offset_z + offset_t)));
            } else if (position[0] < intersectionPoint[0] + 1 && position[1] >= intersectionPoint[1] + 1) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(2 + offset_c + offset_z + offset_t)));
            } else if (position[0] >= intersectionPoint[0] + 1 && position[1] >= intersectionPoint[1] + 1) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(3 + offset_c + offset_z + offset_t)));
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

        int t_axis = image.getMetadata().axisOrder().t_axis;
        int z_axis = image.getMetadata().axisOrder().z_axis;

        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {
            IPixel<UINT16> pixel = cursor.next();

            long[] position = cursor.localize();

            int offset_z = z_axis > 0 ? (int) position[z_axis] : 0;
            int offset_t = t_axis > 0 ? (int) position[t_axis] : 0;

            equals &= pixel.value().get() == value + channel + offset_z + offset_t;
        }
        return equals;
    }
}

/**
 * Can hold only one captured image at a time (could be single channel or
 * multichannel)
 */
class SIPVCDummyCapturedImageSet implements ICapturedImageSet {
    ICapturedImage capturedImages;
    boolean isMultiChannel;

    public SIPVCDummyCapturedImageSet(boolean isMultiChannel) {
        this.isMultiChannel = isMultiChannel;

    }

    public void setFileSet(ICapturedImage capturedImage) {
        this.capturedImages = capturedImage;
    }

    @Override
    public ICapturedImage[] getCapturedImage(String label) {
        return new ICapturedImage[] { capturedImages };
    }

    @Override
    public ICapturedImageFileSet fileSet() {
        return new RIPVCDummyFileSet();
    }

    @Override
    public boolean hasMultiChannelImage() {
        return isMultiChannel;
    }

    @Override
    public ICapturedImage getChannelPolarizationImage(int channel, Polarization polarization) {
        return this.capturedImages;
    }

}

class SIPVCDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public SIPVCDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
        metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();
        image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
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
    public int[] channels() {
        return this.channels;
    }

}

class SIPVCDummyFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return null;
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        return null;
    }

    @Override
    public int[] getChannels() {
        return null;
    }

}