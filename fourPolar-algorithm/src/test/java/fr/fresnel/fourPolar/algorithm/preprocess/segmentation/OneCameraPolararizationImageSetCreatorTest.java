package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

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
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Tests are done only for sample images.
 */
public class OneCameraPolararizationImageSetCreatorTest {
    ChannelPolarizationSegmenter _singleChannelSegmenter = new SampleSingleChannelPolarizationSegmenter();
    ChannelPolarizationSegmenter _multiChannelSegmenter = new SampleMultiChannelPolarizationSegmenter();

    @Test
    public void segment_OneSingleChannelXYImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 1;
        ICapturedImage capturedImage = new OCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 }, new int[] { 1 });

        OCISDummyCapturedImageSet capturedImageSet = new OCISDummyCapturedImageSet(false, 1);
        capturedImageSet.setFileSet(capturedImage);

        OneCameraPolararizationImageSetCreator creator = new OneCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(_singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);
        IPolarizationImageSet imageSet = creator.create(1);

        assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, 1));
        assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, 1));
        assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 2, 1));
        assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 3, 1));
    }

    @Test
    public void segment_TwoSingleChannelXYImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 2;
        ICapturedImage capturedImage_c1 = new OCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });
        ICapturedImage capturedImage_c2 = new OCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 2 });

        OCISDummyCapturedImageSet capturedImageSet = new OCISDummyCapturedImageSet(false, numChannels);
        capturedImageSet.setFileSet(capturedImage_c1);
        capturedImageSet.setFileSet(capturedImage_c2);

        OneCameraPolararizationImageSetCreator creator = new OneCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(_singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 2, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 3, c));

        }
    }

    @Test
    public void segment_TwoSingleChannelXYCZTImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 2;
        ICapturedImage capturedImage_c1 = new OCISDummyCapturedImage(AxisOrder.XYCZT, new long[] { 4, 4, 1, 2, 3 },
                new int[] { 1 });
        ICapturedImage capturedImage_c2 = new OCISDummyCapturedImage(AxisOrder.XYCZT, new long[] { 4, 4, 1, 2, 3 },
                new int[] { 2 });

        OCISDummyCapturedImageSet capturedImageSet = new OCISDummyCapturedImageSet(false, numChannels);
        capturedImageSet.setFileSet(capturedImage_c1);
        capturedImageSet.setFileSet(capturedImage_c2);

        OneCameraPolararizationImageSetCreator creator = new OneCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(_singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 2, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 3, c));

        }
    }

    @Test
    public void segment_TwoMutliChannelXYCImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 4;
        ICapturedImage capturedImage_c1 = new OCISDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, 2 },
                new int[] { 1, 2 });
        ICapturedImage capturedImage_c2 = new OCISDummyCapturedImage(AxisOrder.XYC, new long[] { 4, 4, 2 },
                new int[] { 3, 4 });

        OCISDummyCapturedImageSet capturedImageSet = new OCISDummyCapturedImageSet(true, 2);
        capturedImageSet.setFileSet(capturedImage_c1);
        capturedImageSet.setFileSet(capturedImage_c2);

        OneCameraPolararizationImageSetCreator creator = new OneCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(_multiChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 2, c));
            assertTrue(
                    OCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 3, c));

        }
    }

}

class OCISImageChecker {
    /**
     * For each plane, sets (0,0)-> 0 + channelNo, (1,0)-> 1 + channelNo, (0,1)-> 2
     * + channelNo, (1,1)-> 3 + channelNo. If no channel, add zero.
     */
    public static void _setImage(Image<UINT16> image, int[] channels) {
        int c_axis = image.getMetadata().axisOrder().c_axis;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            int offset = c_axis > 0 ? channels[(int) position[c_axis]] : channels[0];
            if (position[0] < 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(0 + offset)));
            } else if (position[0] >= 2 && position[1] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(1 + offset)));
            } else if (position[0] < 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(2 + offset)));
            } else if (position[0] >= 2 && position[1] >= 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(3 + offset)));
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

class OCISDummyCapturedImageSet implements ICapturedImageSet {
    ICapturedImage[] fileSets;
    boolean isMultiChannel;
    int counter = 0;

    public OCISDummyCapturedImageSet(boolean isMultiChannel, int nFileSets) {
        this.isMultiChannel = isMultiChannel;
        fileSets = new ICapturedImage[nFileSets];

    }

    public void setFileSet(ICapturedImage fileSet) {
        fileSets[counter++] = fileSet;
    }

    @Override
    public ICapturedImage[] getCapturedImage(String label) {
        return fileSets;
    }

    @Override
    public ICapturedImageFileSet fileSet() {
        return new OCISDummyFileSet();
    }

    @Override
    public boolean hasMultiChannelImage() {
        return isMultiChannel;
    }

}

class OCISDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public OCISDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
        metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();
        image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        OCISImageChecker._setImage(image, channels);
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

class OCISDummyFileSet implements ICapturedImageFileSet {

    @Override
    public ICapturedImageFile[] getFile(String label) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSetName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cameras getnCameras() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getChannels() {
        // TODO Auto-generated method stub
        return null;
    }

}