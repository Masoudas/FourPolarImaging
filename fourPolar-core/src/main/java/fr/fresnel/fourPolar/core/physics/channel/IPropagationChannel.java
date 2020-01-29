package fr.fresnel.fourPolar.core.physics.channel;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * The interface for accessing the information of different propagation channels.
 */
public interface IPropagationChannel {
    public double getWavelength();
    public double getCalibrationFactor(Polarization pol);
    
}