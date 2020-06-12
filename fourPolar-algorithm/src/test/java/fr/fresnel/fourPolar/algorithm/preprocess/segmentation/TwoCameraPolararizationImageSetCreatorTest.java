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
public class TwoCameraPolararizationImageSetCreatorTest {
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
        ICapturedImage capturedImage_pol0_90 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });
        ICapturedImage capturedImage_pol45_135 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });

        TCISDummyCapturedImageSet capturedImageSet = new TCISDummyCapturedImageSet(false, 1);
        capturedImageSet.setFileSet(capturedImage_pol0_90, capturedImage_pol45_135);

        TwoCameraPolararizationImageSetCreator creator = new TwoCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(this._singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);
        creator.setCapturedImageSet(capturedImageSet);
        IPolarizationImageSet imageSet = creator.create(1);

        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, 1));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, 1));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 0, 1));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 1, 1));
    }

    @Test
    public void segment_TwoSingleChannelXYImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 2;
        ICapturedImage capturedImage_pol0_90_c1 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });
        ICapturedImage capturedImage_pol45_135_c1 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });

        ICapturedImage capturedImage_pol0_90_c2 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 2 });
        ICapturedImage capturedImage_pol45_135_c2 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 2 });

        TCISDummyCapturedImageSet capturedImageSet = new TCISDummyCapturedImageSet(false, 2);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c1, capturedImage_pol45_135_c1);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c2, capturedImage_pol45_135_c2);

        TwoCameraPolararizationImageSetCreator creator = new TwoCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(this._singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 1, c));

        }
    }

    @Test
    public void segment_OneSingleChannelXYCZTImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 2;
        ICapturedImage capturedImage_pol0_90_c1 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 1, 1, 3 }, new int[] { 1 });
        ICapturedImage capturedImage_pol45_135_c1 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 1, 1, 3 }, new int[] { 1 });

        ICapturedImage capturedImage_pol0_90_c2 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 1, 1, 3 }, new int[] { 2 });
        ICapturedImage capturedImage_pol45_135_c2 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 1, 1, 3 }, new int[] { 2 });

        TCISDummyCapturedImageSet capturedImageSet = new TCISDummyCapturedImageSet(false, 2);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c1, capturedImage_pol45_135_c1);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c2, capturedImage_pol45_135_c2);

        TwoCameraPolararizationImageSetCreator creator = new TwoCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(this._singleChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 1, c));

        }
    }

    @Test
    public void segment_TwoMultiChannelXYCZTImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 2, 2 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 4, 2 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 3 }, new long[] { 2, 4 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 3 }, new long[] { 4, 4 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 4;
        ICapturedImage capturedImage_pol0_90_c1 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 2, 1, 3 }, new int[] { 1, 2 });
        ICapturedImage capturedImage_pol45_135_c1 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 2, 1, 3 }, new int[] { 1, 2 });

        ICapturedImage capturedImage_pol0_90_c2 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 2, 1, 3 }, new int[] { 3, 4 });
        ICapturedImage capturedImage_pol45_135_c2 = new TCISDummyCapturedImage(AxisOrder.XYCZT,
                new long[] { 4, 4, 2, 1, 3 }, new int[] { 3, 4 });

        TCISDummyCapturedImageSet capturedImageSet = new TCISDummyCapturedImageSet(true, 2);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c1, capturedImage_pol45_135_c1);
        capturedImageSet.setFileSet(capturedImage_pol0_90_c2, capturedImage_pol45_135_c2);

        TwoCameraPolararizationImageSetCreator creator = new TwoCameraPolararizationImageSetCreator(fov, numChannels);
        creator.setSegmenter(this._multiChannelSegmenter);
        creator.setCapturedImageSet(capturedImageSet);

        for (int c = 1; c <= numChannels; c++) {
            IPolarizationImageSet imageSet = creator.create(c);

            assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 0, c));
            assertTrue(
                    TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 1, c));

        }
    }

}

class TCISImageChecker {
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
            if (position[0] < 2) {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(0 + offset)));
            } else {
                cursor.setPixel(new Pixel<UINT16>(new UINT16(1 + offset)));
            }
        }
    }

    /**
     * Checks all values equal value.
     */
    public static boolean _checkImage(Image<UINT16> image, int value, int channels) {
        if (!image.getCursor().hasNext()) {
            return false;
        }

        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {

            IPixel<UINT16> pixel = cursor.next();
            equals &= pixel.value().get() == value + channels;
        }
        return equals;
    }
}

class TCISDummyCapturedImageSet implements ICapturedImageSet {
    ICapturedImage[] fileSets_pol0_90;
    ICapturedImage[] fileSets_pol45_135;
    boolean isMultiChannel;
    int counter = 0;

    String[] labels = Cameras.getLabels(Cameras.Two);

    public TCISDummyCapturedImageSet(boolean isMultiChannel, int nFileSets) {
        this.isMultiChannel = isMultiChannel;
        fileSets_pol0_90 = new ICapturedImage[nFileSets];
        fileSets_pol45_135 = new ICapturedImage[nFileSets];

    }

    public void setFileSet(ICapturedImage pol0_90, ICapturedImage pol45_135) {
        fileSets_pol0_90[counter] = pol0_90;
        fileSets_pol45_135[counter++] = pol45_135;
    }

    @Override
    public ICapturedImage[] getCapturedImage(String label) {
        if (label.equals(this.labels[0])) {
            return fileSets_pol0_90;
        } else {
            return fileSets_pol45_135;
        }
    }

    @Override
    public ICapturedImageFileSet fileSet() {
        return new TCISDummyFileSet();
    }

    @Override
    public boolean hasMultiChannelImage() {
        return isMultiChannel;
    }

}

class TCISDummyCapturedImage implements ICapturedImage {
    Image<UINT16> image;
    IMetadata metadata;
    int[] channels;

    public TCISDummyCapturedImage(AxisOrder axisOrder, long[] dim, int[] channels) {
        metadata = new Metadata.MetadataBuilder(dim).axisOrder(axisOrder).build();
        image = new ImgLib2ImageFactory().create(metadata, UINT16.zero());
        TCISImageChecker._setImage(image, channels);
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

class TCISDummyFileSet implements ICapturedImageFileSet {

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