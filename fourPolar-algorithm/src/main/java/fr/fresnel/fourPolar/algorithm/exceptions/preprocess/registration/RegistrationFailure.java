package fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration;

/**
 * Thrown in case the registration algorithm fails to register for any cause.
 */
public class RegistrationFailure extends Exception {
    private static final long serialVersionUID = 7499711200932393875L;

    public RegistrationFailure(String message) {
        super(message);
    }

}