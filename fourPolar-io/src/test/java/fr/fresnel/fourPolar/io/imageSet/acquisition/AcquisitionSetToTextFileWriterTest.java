package fr.fresnel.fourPolar.io.imageSet.acquisition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.management.openmbean.KeyAlreadyExistsException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSetType;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;

public class AcquisitionSetToTextFileWriterTest {
    private static File _root;

    @BeforeAll
    private static void _createRoot() {
        _root = new File(AcquisitionSetToTextFileWriterTest.class.getResource("").getPath());
        _root.delete();
        _root.mkdirs();
    }

    @Test
    public void write_OneCameraSetWithTwoCapturedSets_WritesTwoFilesInTheResourceFolder()
            throws AcquisitionSetIOIssue {
        File root = new File(_root, "AcquisitionSetToTextWriterTest_OneCamera");
        IFourPolarImagingSetup setup = new DummyFPSetup(3, Cameras.One);

        String setOneName = "Set1";
        ICapturedImageFileSet set1 = _defineOneCameraFileSet(setOneName);

        String setTwoName = "Set2";
        ICapturedImageFileSet set2 = _defineOneCameraFileSet(setTwoName);

        WriterDummyAcquisitionSet acquisitionSet = new WriterDummyAcquisitionSet(AcquisitionSetType.Sample, root);
        acquisitionSet.addCapturedImageSet(set1);
        acquisitionSet.addCapturedImageSet(set2);

        AcquisitionSetToTextFileWriter writer = new AcquisitionSetToTextFileWriter(acquisitionSet, setup);

        writer.write();

        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setOneName));
        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setTwoName));

    }

    @Test
    public void write_TwoCameraSetWithTwoCapturedSets_WritesTwoFilesInTheResourceFolder()
            throws AcquisitionSetIOIssue {
        File root = new File(_root, "AcquisitionSetToTextWriterTest_TwoCamera");
        IFourPolarImagingSetup setup = new DummyFPSetup(3, Cameras.Two);

        String setOneName = "Set1";
        ICapturedImageFileSet set1 = _defineTwoCameraFileSet(setOneName);

        String setTwoName = "Set2";
        ICapturedImageFileSet set2 = _defineTwoCameraFileSet(setTwoName);

        WriterDummyAcquisitionSet acquisitionSet = new WriterDummyAcquisitionSet(AcquisitionSetType.Sample, root);
        acquisitionSet.addCapturedImageSet(set1);
        acquisitionSet.addCapturedImageSet(set2);

        AcquisitionSetToTextFileWriter writer = new AcquisitionSetToTextFileWriter(acquisitionSet, setup);

        writer.write();

        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setOneName));
        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setTwoName));

    }

    @Test
    public void write_FourCameraSetWithTwoCapturedSets_WritesTwoFilesInTheResourceFolder()
            throws AcquisitionSetIOIssue {
        File root = new File(_root, "AcquisitionSetToTextWriterTest_FourCamera");
        IFourPolarImagingSetup setup = new DummyFPSetup(3, Cameras.Four);

        String setOneName = "Set1";
        ICapturedImageFileSet set1 = _defineFourCameraFileSet(setOneName);

        String setTwoName = "Set2";
        ICapturedImageFileSet set2 = _defineFourCameraFileSet(setTwoName);

        WriterDummyAcquisitionSet acquisitionSet = new WriterDummyAcquisitionSet(AcquisitionSetType.Sample, root);
        acquisitionSet.addCapturedImageSet(set1);
        acquisitionSet.addCapturedImageSet(set2);

        AcquisitionSetToTextFileWriter writer = new AcquisitionSetToTextFileWriter(acquisitionSet, setup);

        writer.write();

        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setOneName));
        assertTrue(_isFileOnDisk(root, AcquisitionSetType.Sample, setTwoName));

    }

    private boolean _isFileOnDisk(File root, AcquisitionSetType setType, String setName) {
        File rootTextFiles = new File(PathFactoryOfProject.getFolder_Params(root), setType.description);
        return new File(rootTextFiles, setName + ".txt").exists();
    }

    private ICapturedImageFileSet _defineOneCameraFileSet(String setName) {
        int[] singleChannel = { 1 };
        File singleChannelFile = new File("singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("multiChannel.tif");

        ICapturedImageFile[] pol0_45_90_135 = { new WriterDummyCapturedImageFile(singleChannel, singleChannelFile),
                new WriterDummyCapturedImageFile(multiChannel, multiChannelFile) };

        WriterDummyCapturedImageFileSet fileSet = new WriterDummyCapturedImageFileSet(setName);
        fileSet.setFileSet(Cameras.getLabels(Cameras.One)[0], pol0_45_90_135);

        return fileSet;
    }

    private ICapturedImageFileSet _defineTwoCameraFileSet(String setName) {
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("multiChannel_pol45_135.tif");

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
        File singleChannelFile_pol0 = new File("singleChannel_pol0.tif");
        File singleChannelFile_pol45 = new File("singleChannel_pol45.tif");
        File singleChannelFile_pol90 = new File("singleChannel_pol90.tif");
        File singleChannelFile_pol135 = new File("singleChannel_pol135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0 = new File("multiChannel_pol0.tif");
        File multiChannelFile_pol45 = new File("multiChannel_pol45.tif");
        File multiChannelFile_pol90 = new File("multiChannel_pol90.tif");
        File multiChannelFile_pol135 = new File("multiChannel_pol135.tif");

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
}

class WriterDummyAcquisitionSet implements AcquisitionSet {
    Hashtable<String, ICapturedImageFileSet> _sets = new Hashtable<>();
    AcquisitionSetType type;
    File root;

    public WriterDummyAcquisitionSet(AcquisitionSetType type, File root) {
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
        return 0;
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

class WriterDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ICapturedImageFile[]> files = new Hashtable<>();
    private Cameras _cameras;
    private String setName;

    WriterDummyCapturedImageFileSet(String setName) {
        this.setName = setName;
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
        return null;
    }

}

class WriterDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public WriterDummyCapturedImageFile(int[] channels, File file) {
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

class DummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private int numChannels;

    public DummyFPSetup(int numChannels, Cameras camera) {
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