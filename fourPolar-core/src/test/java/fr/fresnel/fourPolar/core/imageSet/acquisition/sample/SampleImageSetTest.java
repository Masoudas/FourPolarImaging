package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.security.KeyException;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

public class SampleImageSetTest {
    File root = new File("/root");

    File pol0 = new File(root, "pol0.tiff");
    File pol45 = new File(root, "pol45.tiff");
    File pol90 = new File(root, "pol90.tiff");
    File pol135 = new File(root, "pol135.tiff");

    @Test
    public void addImage_DuplicateImage_ShouldThrowException()
            throws IllegalArgumentException, IncompatibleCapturedImage, KeyException {
        SampleImageSet sampleSet = new SampleImageSet(new File("/"));
        sampleSet.addCapturedImageSet(new DummyCapturedSet(pol0, new int[] { 1, 2 }));

        assertThrows(KeyAlreadyExistsException.class, () -> {
            sampleSet.addCapturedImageSet(new DummyCapturedSet(pol0, new int[] { 1, 2 }));
        });
    }

    @Test
    public void removeImage_fileSet_ReturnsZeroLengthForChannelOne() throws IllegalArgumentException, KeyException {
        SampleImageSet sampleSet = new SampleImageSet(new File("/"));
        sampleSet.addCapturedImageSet(new DummyCapturedSet(pol0, new int[] { 1, 2 }));

        sampleSet.removeCapturedImageSet(new DummyCapturedSet(pol0, new int[] { 1, 2 }).getSetName());
        assertTrue(sampleSet.setSize() == 0);
    }

    @Test
    public void removeImage_nonExistentfileSet_ShouldThrowException() throws KeyException {
        SampleImageSet sampleSet = new SampleImageSet(new File("/"));
        sampleSet.addCapturedImageSet(new DummyCapturedSet(pol0, new int[] { 1, 2 }));

        assertThrows(KeyException.class, () -> {
            sampleSet.removeCapturedImageSet("wrongSetName");
        });
    }

    @Test
    public void getIterator_TwoFileSets_ReturnsIteratorProperSize() {
        SampleImageSet sampleSet = new SampleImageSet(new File("/"));

        ICapturedImageFileSet set1 = new DummyCapturedSet(pol0, new int[] { 1, 2 });
        ICapturedImageFileSet set2 = new DummyCapturedSet(pol45, new int[] { 1, 2 });

        sampleSet.addCapturedImageSet(set1);
        sampleSet.addCapturedImageSet(set2);

        Iterator<ICapturedImageFileSet> itr = sampleSet.getIterator();

        assertTrue(itr.next().getSetName().equals(set1.getSetName()));
        assertTrue(itr.next().getSetName().equals(set2.getSetName()));
    }

}

/**
 * A dummy checker, always checks to valid.
 */
class DummyChecker implements ICapturedImageChecker {

    @Override
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage {

    }

}

class DummyCapturedImageFile implements ICapturedImageFile {
    File file;
    int[] channels;

    public DummyCapturedImageFile(File file, int[] channels) {
        this.file = file;
        this.channels = channels;
    }

    @Override
    public int[] channels() {
        return channels;
    }

    @Override
    public File file() {
        return file;
    }

}

class DummyCapturedSet implements ICapturedImageFileSet {
    private DummyCapturedImageFile[] file = new DummyCapturedImageFile[1];
    private String setName;

    public DummyCapturedSet(File file, int[] channel) {
        this.file = new DummyCapturedImageFile[] { new DummyCapturedImageFile(file, channel) };
        this.setName = file.getName();

    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return file;
    }

    @Override
    public String getSetName() {
        return setName;
    }

    @Override
    public Cameras getnCameras() {
        return null;
    }

    @Override
    public boolean hasLabel(String label) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return setName.equals((String) obj);
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        return file[0].file().getAbsolutePath().equals(fileset.getFile("")[0].file().getAbsolutePath());
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
