package fr.fresnel.fourPolar.core.physics.channel;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Represents the propagation channels of the imaging system. Each channel
 * corresponds to one wavelength.
 */
public class PropagationChannel implements IPropagationChannel {
    private double _wavelength;
    private Hashtable<Polarizations, Double> _calibFact = new Hashtable<Polarizations, Double>(4);

    /**
     * Represents the propagation channels of the imaging system. Each channel
     * corresponds to one wavelength.
     * 
     * @param wavelength
     * @param calibFactPol0
     * @param calibFactPol45
     * @param calibFactPol90
     * @param calibFactPol135
     */
    public PropagationChannel(
        double wavelength, double calibFactPol0, double calibFactPol45, double calibFactPol90, double calibFactPol135){
        this._wavelength = wavelength;

        this._calibFact.put(Polarizations.pol0, calibFactPol0);
        this._calibFact.put(Polarizations.pol45, calibFactPol45);
        this._calibFact.put(Polarizations.pol90, calibFactPol90);
        this._calibFact.put(Polarizations.pol135, calibFactPol135);
    }

    @Override
    public double getWavelength() {
        return this._wavelength;
    }

    @Override
    public double getCalibrationFactor(Polarizations pol) {
        return this._calibFact.get(pol);
    }

}