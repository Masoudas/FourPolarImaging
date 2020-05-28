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
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * CapturedImageSetTest
 */
public class CapturedImageSetTest {
    private String root = "/";

    @Test
    public void hasMultiChannelImage_FourCamASetWithOneMultiChannelFile_ReturnsTrue() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1, 2 };
        int[] channel2 = { 3 };

        ICapturedImageFile[] pol0 = { new DummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        ICapturedImageFile[] pol45 = { new DummyCapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        ICapturedImageFile[] pol90 = { new DummyCapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        ICapturedImageFile[] pol135 = { new DummyCapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol1351.tiff")) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(capturedImageSet.hasMultiChannelImage());
    }

    @Test
    public void hasMultiChannelImage_FourCamASetWithNoMultiChannelFile_ReturnsFalse() {
        Cameras camera = Cameras.Four;
        int[] channel1 = { 1 };
        int[] channel2 = { 2 };

        ICapturedImageFile[] pol0 = { new DummyCapturedImageFile(channel1, new File(root, "pol0.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol01.tiff")) };
        ICapturedImageFile[] pol45 = { new DummyCapturedImageFile(channel1, new File(root, "pol45.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol451.tiff")) };
        ICapturedImageFile[] pol90 = { new DummyCapturedImageFile(channel1, new File(root, "pol90.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol901.tiff")) };
        ICapturedImageFile[] pol135 = { new DummyCapturedImageFile(channel1, new File(root, "pol135.tiff")),
                new DummyCapturedImageFile(channel2, new File(root, "pol1351.tiff")) };

        DummyCapturedImageFileSet fileSet = new DummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);
        fileSet.setCameras(camera);

        DummyBuilder builder = new DummyBuilder();
        builder.setFileSet(fileSet);
        CapturedImageSet capturedImageSet = new CapturedImageSet(builder);
        assertTrue(!capturedImageSet.hasMultiChannelImage());
    }

}

class DummyBuilder extends ICapturedImageSetBuilder {
    private Hashtable<String, ICapturedImage[]> files = new Hashtable<>();
    private ICapturedImageFileSet _fileSet;

    public void setFileSet(ICapturedImageFileSet fileSet) {
        this._fileSet = fileSet;
    }

    public void setFiles(String label, ICapturedImage[] file) {
        this.files.put(label, file);
    }

    @Override
    ICapturedImageFileSet getFileSet() {
        return this._fileSet;
    }

    @Override
    ICapturedImage[] getCapturedImages(String label) {
        return new ICapturedImage[0];
    }

}

class DummyCapturedImageFileSet implements ICapturedImageFileSet {
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
        return false;
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

}

class DummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public DummyCapturedImageFile(int[] channels, File file) {
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