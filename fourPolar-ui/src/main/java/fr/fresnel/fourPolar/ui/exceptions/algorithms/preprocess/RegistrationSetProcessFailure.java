package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess;

/**
 * An exception that is thrown in case of failure while processing the
 * {@link RegistrationImageSet} using {@link IRegistrationSetProcessor}.
 */
public class RegistrationSetProcessFailure extends Exception {
    public enum FailureReason {
        IO_ISSUES("Can't read the provided captured image sets."),
        REGISTRATION_FAILURE("Registering the image set is impossible.");

        private final String _reason;

        FailureReason(String reason) {
            this._reason = reason;
        }

        public String reason() {
            return _reason;
        }
    }

    private static final long serialVersionUID = -3879333985714233075L;
    private final FailureReason _reason;

    public RegistrationSetProcessFailure(FailureReason reason) {
        this._reason = reason;
    }

    @Override
    public String getMessage() {
        return this._reason.toString();
    }
}