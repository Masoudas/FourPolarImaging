package fr.fresnel.fourPolar.core.preprocess.registration;

import java.io.File;
import java.util.Map;

import fr.fresnel.fourPolar.core.util.transform.AffineTransform;

/**
 * Represents the results of a registration algorithm.
 */
public interface IRegistrationResult {
    /**
     * Returns true if registration was successful. See also
     * {@link IRegistrationResult#getDescription()}.
     * 
     * @return
     */
    public boolean registrationSuccessful();

    /**
     * Returns the equivalent affine transform of this registration.
     */
    public AffineTransform getAffineTransform();

    /**
     * The registration error of the algorithm.
     * 
     * @return
     */
    public double error();

    /**
     * Returns a string representation of the result of the algorithm. (Example: For
     * the bead descriptor based algorithm, it returns the number detected feature
     * points as a string, or that no feature points were detected).
     * 
     * @return
     */
    public String getDescription();

    /**
     * Returns any possible resulting image files of the registration algorithm,
     * that can be used to subjectively qualify the result of registration. The
     * string represents a discription of what the image contains (Example: For the
     * descripto based algorithm, the descriptions are "Color Image" and "Marker
     * Image", which are simply to image representations of the outcome of the
     * registration).
     */
    public Map<String, File> getResultFiles();

}