package fr.fresnel.fourPolar.core.preprocess.registration;

import java.util.Optional;

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
     * Returns the equivalent affine transform of this registration. The optional is
     * empty if {@link IChannelRegistrationResult#registrationSuccessful} returns
     * false.
     */
    public Optional<Affine2D> getAffineTransform(RegistrationRule rule);

    /**
     * The registration error of the algorithm. The error would be negative if
     * {@link IChannelRegistrationResult#registrationSuccessful} returns false.
     * 
     * @return
     */
    public double error(RegistrationRule rule);

    /**
     * Returns a string representation of the failure reason of the algorithm.
     * (Example: For the bead descriptor based algorithm, it says that no feature
     * points were detected). The optional is empty if
     * {@link IChannelRegistrationResult#registrationSuccessful} returns true.
     * 
     */
    public Optional<String> getFailureDescription(RegistrationRule rule);

    /**
     * The channel number this dark background is associated with.
     * 
     * @return
     */
    public int channel();

    /**
     * @return a string description of the registration method.
     */
    public String registrationMethod();
}