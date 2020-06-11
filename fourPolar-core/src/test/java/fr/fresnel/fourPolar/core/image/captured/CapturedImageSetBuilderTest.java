package fr.fresnel.fourPolar.core.image.captured;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class CapturedImageSetBuilderTest {
    String root = "/";

    @Test
    public void build_OneCameraCaseWithTwoSingleChannelImage_CreatsSetWithTwoImages() {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        ICapturedImageFile[] pol0_45_90_135 = {
                new BuilderDummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        Image<UINT16> pol0_45_90_135_0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol0_45_90_135_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        BuilderDummyCapturedImageFileSet fileSet = new BuilderDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_45_90_135);
        fileSet.setCameras(camera);

        CapturedImageSetBuilder builder = new CapturedImageSetBuilder(camera);
        builder.setFileSet(fileSet);

        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135[0], pol0_45_90_135_0);
        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135[1], pol0_45_90_135_1);

        ICapturedImageSet set = builder.build();

        assertTrue(!set.hasMultiChannelImage());
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol0_45_90_135_0
                || set.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol0_45_90_135_1);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol0_45_90_135_0
                || set.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol0_45_90_135_1);

    }

    @Test
    public void build_RepetitiveBuild_CreatsSetWithTwoImagesEachTime() {
        Cameras camera = Cameras.One;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        // Build first time
        ICapturedImageFile[] pol0_45_90_135 = {
                new BuilderDummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        Image<UINT16> pol0_45_90_135_0 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol0_45_90_135_1 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        BuilderDummyCapturedImageFileSet fileSet = new BuilderDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_45_90_135);
        fileSet.setCameras(camera);

        CapturedImageSetBuilder builder = new CapturedImageSetBuilder(camera);
        builder.setFileSet(fileSet);

        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135[0], pol0_45_90_135_0);
        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135[1], pol0_45_90_135_1);

        // Build second time
        ICapturedImageFile[] pol0_45_90_135_s = {
                new BuilderDummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        Image<UINT16> pol0_45_90_135_0_s = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol0_45_90_135_1_s = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        BuilderDummyCapturedImageFileSet fileSet_s = new BuilderDummyCapturedImageFileSet();
        fileSet_s.setFileSet(Cameras.getLabels(camera)[0], pol0_45_90_135_s);
        fileSet_s.setCameras(camera);

        builder.setFileSet(fileSet_s);
        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135_s[0], pol0_45_90_135_0_s);
        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0_45_90_135_s[1], pol0_45_90_135_1_s);

        ICapturedImageSet set_s = builder.build();

        assertTrue(!set_s.hasMultiChannelImage());
        assertTrue(set_s.getCapturedImage(Cameras.getLabels(camera)[0]).length == 2);
        assertTrue(set_s.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol0_45_90_135_0_s
                || set_s.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol0_45_90_135_1_s);
        assertTrue(set_s.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol0_45_90_135_0_s
                || set_s.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol0_45_90_135_1_s);

    }

    @Test
    public void build_FourCameraCaseWithEightSingleChannelImage_CreatsSetWithEightImages() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        long[] dim1 = { 1, 1, 1, 1, 1 };

        IMetadata metadata1 = new Metadata.MetadataBuilder(dim1).axisOrder(AxisOrder.XYCZT).build();

        ICapturedImageFile[] pol0 = { new BuilderDummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        Image<UINT16> pol00 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol01 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        ICapturedImageFile[] pol45 = { new BuilderDummyCapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        Image<UINT16> pol450 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol451 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        ICapturedImageFile[] pol90 = { new BuilderDummyCapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        Image<UINT16> pol900 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol901 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        ICapturedImageFile[] pol135 = { new BuilderDummyCapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new BuilderDummyCapturedImageFile(channel2, new File(root, "pol1351.tiff")) };
        Image<UINT16> pol1350 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());
        Image<UINT16> pol1351 = new ImgLib2ImageFactory().create(metadata1, UINT16.zero());

        BuilderDummyCapturedImageFileSet fileSet = new BuilderDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        CapturedImageSetBuilder builder = new CapturedImageSetBuilder(camera);
        builder.setFileSet(fileSet);

        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0[0], pol00);
        builder.setCapturedImage(Cameras.getLabels(camera)[0], pol0[1], pol01);
        builder.setCapturedImage(Cameras.getLabels(camera)[1], pol45[0], pol450);
        builder.setCapturedImage(Cameras.getLabels(camera)[1], pol45[1], pol451);
        builder.setCapturedImage(Cameras.getLabels(camera)[2], pol90[0], pol900);
        builder.setCapturedImage(Cameras.getLabels(camera)[2], pol90[1], pol901);
        builder.setCapturedImage(Cameras.getLabels(camera)[3], pol135[0], pol1350);
        builder.setCapturedImage(Cameras.getLabels(camera)[3], pol135[1], pol1351);

        ICapturedImageSet set = builder.build();

        assertTrue(!set.hasMultiChannelImage());
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol00
                || set.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol01);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[0])[0].getImage() == pol00
                || set.getCapturedImage(Cameras.getLabels(camera)[0])[1].getImage() == pol01);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[1])[0].getImage() == pol450
                || set.getCapturedImage(Cameras.getLabels(camera)[1])[1].getImage() == pol451);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[1])[0].getImage() == pol450
                || set.getCapturedImage(Cameras.getLabels(camera)[1])[1].getImage() == pol451);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[2])[0].getImage() == pol900
                || set.getCapturedImage(Cameras.getLabels(camera)[2])[1].getImage() == pol901);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[2])[1].getImage() == pol901
                || set.getCapturedImage(Cameras.getLabels(camera)[2])[0].getImage() == pol900);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[3])[0].getImage() == pol1350
                || set.getCapturedImage(Cameras.getLabels(camera)[3])[1].getImage() == pol1351);
        assertTrue(set.getCapturedImage(Cameras.getLabels(camera)[3])[1].getImage() == pol1351
                || set.getCapturedImage(Cameras.getLabels(camera)[3])[0].getImage() == pol1350);

    }
}

class BuilderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;

    public void setFileSet(String label, ICapturedImageFile[] file) {
        this.files.put(label, file);
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return this.files.get(label);
    }

    @Override
    public String getSetName() {
        return null;
    }

    @Override
    public Cameras getnCameras() {
        return this._cameras;
    }

    @Override
    public boolean hasLabel(String label) {
        return this.files.containsKey(label);
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return false;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.files.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();
    }

    @Override
    public int[] getChannels() {
        // TODO Auto-generated method stub
        return null;
    }

}

class BuilderDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public BuilderDummyCapturedImageFile(int[] channels, File file) {
        _channels = channels;
        _file = file;
    }

    @Override
    public int[] channels() {
        return this._channels;
    }

    @Override
    public File file() {
        return this._file;
    }

}