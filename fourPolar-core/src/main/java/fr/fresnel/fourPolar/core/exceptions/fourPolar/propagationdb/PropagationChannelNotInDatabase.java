package fr.fresnel.fourPolar.core.exceptions.fourPolar.propagationdb;

/**
 * The exception that is thrown in case there's no {@link OpticalPropagation} is
 * not in the database of optical propagations.
 */
public class PropagationChannelNotInDatabase extends Exception {
    private static final long serialVersionUID = 4234549789843L;
    private final String _message;

    public PropagationChannelNotInDatabase() {
        _message = "No optical propagation is defined for the given propagation channel and numerical aperture in the database.";
    }

    public PropagationChannelNotInDatabase(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}