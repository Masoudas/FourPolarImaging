package fr.fresnel.fourPolar.io.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;

public class ICapturedImageFileSetFromTextAdapterTest {
    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     * 
     * @throws CorruptCapturedImageSet
     */
    @Test
    public void getStringRepresentation_OneCameraCase_ReturnsCorrectString() throws CorruptCapturedImageSet {
        Cameras camera = Cameras.One;
        String setName = "OneCamera";

        int[] singleChannel = { 1 };
        File singleChannelFile = new File("/", "singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("/", "multiChannel.tif");

        ICapturedImageFile[] pol0_45_90_135 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile) };

        IFourPolarImagingSetup setup = new DummyFPSetup(singleChannel.length + multiChannel.length, camera);

        FromDummyCapturedImageFileSet fileSet = new FromDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_45_90_135);

        ICapturedImageFileSetToTextAdapter toAdapter = new ICapturedImageFileSetToTextAdapter(camera);
        Iterator<String[]> representors = toAdapter.toString(fileSet);

        ICapturedImageFileSetOneCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetOneCameraFromTextAdapter(
                setup, new DummyChecker());

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0_45_90_135[0]));
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0_45_90_135[1]));

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
        String setName = "TwoCamera";

        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("/", "singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("/", "singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("/", "multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("/", "multiChannel_pol45_135.tif");

        ICapturedImageFile[] pol0_90 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol0_90),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol0_90) };
        ICapturedImageFile[] pol45_135 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol45_135),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol45_135) };

        IFourPolarImagingSetup setup = new DummyFPSetup(singleChannel.length + multiChannel.length, camera);

        FromDummyCapturedImageFileSet fileSet = new FromDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45_135);

        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(camera);
        Iterator<String[]> representors = adapter.toString(fileSet);

        ICapturedImageFileSetTwoCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetTwoCameraFromTextAdapter(
                setup, new DummyChecker());

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0_90[0], pol45_135[0]));
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0_90[1], pol45_135[1]));

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
        String setName = "FourCamera";

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

        IFourPolarImagingSetup setup = new DummyFPSetup(singleChannel.length + multiChannel.length, camera);

        ICapturedImageFile[] pol0 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol0),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol0), };
        ICapturedImageFile[] pol45 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol45),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol45) };
        ICapturedImageFile[] pol90 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol90),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol90), };
        ICapturedImageFile[] pol135 = { new FromDummyCapturedImageFile(singleChannel, singleChannelFile_pol135),
                new FromDummyCapturedImageFile(multiChannel, multiChannelFile_pol135) };

        FromDummyCapturedImageFileSet fileSet = new FromDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);

        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(camera);
        Iterator<String[]> representors = adapter.toString(fileSet);

        ICapturedImageFileSetFourCameraFromTextAdapter fromAdaptor = new ICapturedImageFileSetFourCameraFromTextAdapter(
                setup, new DummyChecker());

        ICapturedImageFileSet set = fromAdaptor.fromString(representors, setName);
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0[0], pol45[0], pol90[0], pol135[0]));
        assertTrue(_isFromStringRepresentationCorrect(set, camera, pol0[1], pol45[1], pol90[1], pol135[1]));
    }

    @Test
    public void getStringRepresentation_EmptyFileStrings_ThrowsCorruptCapturedImageSet()
            throws CorruptCapturedImageSet {
        Cameras camera = Cameras.Four;
        IFourPolarImagingSetup setup = new DummyFPSetup(1, camera);

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
                setup, new DummyChecker());

        assertThrows(CorruptCapturedImageSet.class, () -> fromAdaptor.fromString(representorList.iterator(), setName));
    }

    private boolean _isFromStringRepresentationCorrect(ICapturedImageFileSet set, Cameras camera,
            ICapturedImageFile... files) {
        boolean equals = true;
        ICapturedImageFile[] label0Files = set.getFile(Cameras.getLabels(camera)[0]);

        for (int i = 0; i < label0Files.length && equals; i++) {
            equals &= Arrays.stream(label0Files).anyMatch(p -> Arrays.equals(files[0].channels(), p.channels()));
        }

        for (int i = 0; i < Cameras.getNImages(camera) && equals; i++) {
            String label = Cameras.getLabels(camera)[i];
            ICapturedImageFile originalFileOfLabel = files[i];
            equals &= Arrays.stream(set.getFile(label)).anyMatch(w -> w.file().equals(originalFileOfLabel.file()));
        }

        return equals;

    }

}

class FromDummyCapturedImageFileSet implements ICapturedImageFileSet {
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
        return null;
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

class DummyChecker implements ICapturedImageChecker {

    @Override
    public void check(ICapturedImageFile capturedImageFile) throws IncompatibleCapturedImage {

    }

}