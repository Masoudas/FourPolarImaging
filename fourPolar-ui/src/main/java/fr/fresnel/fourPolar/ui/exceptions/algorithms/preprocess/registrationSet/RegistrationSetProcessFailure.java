package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet;

/**
 * An exception that is thrown in case of failure while processing the
 * {@link RegistrationImageSet} using {@link IRegistrationSetProcessor}.
 */
public abstract class RegistrationSetProcessFailure extends Exception {
    private static final long serialVersionUID = -3879333985714233075L;

    @Override
    public String getMessage() {
        return this._createMessage();
    }

    protected abstract String _createMessage();
}