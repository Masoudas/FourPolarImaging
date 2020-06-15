package fr.fresnel.fourPolar.core.preprocess.darkBackground;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/** An interface for accessing the dark background of a particular channel. */
public interface IChannelDarkBackground {
    /**
     * Return the background level for the given polarization.
     */
    public double getBackgroundLevel(Polarization polarization);

    /**
     * The channel number this dark background is associated with.
     * 
     * @return
     */
    public int channel();

    /**
     * @return a string description of the registration method.
     */
    public String estimationMethod();
}