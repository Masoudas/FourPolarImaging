package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Exception thrown when at least one {@link ICapturedImageFileSet} of an
 * {@link AcquisitionSet} can't be written to /read from the disk.
 */
public class AcquisitionSetIOIssue extends IOException {
    private static final long serialVersionUID = 432029075341114324L;

    private final ArrayList<String> _failedSets = new ArrayList<>();
    private final String _message;

    public AcquisitionSetIOIssue(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }

    /**
     * Add failed set to the exception.
     */
    public void addFailedSet(String setName) {
        _failedSets.add(setName);
    }

    /**
     * @return an iterator over set names with IO issues.
     */
    public Iterator<String> getFailedSets() {
        return null;
    }

    public boolean hasFailedSets() {
        return !_failedSets.isEmpty();
    }
}