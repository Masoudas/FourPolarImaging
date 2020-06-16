package fr.fresnel.fourPolar.core.preprocess.registration;

import fr.fresnel.fourPolar.core.util.transform.Affine2D;

/**
 * Represents the results of a registration algorithm for a channel.
 */
public interface IChannelRegistrationResult {
    /**
     * @return a string description of the registration method.
     */
    public String registrationMethod();

    /**
     * The channel number this dark background is associated with.
     * 
     * @return
     */
    public int channel();

    /**
     * Returns the equivalent affine transform of this registration. The optional is
     * empty if {@link IChannelRegistrationResult#registrationSuccessful} returns
     * false.
     */
    public Affine2D getAffineTransform(RegistrationRule rule);

    /**
     * The registration error of the algorithm. The error would be negative if
     * {@link IChannelRegistrationResult#registrationSuccessful} returns false.
     * 
     * @return
     */
    public double error(RegistrationRule rule);

}