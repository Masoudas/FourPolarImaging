package fr.fresnel.fourPolar.core.physics.na;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.na.NumericalApertureOutOfRange;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Models the numerical aperture of the imaging setup.
 */
public class NumericalAperture implements INumericalAperture {
    private Hashtable<Polarizations, Double> numericalAperture = new Hashtable<Polarizations, Double>(4);
    
    /**
     * Models the numerical aperture of the imaging setup.
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     * @throws NumericalApertureOutOfRange
     */
    public NumericalAperture(double pol0, double pol45, double pol90, double pol135) throws NumericalApertureOutOfRange{
        _checkNa(pol0, pol45, pol90, pol135);
        numericalAperture.put(Polarizations.pol0, pol0);
        numericalAperture.put(Polarizations.pol45, pol45);
        numericalAperture.put(Polarizations.pol90, pol90);
        numericalAperture.put(Polarizations.pol135, pol135);
    }

    @Override
    public double getNA(Polarizations pol) {
        return numericalAperture.get(pol);
    }

    private void _checkNa(double pol0, double pol45, double pol90, double pol135) throws NumericalApertureOutOfRange{
        if (pol0 < 0 || pol45 < 0 || pol90 < 0 || pol135 < 0) {
            throw new NumericalApertureOutOfRange("Negative values are not accepted");
        }
    }
    
}