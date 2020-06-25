package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Exception thrown when at least one {@link ICapturedImageFileSet} of an
 * {@link AcquisitionSet} can't be written to disk.
 */
public class AcquisitionSetWriteError extends IOException {
    private static final long serialVersionUID = 432029075341114324L;

    ArrayList<String> _failedSets = new ArrayList<>();

    @Override
    public String getMessage() {
        return "At least one captured image set file can't be written to disk due to IO issues.";
    }

    /**
     * Set the failed sets that could not be written
     */
    public void setFailedSets(String setName) {
        _failedSets.add(setName);
    }

    /**
     * @return an iterator over set names that could not be written to disk.
     */
    public Iterator<String> getFailedSets() {
        return null;
    }

    public boolean hasFaileSets() {
        return !_failedSets.isEmpty();
    }
}