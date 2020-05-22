package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/** An interface for accessing the dark background of a particular channel. */
public interface IChannelDarkBackground {
    /**
     * Return the background level for the given polarization.
     */
    public double getBackgroundLevel(Polarization polarization);
}