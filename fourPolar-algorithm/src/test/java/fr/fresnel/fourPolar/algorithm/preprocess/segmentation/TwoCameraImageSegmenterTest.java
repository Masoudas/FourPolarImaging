package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class TwoCameraImageSegmenterTest {
    @Test
    public void segment_OneSingleChannelXYImage_ReturnsCorrectPolImages() {
        IBoxShape fov_pol0 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 3, 5 }, AxisOrder.XY);
        IBoxShape fov_pol45 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 5, 5 }, AxisOrder.XY);
        IBoxShape fov_pol90 = new ShapeFactory().closedBox(new long[] { 1, 1 }, new long[] { 3, 5 }, AxisOrder.XY);
        IBoxShape fov_pol135 = new ShapeFactory().closedBox(new long[] { 3, 1 }, new long[] { 5, 5 }, AxisOrder.XY);

        FieldOfView fov = new FieldOfView(fov_pol0, fov_pol45, fov_pol90, fov_pol135);

        int numChannels = 1;
        ICapturedImage capturedImage_pol0_90 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });
        ICapturedImage capturedImage_pol45_135 = new TCISDummyCapturedImage(AxisOrder.XY, new long[] { 4, 4 },
                new int[] { 1 });

        TCISDummyCapturedImageSet capturedImageSet = new TCISDummyCapturedImageSet(false, 1);
        capturedImageSet.setFileSet(capturedImage_pol0_90, capturedImage_pol45_135);

        TwoCameraImageSegmenter segmenter = new TwoCameraImageSegmenter(fov, numChannels);
        segmenter.setCapturedImageSet(capturedImageSet);
        IPolarizationImageSet imageSet = segmenter.segment(1);

        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol0).getImage(), 0));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol45).getImage(), 1));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol90).getImage(), 2));
        assertTrue(TCISImageChecker._checkImage(imageSet.getPolarizationImage(Polarization.pol135).getImage(), 3));
    }

}

class TCISImageChecker {
    /**
     * For each plane, sets (0,0)-> 0 + channelNo, (1,0)-> 1 + channelNo, (0,1)-> 2
     * + channelNo, (1,1)-> 3 + channelNo. If no channel, add zero.
     */
    public static void _setImage(Image<UINT16> image) {
        int c_axis = image.getMetadata().axisOrder().c_axis;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            cursor.next();
            long[] position = cursor.localize();
            int offset = c_axis > 0 ? (int) position[c_axis] : 0;
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
    public static boolean _checkImage(Image<UINT16> image, int value) {
        if (!image.getCursor().hasNext()) {
            return false;
        }

        int c_axis = image.getMetadata().axisOrder().c_axis;
        boolean equals = true;
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext() && equals;) {
            long[] position = cursor.localize();
            int offset = c_axis > 0 ? (int) position[c_axis] : 0;

            IPixel<UINT16> pixel = cursor.next();
            equals &= pixel.value().get() == value + offset;
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

    public void setFileSet(ICapturedImage fileSet_pol0_90, ICapturedImage fileSet_pol45_135) {
        fileSets_pol0_90[counter] = fileSet_pol0_90;
        fileSets_pol45_135[counter++] = fileSet_pol45_135;
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

class TCISDummyFileSet implements ICapturedImageFileSet {

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

}