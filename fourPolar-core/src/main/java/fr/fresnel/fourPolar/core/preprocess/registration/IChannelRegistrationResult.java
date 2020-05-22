package fr.fresnel.fourPolar.core.preprocess.registration;

import java.util.Map;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;

/**
 * Represents the results of a registration algorithm for a channel.
 */
public interface IChannelRegistrationResult {
    /**
     * Returns true if registration was successful. See also
     * {@link IRegistrationResult#getDescription()}.
     * 
     * @return
     */
    public boolean registrationSuccessful(RegistrationOrder rule);

    /**
     * Returns the equivalent affine transform of this registration.
     */
    public AffineTransform2D getAffineTransform(RegistrationOrder rule);

    /**
     * The registration error of the algorithm.
     * 
     * @return
     */
    public double error(RegistrationOrder rule);

    /**
     * Returns a string representation of the result of the algorithm. (Example: For
     * the bead descriptor based algorithm, it returns the number detected feature
     * points as a string, or that no feature points were detected).
     * 
     * @return
     */
    public String getDescription(RegistrationOrder rule);

    /**
     * Returns any possible resulting images that can be used to subjectively
     * qualify the result of registration. The string represents a discription of
     * what the image contains (Example: For the descripto based algorithm, the
     * description is "Color Image", which is simply the overlay of the two images).
     */
    public Map<String, Image<RGB16>> getResultFiles(RegistrationOrder rule);

}