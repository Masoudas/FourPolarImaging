package fr.fresnel.fourPolar.core.physics.channel;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.channel.CalibrationFactorOutOfRange;
import fr.fresnel.fourPolar.core.exceptions.physics.channel.WavelengthOutOfRange;
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
        double wavelength, double calibFactPol0, double calibFactPol45, double calibFactPol90, double calibFactPol135) throws
        WavelengthOutOfRange, CalibrationFactorOutOfRange{
        this._checkWavelength(wavelength);
        this._wavelength = wavelength;

        this._checkCalibrationFactor(calibFactPol0, calibFactPol45, calibFactPol90, calibFactPol135);
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

    private void _checkWavelength(double wavelength) throws WavelengthOutOfRange{
        double lowerWavelength = 1e-9;
        double upperWavelength = 10e-9;

        if (wavelength < lowerWavelength || wavelength > upperWavelength){
            throw new WavelengthOutOfRange("The wavelength is not in the working range of the FourPolar setup");
        } 
        
    }

    private void _checkCalibrationFactor(double calibFactPol0, double calibFactPol45, double calibFactPol90, double calibFactPol135) 
    throws CalibrationFactorOutOfRange{
        if (calibFactPol0 < 0 || calibFactPol45 < 0 || calibFactPol90 < 0 || calibFactPol135 < 0){
            throw new CalibrationFactorOutOfRange("Negative calibration factors are not allowed");
        }
        
    }

}