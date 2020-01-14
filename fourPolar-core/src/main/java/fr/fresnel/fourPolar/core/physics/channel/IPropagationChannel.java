package fr.fresnel.fourPolar.core.physics.channel;

import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * The interface for accessing the information of different propagation channels.
 */
public interface IPropagationChannel {
    public double getWavelength();
    public double getCalibrationFactor(Polarizations pol);
    
}