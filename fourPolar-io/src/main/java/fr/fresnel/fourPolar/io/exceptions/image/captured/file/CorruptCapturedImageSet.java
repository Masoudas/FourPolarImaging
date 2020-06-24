package fr.fresnel.fourPolar.io.exceptions.image.captured.file;

/**
 * Exception thrown in case a captured image set does not exist on the disk, or
 * is somehow corrupted.
 */
public class CorruptCapturedImageSet extends Exception {
    private static final long serialVersionUID = 1L;

    private final String _setName;

    public CorruptCapturedImageSet(String setName) {
        _setName = setName;
    }

    @Override
    public String getMessage() {
        return "The captured set is corrupted or does not exist: " + _setName;
    }

    public String getSetName() {
        return _setName;
    }

}