package fr.fresnel.fourPolar.core.physics.channel;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.channel.CalibrationFactorOutOfRange;
import fr.fresnel.fourPolar.core.exceptions.physics.channel.WavelengthOutOfRange;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Represents the propagation channels of the imaging system. Each channel
 * corresponds to one wavelength.
 */
public class Channel implements IChannel {
    private double _wavelength;
    private Hashtable<Polarization, Double> _calibFact = new Hashtable<Polarization, Double>(4);

    /**
     * Represents the propagation channels of the imaging system. Each channel
     * corresponds to one wavelength.
     * 
     * @param wavelength is the channel wavelength in meter.
     * @param calibFactPol0 is the calibration factor of polarization 0.
     * @param calibFactPol45 is the calibration factor of polarization 45.
     * @param calibFactPol90 is the calibration factor of polarization 90.
     * @param calibFactPol135 is the calibration factor of polarization 135.
     */
    public Channel(double wavelength, double calibFactPol0, double calibFactPol45, double calibFactPol90,
            double calibFactPol135) throws WavelengthOutOfRange, CalibrationFactorOutOfRange {
        this._checkWavelength(wavelength);
        this._wavelength = wavelength;

        this._checkCalibrationFactor(calibFactPol0, calibFactPol45, calibFactPol90, calibFactPol135);
        this._calibFact.put(Polarization.pol0, calibFactPol0);
        this._calibFact.put(Polarization.pol45, calibFactPol45);
        this._calibFact.put(Polarization.pol90, calibFactPol90);
        this._calibFact.put(Polarization.pol135, calibFactPol135);
    }

    @Override
    public double getWavelength() {
        return this._wavelength;
    }

    @Override
    public double getCalibrationFactor(Polarization pol) {
        return this._calibFact.get(pol);
    }

    private void _checkWavelength(double wavelength) throws WavelengthOutOfRange {
        if (wavelength <= 0) {
            throw new WavelengthOutOfRange("Wavelength should be a positive value");
        }

    }

    private void _checkCalibrationFactor(double calibFactPol0, double calibFactPol45, double calibFactPol90,
            double calibFactPol135) throws CalibrationFactorOutOfRange {
        if (calibFactPol0 < 0 || calibFactPol45 < 0 || calibFactPol90 < 0 || calibFactPol135 < 0) {
            throw new CalibrationFactorOutOfRange("Negative calibration factors are not allowed");
        }

    }

    @Override
    public boolean equals(IChannel channel) {
        return channel.getWavelength() == this.getWavelength()
                && channel.getCalibrationFactor(Polarization.pol0) == this.getCalibrationFactor(Polarization.pol0)
                && channel.getCalibrationFactor(Polarization.pol45) == this.getCalibrationFactor(Polarization.pol45)
                && channel.getCalibrationFactor(Polarization.pol90) == this.getCalibrationFactor(Polarization.pol90)
                && channel.getCalibrationFactor(Polarization.pol135) == this.getCalibrationFactor(Polarization.pol135);

    }

}