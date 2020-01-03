package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.util.Base64;
import java.util.Hashtable;
import java.util.Set;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A file container, which holds the images provided in the raw constellation
 * format.
 */

public class CapturedImageFileSet implements ICapturedImageFileSet {
    private String setName = "";
    private Hashtable<String, File> fileSet = new Hashtable<String, File>();

    /**
     * Used for the case when only one camera is present.
     * @param pol0_45_90_135
     */
    public CapturedImageFileSet(File pol0_45_90_135) {
        fileSet.put("Pol0_45_90_135", pol0_45_90_135);

        setName = defineSetName(pol0_45_90_135);
    }

    /**
     * Used when two cameras are present.
     * @param pol0_90
     * @param pol45_135
     */
    public CapturedImageFileSet(File pol0_90, File pol45_135) {
        fileSet.put("Pol0_90", pol0_90);
        fileSet.put("Pol45_135", pol45_135);

        setName = defineSetName(pol0_90);
    }

    /**
     * Used when four cameras are present.
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     */
    public CapturedImageFileSet(File pol0, File pol45, File pol90, File pol135) {
        fileSet.put("Pol0", pol0);
        fileSet.put("Pol45", pol45);
        fileSet.put("Pol90", pol90);
        fileSet.put("Pol135", pol135);

        setName = defineSetName(pol0);
    }

    /**
     * Returns a string, comprising fileName + first five bytes of hash.
     * @param file
     * @return
     */
    private String defineSetName(File file) {
        byte[] hash = {0};
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest("inpudafesdat".getBytes(StandardCharsets.UTF_8));

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

        if (fileSet.getSetName().equals(this.setName))
            return true;

        return false;
    }

    @Override
    public Set<String> getLabels() {
        return fileSet.keySet();
    }

    @Override
    public File getFile(String label) {
        return fileSet.get(label);
    }

    @Override
    public String getSetName() {
        return setName;
    }


}