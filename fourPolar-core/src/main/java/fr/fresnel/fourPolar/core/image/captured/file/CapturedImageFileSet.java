package fr.fresnel.fourPolar.core.image.captured.file;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A file container, which holds the images provided in the raw captured format.
 */
class CapturedImageFileSet implements ICapturedImageFileSet {
    private String setName = "";
    final private Hashtable<String, ICapturedImageFile[]> fileSet = new Hashtable<String, ICapturedImageFile[]>();
    final private Cameras cameras;
    final private int[] channels;

    /**
     * Used for the case when only one camera is present.
     * 
     * @param pol0_45_90_135 is the captured image file that has all four
     *                       polarizations.
     */
    public CapturedImageFileSet(ICapturedImageFile[] pol0_45_90_135) {
        cameras = Cameras.One;

        String[] labels = Cameras.getLabels(cameras);
        fileSet.put(labels[0], pol0_45_90_135);

        File flagFile = _getFlagFile(pol0_45_90_135);
        setName = defineSetName(flagFile); // Use the first file as flag to define a set name.

        this.channels = this._setChannels(pol0_45_90_135);
    }

    /**
     * Used when two cameras are present.
     * 
     * @param pol0_90   is the captured image file that has polarizations 0 and 90.
     * @param pol45_135 is the captured image file that has polarizations 45 and
     *                  135.
     */
    public CapturedImageFileSet(ICapturedImageFile[] pol0_90, ICapturedImageFile[] pol45_135) {
        cameras = Cameras.Two;
        String[] labels = Cameras.getLabels(cameras);

        fileSet.put(labels[0], pol0_90);
        fileSet.put(labels[1], pol45_135);

        File flagFile = _getFlagFile(pol0_90);
        setName = defineSetName(flagFile);

        this.channels = this._setChannels(pol0_90);
    }

    /**
     * Used when four cameras are present.
     * 
     * @param pol0   is the captured image file that has polarization 0.
     * @param pol45  is the captured image file that has polarization 45.
     * @param pol90  is the captured image file that has polarization 90.
     * @param pol135 is the captured image file that has polarization 135.
     */
    public CapturedImageFileSet(ICapturedImageFile[] pol0, ICapturedImageFile[] pol45, ICapturedImageFile[] pol90,
            ICapturedImageFile[] pol135) {
        cameras = Cameras.Four;
        String[] labels = Cameras.getLabels(cameras);

        fileSet.put(labels[0], pol0);
        fileSet.put(labels[1], pol45);
        fileSet.put(labels[2], pol90);
        fileSet.put(labels[3], pol135);

        File flagFile = _getFlagFile(pol0);
        setName = defineSetName(flagFile);

        this.channels = this._setChannels(pol0);
    }

    /**
     * Defines the set name associated with the given fileSet. The set name is
     * defined using the flag file name + hash of flag file full path with the
     * following rules:
     * <ul>
     * <li>One camera: Flag file is first channel (or all channels in multi-channel
     * case).</li>
     * <li>Two cameras: Flag file is pol0-90 of first channel (or all channels in
     * multi-channel case).</li>
     * <li>Two cameras: Flag file is pol0 of first channel (or all channels in
     * multi-channel case).</li>
     * 
     * </ul>
     */
    private String defineSetName(File flagFile) {
        byte[] hash = { 0 };
        String concatenatedPaths = flagFile.getAbsolutePath();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(concatenatedPaths.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        return flagFile.getName().substring(0, flagFile.getName().lastIndexOf('.')) + " "
                + hexString.toString().substring(0, 5);
    }

    @Override
    public boolean equals(Object obj) {
        String setNameToCompare = null;
        if (obj instanceof ICapturedImageFileSet) {
            ICapturedImageFileSet fileset = (ICapturedImageFileSet) obj;
            setNameToCompare = fileset.getSetName();
        } else if (obj instanceof String) {
            setNameToCompare = (String) obj;
        } else {
            return false;
        }

        return this.getSetName().equals(setNameToCompare);

    }

    @Override
    public ICapturedImageFile[] getFile(String label) {
        return fileSet.get(label).clone();
    }

    @Override
    public String getSetName() {
        return setName;
    }

    @Override
    public int hashCode() {
        return setName.hashCode();
    }

    @Override
    public Cameras getnCameras() {
        return this.cameras;
    }

    @Override
    public boolean hasLabel(String label) {
        String[] labels = Cameras.getLabels(cameras);
        if (!Arrays.stream(labels).anyMatch(label::equals)) {
            return false;
        }

        return true;
    }

    private File _getFlagFile(ICapturedImageFile[] files) {
        File channelOneFile = null;
        boolean foundChannelOne = false;
        for (int i = 0; i < files.length && !foundChannelOne; i++) {
            if (files[i].channels()[0] == 1) {
                channelOneFile = files[i].file();
            }
        }

        return channelOneFile;
    }

    @Override
    public boolean deepEquals(ICapturedImageFileSet fileset) {
        Objects.requireNonNull(fileset, "fileset can't be null");

        String[] labels = Cameras.getLabels(cameras);
        if (labels.length != this.fileSet.size())
            return false;

        for (String label : labels) {

            if (Arrays.deepEquals(this.getFile(label), fileset.getFile(label))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Iterator<ICapturedImageFile> getIterator() {
        Stream<ICapturedImageFile> concatStream = Stream.empty();
        for (Iterator<ICapturedImageFile[]> iterator = this.fileSet.values().iterator(); iterator.hasNext();) {
            concatStream = Stream.concat(concatStream, Arrays.stream(iterator.next()));
        }

        return concatStream.iterator();

    }

    @Override
    public int[] getChannels() {
        return this.channels.clone();
    }

    /**
     * Determines what channels this file set correspond to, by looking at the
     * channels of a particular camera.
     * 
     * @param cameraFiles
     * @return
     */
    private int[] _setChannels(ICapturedImageFile[] cameraFiles) {
        IntStream channels = IntStream.empty();
        for (ICapturedImageFile capturedImageFile : cameraFiles) {
            channels = IntStream.concat(channels, Arrays.stream(capturedImageFile.channels()));
        }

        return channels.toArray();
    }
}