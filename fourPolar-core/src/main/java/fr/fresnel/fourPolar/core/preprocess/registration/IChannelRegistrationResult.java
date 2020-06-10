package fr.fresnel.fourPolar.core.preprocess.registration;

import fr.fresnel.fourPolar.core.util.transform.Affine2D;

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
    public boolean registrationSuccessful(RegistrationRule rule);

    /**
     * Returns the equivalent affine transform of this registration.
     */
    public Affine2D getAffineTransform(RegistrationRule rule);

    /**
     * The registration error of the algorithm.
     * 
     * @return
     */
    public double error(RegistrationRule rule);

    /**
     * Returns a string representation of the result of the algorithm. (Example: For
     * the bead descriptor based algorithm, it returns the number detected feature
     * points as a string, or that no feature points were detected).
     * 
     * @return
     */
    public String getDescription(RegistrationRule rule);

    /**
     * The channel number this dark background is associated with.
     * 
     * @return
     */
    public int channel();

}