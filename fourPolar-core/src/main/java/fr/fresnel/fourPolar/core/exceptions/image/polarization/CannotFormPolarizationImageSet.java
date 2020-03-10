package fr.fresnel.fourPolar.core.exceptions.image.polarization;

/**
 * Exception thrown when a polarization image set cannot be formed based on the
 * provided images.
 */
public class CannotFormPolarizationImageSet extends Exception {
    private static final long serialVersionUID = 4806384446543724304L;
    final private String _message;

    public CannotFormPolarizationImageSet(String message){
        this._message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}