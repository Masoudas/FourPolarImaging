package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

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
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class RegistrationImageSegmenterTest {
    @Test
    public void segment_OneSingleChannelXYImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4 };

        AxisOrder capImage_axisOrder = AxisOrder.XY;
        int[] capImage_channels = new int[] { 1 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RISDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RISDummyCapturedImageSet capturedImageSet = new RISDummyCapturedImageSet(false);
        capturedImageSet.setFileSet(capturedImage);

        RegistrationImageSegmenter segmenter = new RegistrationImageSegmenter(fov, capImage_channels.length);
        IPolarizationImageSet pSet = segmenter.segment(capturedImageSet, capImage_channels[0]);

        _checkViews(pSet, fov, capImage_channels[0]);

    }

    @Test
    public void segment_OneSingleChannelXYZImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 3 };

        AxisOrder capImage_axisOrder = AxisOrder.XYZ;
        int[] capImage_channels = new int[] { 1 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RISDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RISDummyCapturedImageSet capturedImageSet = new RISDummyCapturedImageSet(false);
        capturedImageSet.setFileSet(capturedImage);

        RegistrationImageSegmenter segmenter = new RegistrationImageSegmenter(fov, capImage_channels.length);
        IPolarizationImageSet polarizationImageSet = segmenter.segment(capturedImageSet, capImage_channels[0]);

        _checkViews(polarizationImageSet, fov, capImage_channels[0]);

    }

    @Test
    public void segment_OneTwoChannelXYCImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 2 };

        AxisOrder capImage_axisOrder = AxisOrder.XYC;
        int[] capImage_channels = new int[] { 1, 2 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RISDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RISDummyCapturedImageSet capturedImageSet = new RISDummyCapturedImageSet(true);
        capturedImageSet.setFileSet(capturedImage);

        RegistrationImageSegmenter segmenter = new RegistrationImageSegmenter(fov, capImage_channels.length);
        IPolarizationImageSet polsetChannel1 = segmenter.segment(capturedImageSet, capImage_channels[0]);
        _checkViews(polsetChannel1, fov, capImage_channels[0]);

        IPolarizationImageSet polsetChannel2 = segmenter.segment(capturedImageSet, capImage_channels[1]);
        _checkViews(polsetChannel2, fov, capImage_channels[1]);
    }

    @Test
    public void segment_OneTwoChannelXYCZTImage_ReturnsCorrectPolImages() {
        long[] intersectionPoint = { 1, 1 };
        long[] capImage_dimension = new long[] { 4, 4, 2, 3, 5 };

        AxisOrder capImage_axisOrder = AxisOrder.XYCZT;
        int[] capImage_channels = new int[] { 1, 2 };

        FieldOfView fov = _createFoV(intersectionPoint, capImage_dimension);

        ICapturedImage capturedImage = new RISDummyCapturedImage(capImage_axisOrder, capImage_dimension,
                capImage_channels);
        RIPVCImageChecker._setImage(capturedImage.getImage(), capImage_channels, intersectionPoint);

        RISDummyCapturedImageSet capturedImageSet = new RISDummyCapturedImageSet(true);
        capturedImageSet.setFileSet(capturedImage);

        RegistrationImageSegmenter segmenter = new RegistrationImageSegmenter(fov, capImage_channels.length);
        IPolarizationImageSet polsetChannel1 = segmenter.segment(capturedImageSet, capImage_channels[0]);
        _checkViews(polsetChannel1, fov, capImage_channels[0]);

        IPolarizationImageSet polsetChannel2 = segmenter.segment(capturedImageSet, capImage_channels[1]);
        _checkViews(polsetChannel2, fov, capImage_channels[1]);
    }

    private void _checkViews(IPolarizationImageSet pSet, IFieldOfView fov, int channel) {
        // Dimension must equal maximum length of fovs in x and y dimension. Other
        // dimensions are 1.
        long[] dim = { fov.getMaximumLength()[0], fov.getMaximumLength()[1], 1, 1, 1 };
        assertArrayEquals(pSet.getPolarizationImage(Polarization.pol0).getImage().getMetadata().getDim(), dim);
        assertArrayEquals(pSet.getPolarizationImage(Polarization.pol45).getImage().getMetadata().getDim(), dim);
        assertArrayEquals(pSet.getPolarizationImage(Polarization.pol90).getImage().getMetadata().getDim(), dim);
        assertArrayEquals(pSet.getPolarizationImage(Polarization.pol135).getImage().getMetadata().getDim(), dim);

        assertTrue(RISImageChecker._checkImage(pSet.getPolarizationImage(Polarization.pol0).getImage(), 0, channel));
        assertTrue(RISImageChecker._checkImage(pSet.getPolarizationImage(Polarization.pol45).getImage(), 1, channel));
        assertTrue(RISImageChecker._checkImage(pSet.getPolarizationImage(Polarization.pol90).getImage(), 2, channel));
        assertTrue(RISImageChecker._checkImage(pSet.getPolarizationImage(Polarization.pol135).getImage(), 3, channel));
    }

    private FieldOfView _createFoV(long[] intersectionPoint, long[] capImage_dimension) {
        IBoxShape fov_pol0 = ShapeFactory.closedBox(new long[] { 0, 0 }, intersectionPoint, AxisOrder.XY);
        IBoxShape fov_pol45 = ShapeFactory.closedBox(new long[] { intersectionPoint[0] + 1, 0 },
                new long[] { capImage_dimension[0] - 1, intersectionPoint[1] }, AxisOrder.XY);
        IBoxShape fov_pol90 = ShapeFactory.closedBox(new long[] { 0, intersectionPoint[1] + 1 },
                new long[] { intersectionPoint[0], capImage_dimension[1] - 1 }, AxisOrder.XY);
        IBoxShape fov_pol135 = ShapeFactory.closedBox(
                new long[] { intersectionPoint[0] + 1, intersectionPoint[1] + 1 },
                new long[] { capImage_dimension[0] - 1, capImage_dimension[1] - 1 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);
        return fov;
    }

}

class RISImageChecker {
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
class RISDummyCapturedImageSet implements ICapturedImageSet {
    ICapturedImage capturedImages;
    boolean isMultiChannel;

    public RISDummyCapturedImageSet(boolean isMultiChannel) {
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

class RISDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public RISDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
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

class RISDummyFileSet implements ICapturedImageFileSet {

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