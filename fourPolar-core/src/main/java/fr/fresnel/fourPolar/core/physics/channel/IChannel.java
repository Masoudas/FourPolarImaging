package fr.fresnel.fourPolar.core.physics.channel;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * The interface for accessing the information of different propagation
 * channels.
 */
public interface IChannel {
    /**
     * @return the wavelength in meter.
     */
    public double getWavelength();

    /**
     * Returns the calibration factor of each polarization.
     * 
     * @param pol
     * @return
     */
    public double getCalibrationFactor(Polarization pol);

    /**
     * The method to check whether two propagation channels are equal.
     * 
     * @param channel
     * @return
     */
    boolean equals(IChannel channel);

}