package fr.fresnel.fourPolar.io.imageSet.acquisition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSetType;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetNotFound;

public class AcquisitionSetFromTextFileReaderTest {
    private String _resourceRoot = AcquisitionSetFromTextFileReaderTest.class.getResource("").getPath();

    @Test
    public void read_OneCameraSampleSetWithTwoCapturedImageSets_CreatesProperAcquisitionSet()
            throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        IFourPolarImagingSetup setup = new DummyFPSetup(3, Cameras.One);
        File root4PProject = new File(_resourceRoot, "AcquisitionSetReaderTest_OneCamera");

        AcquisitionSet acquisitionSet = new ReaderDummyAcquisitionSet(AcquisitionSetType.Sample, root4PProject);

        AcquisitionSetFromTextFileReader reader = new AcquisitionSetFromTextFileReader(setup, new DummyChecker());
        reader.read(acquisitionSet);

        // This are the sets that are in the file.
        String setOneName = "Set1";
        ICapturedImageFileSet set1 = _defineOneCameraFileSet(setOneName);

        String setTwoName = "Set2";
        ICapturedImageFileSet set2 = _defineOneCameraFileSet(setTwoName);

        assertTrue(_isAcquisitionSetCorrectSize(acquisitionSet, 2));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set1));
        assertTrue(_doesCapturedImageSetExist(acquisitionSet, set2));

    }

    private ICapturedImageFileSet _defineOneCameraFileSet(String setName) {
        int[] singleChannel = { 1 };
        File singleChannelFile = new File("/", "singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("/", "multiChannel.tif");

        ICapturedImageFile[] pol0_45_90_135 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile) };

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setName);
        fileSet.setFileSet(Cameras.getLabels(Cameras.One)[0], pol0_45_90_135);

        return fileSet;
    }

    private ICapturedImageFileSet _defineTwoCameraFileSet(String setName) {
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("/", "singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("/", "singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("/", "multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("/", "multiChannel_pol45_135.tif");

        ICapturedImageFile[] pol0_90 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol0_90),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol0_90) };
        ICapturedImageFile[] pol45_135 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol45_135),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol45_135) };

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setName);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[0], pol0_90);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[1], pol45_135);

        return fileSet;
    }

    private ICapturedImageFileSet _defineFourCameraFileSet(String setName) {
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0 = new File("/", "singleChannel_pol0.tif");
        File singleChannelFile_pol45 = new File("/", "singleChannel_pol45.tif");
        File singleChannelFile_pol90 = new File("/", "singleChannel_pol90.tif");
        File singleChannelFile_pol135 = new File("/", "singleChannel_pol135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0 = new File("/", "multiChannel_pol0.tif");
        File multiChannelFile_pol45 = new File("/", "multiChannel_pol45.tif");
        File multiChannelFile_pol90 = new File("/", "multiChannel_pol90.tif");
        File multiChannelFile_pol135 = new File("/", "multiChannel_pol135.tif");

        ICapturedImageFile[] pol0 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol0),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol0), };
        ICapturedImageFile[] pol45 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol45),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol45) };
        ICapturedImageFile[] pol90 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol90),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol90), };
        ICapturedImageFile[] pol135 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile_pol135),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile_pol135) };

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setName);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[3], pol135);

        return fileSet;
    }

    private boolean _isAcquisitionSetCorrectSize(AcquisitionSet acquisitionSet, int size) {
        return acquisitionSet.setSize() == size;
    }

    private boolean _doesCapturedImageSetExist(AcquisitionSet acquisitionSet, ICapturedImageFileSet fileSet) {
        Iterator<ICapturedImageFileSet> iterator = acquisitionSet.getIterator();

        try {
            ICapturedImageFileSet capturedSetInAcquisitionSet = acquisitionSet
                    .getCapturedImageSet(fileSet.getSetName());

            boolean isChannelsEqual = Arrays.equals(capturedSetInAcquisitionSet.getChannels(), fileSet.getChannels());

            boolean isFilesEqual = true;
            String[] labels = Cameras.getLabels(capturedSetInAcquisitionSet.getnCameras());
            for (String label : labels) {
                isFilesEqual &= capturedSetInAcquisitionSet.getFile(label).equals(fileSet.getFile(label));
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

class ReaderDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;
    private String setName;
    private int numChannels;

    ReaderDummyCapturedImageFileSet(String setName, int numChannels) {
        this.setName = setName;
        this.numChannels = numChannels;
    }

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
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.files.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();
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