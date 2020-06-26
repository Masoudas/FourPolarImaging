package fr.fresnel.fourPolar.io.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;

public class ICapturedImageFileSetFromTextAdapterTest {
    final static String[] setNames = { "Set1", "Set2" };

    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     * 
     * @throws CorruptCapturedImageSet
     */
    @Test
    public void getStringRepresentation_OneCameraCase_ReturnsCorrectString() throws CorruptCapturedImageSet {
        Cameras camera = Cameras.One;
        String setName = setNames[0];

        int[] singleChannel = { 1 };
        File singleChannelFile = new File("singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("multiChannel.tif");

        FromDummyCapturedSetBuilder builder = new FromDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length);
        builder.add(singleChannel, singleChannelFile);
        builder.add(multiChannel, multiChannelFile);
        ICapturedImageFileSet fileSet = builder.build();

        IFourPolarImagingSetup setup = new FromTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);

        ICapturedImageFileSetToTextAdapter toAdapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = toAdapter.toString(fileSet);

        ICapturedImageFileSetOneCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetOneCameraFromTextAdapter(
                setup, new DummyChecker(), new FromDummyCapturedSetBuilder(singleChannel.length + multiChannel.length));

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, fileSet));

    }

    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     * 
     * @throws CorruptCapturedImageSet
     */
    @Test
    public void getStringRepresentation_TwoCameraCase_ReturnsCorrectString() throws CorruptCapturedImageSet {
        Cameras camera = Cameras.Two;
        String setName = setNames[0];

        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("multiChannel_pol45_135.tif");

        FromDummyCapturedSetBuilder builder = new FromDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length);
        builder.add(singleChannel, singleChannelFile_pol0_90, singleChannelFile_pol45_135);
        builder.add(multiChannel, multiChannelFile_pol0_90, multiChannelFile_pol45_135);

        ICapturedImageFileSet fileSet = builder.build();

        IFourPolarImagingSetup setup = new FromTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);

        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = adapter.toString(fileSet);

        ICapturedImageFileSetTwoCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetTwoCameraFromTextAdapter(
                setup, new DummyChecker(), new FromDummyCapturedSetBuilder(singleChannel.length + multiChannel.length));

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, fileSet));

    }

    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     * 
     * @throws CorruptCapturedImageSet
     */
    @Test
    public void getStringRepresentation_FourCameraCase_ReturnsCorrectString() throws CorruptCapturedImageSet {
        Cameras camera = Cameras.Four;
        String setName = setNames[0];

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

        IFourPolarImagingSetup setup = new FromTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);

        FromDummyCapturedSetBuilder builder = new FromDummyCapturedSetBuilder(
                singleChannel.length + multiChannel.length);
        builder.add(singleChannel, singleChannelFile_pol0, singleChannelFile_pol45, singleChannelFile_pol90,
                singleChannelFile_pol135);
        builder.add(multiChannel, multiChannelFile_pol0, multiChannelFile_pol45, multiChannelFile_pol90,
                multiChannelFile_pol135);
        ICapturedImageFileSet fileSet = builder.build();

        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = adapter.toString(fileSet);

        ICapturedImageFileSetFourCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetFourCameraFromTextAdapter(
                setup, new DummyChecker(), new FromDummyCapturedSetBuilder(singleChannel.length + multiChannel.length));

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, fileSet));
    }

    @Test
    public void getStringRepresentation_EmptyFileStrings_ThrowsCorruptCapturedImageSet()
            throws CorruptCapturedImageSet {
        Cameras camera = Cameras.Four;
        IFourPolarImagingSetup setup = new FromTextDummyFPSetup(1, camera);

        String setName = "FourCamera";

        String[] setAsString = new String[5];
        setAsString[0] = "Channels: [1]";
        setAsString[1] = " ";
        setAsString[2] = " ";
        setAsString[3] = " ";
        setAsString[4] = " ";

        ArrayList<String[]> representorList = new ArrayList<>();
        representorList.add(setAsString);

        ICapturedImageFileSetFourCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetFourCameraFromTextAdapter(
                setup, new DummyChecker(), new FromDummyCapturedSetBuilder(1));

        assertThrows(CorruptCapturedImageSet.class, () -> fromAdaptor.fromString(representorList.iterator(), setName));
    }

    private boolean _isFromStringRepresentationCorrect(ICapturedImageFileSet set, Cameras camera,
            ICapturedImageFileSet files) {
        boolean equals = true;

        for (int i = 0; i < Cameras.getNImages(camera) && equals; i++) {
            String label = Cameras.getLabels(camera)[i];
            ICapturedImageFile[] originalFileOfLabel = files.getFile(label);
            equals &= Arrays.stream(set.getFile(label))
                    .allMatch(w -> Arrays.stream(originalFileOfLabel).anyMatch(p -> w.file().equals(p.file())));
        }

        return equals;

    }

}

class FromDummyCapturedImageFileSet implements ICapturedImageFileSet {
    private Hashtable<String, ArrayList<ICapturedImageFile>> files = new Hashtable<>();
    private Cameras _cameras;
    private String setName;
    private int numChannels;

    FromDummyCapturedImageFileSet(int numChannels) {

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

class FromDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public FromDummyCapturedImageFile(int[] channels, File file) {
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

class FromTextDummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private int numChannels;

    public FromTextDummyFPSetup(int numChannels, Cameras camera) {
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

class FromDummyCapturedSetBuilder implements ICapturedImageFileSetBuilder {
    int numChannels;
    int counter = 0;
    FromDummyCapturedImageFileSet fileSet;

    FromDummyCapturedSetBuilder(int numChannels) {
        this.numChannels = numChannels;
        fileSet = new FromDummyCapturedImageFileSet(numChannels);
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_45_90_135) {
        fileSet.setCameras(Cameras.One);
        fileSet.setFileSet(Cameras.getLabels(Cameras.One)[0], new FromDummyCapturedImageFile(channels, pol0_45_90_135));
        return this;
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_90, File pol45_135) {
        fileSet.setCameras(Cameras.Two);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[0], new FromDummyCapturedImageFile(channels, pol0_90));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Two)[1], new FromDummyCapturedImageFile(channels, pol45_135));
        return null;
    }

    @Override
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0, File pol45, File pol90, File pol135) {
        fileSet.setCameras(Cameras.Four);
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[0], new FromDummyCapturedImageFile(channels, pol0));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[1], new FromDummyCapturedImageFile(channels, pol45));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[2], new FromDummyCapturedImageFile(channels, pol90));
        fileSet.setFileSet(Cameras.getLabels(Cameras.Four)[3], new FromDummyCapturedImageFile(channels, pol135));

        return this;
    }

    @Override
    public ICapturedImageFileSet build() {
        fileSet.setSetName(ICapturedImageFileSetFromTextAdapterTest.setNames[counter++]);
        FromDummyCapturedImageFileSet fileSetTemp = fileSet;
        fileSet = new FromDummyCapturedImageFileSet(numChannels);

        return fileSetTemp;

    }

}
