package fr.fresnel.fourPolar.io.imageSet.acquisition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSetType;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetNotFound;

/**
 * The set Name of every resource set is equal to the path of the first file.
 */
public class AcquisitionSetFromTextFileReaderTest {
    private String _resourceRoot = AcquisitionSetFromTextFileReaderTest.class.getResource("AcquisitionSetReaderTest")
            .getPath();

    @Test
    public void read_OneCameraSampleSetWithTwoCapturedImageSets_CreatesProperAcquisitionSet()
            throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        File root4PProject = new File(_resourceRoot, "AcquisitionSetReaderTest_OneCamera");

        AcquisitionSet acquisitionSet = new ReaderDummyAcquisitionSet(AcquisitionSetType.Sample, root4PProject);

        int numChannels = 3;
        IFourPolarImagingSetup setup = new DummyFPSetup(numChannels, Cameras.One);
        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup, new DummyChecker(),
                new ReaderDummyCapturedSetBuilder(numChannels, Cameras.One));
        reader.read(acquisitionSet);

        // This are the sets that are in the file.
        ICapturedImageFileSet set1 = _defineOneCameraFileSet();
        ICapturedImageFileSet set2 = _defineOneCameraFileSet();

        assertTrue(_isAcquisitionSetCorrectSize(acquisitionSet, 2));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set1));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set2));

    }

    @Test
    public void read_TwoCameraSampleSetWithTwoCapturedImageSets_CreatesProperAcquisitionSet()
            throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        File root4PProject = new File(_resourceRoot, "AcquisitionSetReaderTest_TwoCamera");

        AcquisitionSet acquisitionSet = new ReaderDummyAcquisitionSet(AcquisitionSetType.Sample, root4PProject);

        int numChannels = 3;
        IFourPolarImagingSetup setup = new DummyFPSetup(numChannels, Cameras.Two);
        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup, new DummyChecker(),
                new ReaderDummyCapturedSetBuilder(numChannels, Cameras.Two));
        reader.read(acquisitionSet);

        // This are the sets that are in the file.
        ICapturedImageFileSet set1 = _defineTwoCameraFileSet();
        ICapturedImageFileSet set2 = _defineTwoCameraFileSet();

        assertTrue(_isAcquisitionSetCorrectSize(acquisitionSet, 2));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set1));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set2));

    }

    @Test
    public void read_FourCameraSampleSetWithTwoCapturedImageSets_CreatesProperAcquisitionSet()
            throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        File root4PProject = new File(_resourceRoot, "AcquisitionSetReaderTest_FourCamera");

        AcquisitionSet acquisitionSet = new ReaderDummyAcquisitionSet(AcquisitionSetType.Sample, root4PProject);

        int numChannels = 3;
        IFourPolarImagingSetup setup = new DummyFPSetup(numChannels, Cameras.Four);
        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup, new DummyChecker(),
                new ReaderDummyCapturedSetBuilder(numChannels, Cameras.Four));
        reader.read(acquisitionSet);

        // This are the sets that are in the file.
        ICapturedImageFileSet set1 = _defineFourCameraFileSet();
        ICapturedImageFileSet set2 = _defineFourCameraFileSet();

        assertTrue(_isAcquisitionSetCorrectSize(acquisitionSet, 2));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set1));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set2));

    }

    private ICapturedImageFileSet _defineOneCameraFileSet() {
        int[] singleChannel = { 1 };
        File singleChannelFile = new File("singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("multiChannel.tif");

        ReaderDummyCapturedSetBuilder builder = new ReaderDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length, Cameras.One);
        builder.add(singleChannel, singleChannelFile);
        builder.add(multiChannel, multiChannelFile);
        return builder.build();
    }

    private ICapturedImageFileSet _defineTwoCameraFileSet() {
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("multiChannel_pol45_135.tif");

        ReaderDummyCapturedSetBuilder builder = new ReaderDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length, Cameras.Two);
        builder.add(singleChannel, singleChannelFile_pol0_90, singleChannelFile_pol45_135);
        builder.add(multiChannel, multiChannelFile_pol0_90, multiChannelFile_pol45_135);

        return builder.build();
    }

    private ICapturedImageFileSet _defineFourCameraFileSet() {
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0 = new File("singleChannel_pol0.tif");
        File singleChannelFile_pol45 = new File("singleChannel_pol45.tif");
        File singleChannelFile_pol90 = new File("singleChannel_pol90.tif");
        File singleChannelFile_pol135 = new File("singleChannel_pol135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0 = new File("multiChannel_pol0.tif");
        File multiChannelFile_pol45 = new File("multiChannel_pol45.tif");
        File multiChannelFile_pol90 = new File("multiChannel_pol90.tif");
        File multiChannelFile_pol135 = new File("multiChannel_pol135.tif");

        ReaderDummyCapturedSetBuilder builder = new ReaderDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length, Cameras.Four);
        builder.add(singleChannel, singleChannelFile_pol0, singleChannelFile_pol45, singleChannelFile_pol90,
                singleChannelFile_pol135);
        builder.add(multiChannel, multiChannelFile_pol0, multiChannelFile_pol45, multiChannelFile_pol90,
                multiChannelFile_pol135);

        return builder.build();
    }

    private boolean _isAcquisitionSetCorrectSize(AcquisitionSet acquisitionSet, int size) {
        return acquisitionSet.setSize() == size;
    }

    private boolean _doesCapturedImageSetExist(AcquisitionSet acquisitionSet, ICapturedImageFileSet fileSet) {
        try {
            ICapturedImageFileSet capturedSetInAcquisitionSet = acquisitionSet
                    .getCapturedImageSet(fileSet.getSetName());

            boolean isChannelsEqual = Arrays.equals(capturedSetInAcquisitionSet.getChannels(), fileSet.getChannels());

            boolean isFilesEqual = true;
            String[] labels = Cameras.getLabels(capturedSetInAcquisitionSet.getnCameras());
            for (String label : labels) {
                isFilesEqual &= Arrays.stream(capturedSetInAcquisitionSet.getFile(label)).allMatch(t -> {
                    return Arrays.stream(fileSet.getFile(label)).anyMatch(p -> t.file().equals(p.file()));
                });

            }

            return isChannelsEqual && isFilesEqual;
        } catch (KeyAlreadyExistsException | KeyException e) {
            return false;
        }

    }

}

class ReaderDummyAcquisitionSet implements AcquisitionSet {
    Hashtable<String, ICapturedImageFileSet> _sets = new Hashtable<>();
    AcquisitionSetType type;
    File root;

    public ReaderDummyAcquisitionSet(AcquisitionSetType type, File root) {
        this.type = type;
        this.root = root;
    }

    @Override
    public void addCapturedImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        _sets.put(fileSet.getSetName(), fileSet);
    }

    @Override
    public ICapturedImageFileSet getCapturedImageSet(String setName) throws KeyException {
        return _sets.get(setName);
    }

    @Override
    public Iterator<ICapturedImageFileSet> getIterator() {
        return _sets.values().iterator();
    }

    @Override
    public void removeCapturedImageSet(String setName) throws KeyException {
    }

    @Override
    public int setSize() {
        return _sets.size();
    }

    @Override
    public AcquisitionSetType setType() {
        return this.type;
    }

    @Override
    public File rootFolder() {
        return this.root;
    }

}

class ReaderDummyCapturedSetBuilder implements ICapturedImageFileSetBuilder {
    int numChannels;
    int counter = 0;
    ReaderDummyCapturedImageFileSet fileSet;
    Cameras camera;

    ReaderDummyCapturedSetBuilder(int numChannels, Cameras camera) {
        this.numChannels = numChannels;
        fileSet = new ReaderDummyCapturedImageFileSet(numChannels);
        this.camera = camera;
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_45_90_135) {
        fileSet.setCameras(Cameras.One);
        fileSet.setFileSet(Cameras.getLabels(Cameras.One)[0],
                new ReaderDummyCapturedImageFile(channels, pol0_45_90_135));
        return this;
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_90, File pol45_135) {
        fileSet.setCameras(Cameras.Two);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[0], new ReaderDummyCapturedImageFile(channels, pol0_90));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[1], new ReaderDummyCapturedImageFile(channels, pol45_135));
        return null;
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0, File pol45, File pol90, File pol135) {
        fileSet.setCameras(Cameras.Four);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[0], new ReaderDummyCapturedImageFile(channels, pol0));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[1], new ReaderDummyCapturedImageFile(channels, pol45));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[2], new ReaderDummyCapturedImageFile(channels, pol90));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[3], new ReaderDummyCapturedImageFile(channels, pol135));

        return this;
    }

    @Override
    public ICapturedImageFileSet build() {
        setSetName();
        ReaderDummyCapturedImageFileSet fileSetTemp = fileSet;
        fileSet = new ReaderDummyCapturedImageFileSet(numChannels);

        return fileSetTemp;

    }

    private void setSetName() {
        String fileName = fileSet.getFile(Cameras.getLabels(camera)[0])[0].file().getName();
        fileSet.setSetName(fileName.substring(0, fileName.indexOf('.')));
    }

}

class ReaderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ArrayList<ICapturedImageFile>> files = new Hashtable<>();
    private Cameras _cameras;
    private String setName;
    private int numChannels;

    ReaderDummyCapturedImageFileSet(int numChannels) {

        this.numChannels = numChannels;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setFileSet(String label, ICapturedImageFile file) {
        if (this.files.get(label) == null) {
            ArrayList<ICapturedImageFile> cFiles = new ArrayList<>();
            files.put(label, cFiles);
        }

        this.files.get(label).add(file);
    }

    public void setCameras(Cameras cameras) {
        this._cameras = cameras;
    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return this.files.get(label).toArray(new ICapturedImageFile[0]);
    }

    @Override
    public String getSetName() {
        return setName;
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
        ArrayList<ICapturedImageFile> all = new ArrayList<>();
        for (String label : Cameras.getLabels(this._cameras)) {
            for (Iterator<ICapturedImageFile> iterator = this.files.get(label).iterator(); iterator.hasNext();) {
                all.add(iterator.next());
            }

        }

        return all.iterator();
    }

    @Override
    public int[] getChannels() {
        return IntStream.range(1, numChannels + 1).toArray();
    }

}

class ReaderDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public ReaderDummyCapturedImageFile(int[] channels, File file) {
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

class ReaderDummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private int numChannels;

    public ReaderDummyFPSetup(int numChannels, Cameras camera) {
        this.numChannels = numChannels;
        this.cameras = camera;
    }

    @Override
    public Cameras getCameras() {
        return cameras;
    }

    @Override
    public void setCameras(Cameras cameras) throws IllegalArgumentException {
        this.cameras = cameras;
    }

    @Override
    public IChannel getChannel(int channel) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void setChannel(int channel, IChannel propagationChannel) throws IllegalArgumentException {
    }

    @Override
    public int getNumChannel() {
        return this.numChannels;
    }

    @Override
    public INumericalAperture getNumericalAperture() {
        return null;
    }

    @Override
    public void setNumericalAperture(INumericalAperture na) {

    }

    @Override
    public IFieldOfView getFieldOfView() {
        return null;
    }

    @Override
    public void setFieldOfView(IFieldOfView fov) throws IllegalArgumentException {

    }

}

class DummyChecker implements ICapturedImageChecker {

    @Override
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage {

    }

}