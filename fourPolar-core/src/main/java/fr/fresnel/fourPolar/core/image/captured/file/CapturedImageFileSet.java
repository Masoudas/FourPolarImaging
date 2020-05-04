package fr.fresnel.fourPolar.core.image.captured.file;

import java.util.Arrays;
import java.util.Hashtable;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
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
    private final IMetadata _metadataIntersection;
    private String setName = "";
    final private Hashtable<String, File[]> fileSet = new Hashtable<String, File[]>();
    final private Cameras cameras;

    /**
     * Used for the case when only one camera is present.
     * 
     * @param metadataIntersection is the metadata associated with this single
     *                             polarization image file.
     * @param pol0_45_90_135       is the captured image file that has all four
     *                             polarizations.
     */
    public CapturedImageFileSet(IMetadata metadataIntersection, File[] pol0_45_90_135) {
        cameras = Cameras.One;

        String[] labels = Cameras.getLabels(cameras);
        fileSet.put(labels[0], pol0_45_90_135);

        this._metadataIntersection = metadataIntersection;
        setName = defineSetName(pol0_45_90_135[0]); // Use the first file as flag to define a set name.
    }

    /**
     * Used when two cameras are present.
     * 
     * @param metadataIntersection is the metadata that is common among the two
     *                             files (including the number of channels and axis
     *                             order).
     * @param pol0_90              is the captured image file that has polarizations
     *                             0 and 90.
     * @param pol45_135            is the captured image file that has polarizations
     *                             45 and 135.
     */
    public CapturedImageFileSet(IMetadata metadataIntersection, File[] pol0_90, File[] pol45_135) {
        cameras = Cameras.Two;
        String[] labels = Cameras.getLabels(cameras);

        fileSet.put(labels[0], pol0_90);
        fileSet.put(labels[1], pol45_135);

        this._metadataIntersection = metadataIntersection;
        setName = defineSetName(pol0_90[0]); // Use the first file as flag to define a set name.
    }

    /**
     * Used when four cameras are present.
     * 
     * @param metadataIntersection is the metadata that is common among the two
     *                             files (including the number of channels and axis
     *                             order).
     * @param pol0                 is the captured image file that has polarization
     *                             0.
     * @param pol45                is the captured image file that has polarization
     *                             45.
     * @param pol90                is the captured image file that has polarization
     *                             90.
     * @param pol135               is the captured image file that has polarization
     *                             135.
     */
    public CapturedImageFileSet(IMetadata metadataIntersection, File[] pol0, File[] pol45, File[] pol90,
            File[] pol135) {
        cameras = Cameras.Four;
        String[] labels = Cameras.getLabels(cameras);

        fileSet.put(labels[0], pol0);
        fileSet.put(labels[1], pol45);
        fileSet.put(labels[2], pol90);
        fileSet.put(labels[3], pol135);

        this._metadataIntersection = metadataIntersection;
        setName = defineSetName(pol0[0]); // Use the first file as flag to define a set name.
    }

    /**
     * Define set name, which comprieses the flag file name + the hash of the
     * complete path to the file. The hash is added so that if the flag file name of
     * two different files are equal, the set name would be different thanks to
     * hash.
     * 
     */
    private String defineSetName(File file) {
        byte[] hash = { 0 };
        String concatenatedPaths = file.getAbsolutePath();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(concatenatedPaths.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        return file.getName().substring(0, file.getName().indexOf('.')) + " " + hexString.toString().substring(0, 5);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ICapturedImageFileSet))
            return false;

        ICapturedImageFileSet fileSet = (ICapturedImageFileSet) obj;
        String[] labels = Cameras.getLabels(cameras);

        if (labels.length != this.fileSet.size())
            return false;

        for (String label : labels) {
            if (!fileSet.getFile(label).equals(this.getFile(label))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public File[] getFile(String label) {
        return fileSet.get(label);
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

    @Override
    public IMetadata getMetadataIntersecion() {
        return this._metadataIntersection;
    }
}