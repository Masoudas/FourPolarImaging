package fr.fresnel.fourPolar.io.image.captured.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFile;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.IChannel;
import fr.fresnel.fourPolar.core.physics.na.INumericalAperture;

public class ICapturedImageFileSetToTextAdapterTest {
    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     */
    @Test
    public void getStringRepresentation_OneCameraCase_ReturnsCorrectString() {
        Cameras camera = Cameras.One;
        int[] singleChannel = { 1 };
        File singleChannelFile = new File("/", "singleChannel.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile = new File("/", "multiChannel.tif");

        ICapturedImageFile[] pol0_45_90_135 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile) };

        ToDummyCapturedImageFileSet fileSet = new ToDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_45_90_135);

        IFourPolarImagingSetup setup = new ToTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);
        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = adapter.toString(fileSet);

        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0_45_90_135));
        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0_45_90_135));

    }

    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     */
    @Test
    public void getStringRepresentation_TwoCameraCase_ReturnsCorrectString() {
        Cameras camera = Cameras.Two;
        int[] singleChannel = { 1 };
        File singleChannelFile_pol0_90 = new File("/", "singleChannel_pol0_90.tif");
        File singleChannelFile_pol45_135 = new File("/", "singleChannel_pol45_135.tif");

        int[] multiChannel = { 2, 3 };
        File multiChannelFile_pol0_90 = new File("/", "multiChannel_pol0_90.tif");
        File multiChannelFile_pol45_135 = new File("/", "multiChannel_pol45_135.tif");

        ICapturedImageFile[] pol0_90 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol0_90),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol0_90) };
        ICapturedImageFile[] pol45_135 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol45_135),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol45_135) };

        ToDummyCapturedImageFileSet fileSet = new ToDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0_90);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45_135);

        IFourPolarImagingSetup setup = new ToTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);
        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = adapter.toString(fileSet);

        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0_90, pol45_135));
        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0_90, pol45_135));

    }

    /**
     * Create an artificial case of both single channel and multi-channel captured
     * files
     */
    @Test
    public void getStringRepresentation_FourCameraCase_ReturnsCorrectString() {
        Cameras camera = Cameras.Four;
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

        ICapturedImageFile[] pol0 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol0),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol0), };
        ICapturedImageFile[] pol45 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol45),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol45) };
        ICapturedImageFile[] pol90 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol90),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol90), };
        ICapturedImageFile[] pol135 = { new ToDummyCapturedImageFile(singleChannel, singleChannelFile_pol135),
                new ToDummyCapturedImageFile(multiChannel, multiChannelFile_pol135) };

        ToDummyCapturedImageFileSet fileSet = new ToDummyCapturedImageFileSet();
        fileSet.setFileSet(Cameras.getLabels(camera)[0], pol0);
        fileSet.setFileSet(Cameras.getLabels(camera)[1], pol45);
        fileSet.setFileSet(Cameras.getLabels(camera)[2], pol90);
        fileSet.setFileSet(Cameras.getLabels(camera)[3], pol135);

        IFourPolarImagingSetup setup = new ToTextDummyFPSetup(singleChannel.length + multiChannel.length, camera);
        ICapturedImageFileSetToTextAdapter adapter = new ICapturedImageFileSetToTextAdapter(setup);
        Iterator<String[]> representors = adapter.toString(fileSet);

        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0, pol45, pol90, pol135));
        assertTrue(_isStringRepresentationCorrect(representors.next(), camera, pol0, pol45, pol90, pol135));

    }

    private boolean _isStringRepresentationCorrect(String[] strings, Cameras camera, ICapturedImageFile[]... files) {
        boolean equals = Arrays.stream(files[0])
                .anyMatch(p -> strings[0].equals("Channels: " + Arrays.toString(p.channels())));

        for (int i = 1; i < strings.length && equals; i++) {
            String label = Cameras.getLabels(camera)[i - 1];
            String str = strings[i];

            equals &= Arrays.stream(files[i - 1]).anyMatch(p -> str.equals(label + ": " + p.file().getAbsolutePath()));
        }

        return equals;

    }

}

class ToTextDummyFPSetup implements IFourPolarImagingSetup {
    private Cameras cameras;
    private int numChannels;

    public ToTextDummyFPSetup(int numChannels, Cameras camera) {
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


class ToDummyCapturedImageFileSet implements ICapturedImageFileSet {
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

class ToDummyCapturedImageFile implements ICapturedImageFile {
    private int[] _channels;
    private File _file;

    public ToDummyCapturedImageFile(int[] channels, File file) {
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