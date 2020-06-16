package fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration;

/**
 * Thrown in case the registration algorithm fails to register for any cause.
 */
public class RegistrationFailure extends Exception {
    private static final long serialVersionUID = 7499711200932393875L;

    /**
     * Create an exception, by registration a message indicating the failure reason.
     * 
     * @param failureReason is the description of why registration failed.
     */
    public RegistrationFailure(String failureReason) {
        super(failureReason);
    }

}