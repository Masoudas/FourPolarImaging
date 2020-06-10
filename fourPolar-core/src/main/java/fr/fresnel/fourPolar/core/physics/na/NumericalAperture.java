package fr.fresnel.fourPolar.core.physics.na;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.exceptions.physics.na.NumericalApertureOutOfRange;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Models the numerical aperture of the imaging setup.
 */
public class NumericalAperture implements INumericalAperture {
    private Hashtable<Polarization, Double> numericalAperture = new Hashtable<Polarization, Double>(4);

    /**
     * Models the numerical aperture of the imaging setup.
     * 
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     * @throws NumericalApertureOutOfRange
     */
    public NumericalAperture(double pol0, double pol45, double pol90, double pol135)
            throws NumericalApertureOutOfRange {
        _checkNa(pol0, pol45, pol90, pol135);
        numericalAperture.put(Polarization.pol0, pol0);
        numericalAperture.put(Polarization.pol45, pol45);
        numericalAperture.put(Polarization.pol90, pol90);
        numericalAperture.put(Polarization.pol135, pol135);
    }

    @Override
    public double getNA(Polarization pol) {
        return numericalAperture.get(pol);
    }

    private void _checkNa(double pol0, double pol45, double pol90, double pol135) throws NumericalApertureOutOfRange {
        if (Double.isNaN(pol0) || Double.isNaN(pol45) || Double.isNaN(pol90) || Double.isNaN(pol135)) {
            throw new NumericalApertureOutOfRange("Numerical aperture cannot be nan.");
        }

        if (pol0 <= 0 || pol45 <= 0 || pol90 <= 0 || pol135 <= 0) {
            throw new NumericalApertureOutOfRange("Numerical aperture should be positive");
        }
    }

    @Override
    public boolean equals(INumericalAperture na) {
        return this.getNA(Polarization.pol0) == na.getNA(Polarization.pol0)
                && this.getNA(Polarization.pol45) == na.getNA(Polarization.pol45)
                && this.getNA(Polarization.pol90) == na.getNA(Polarization.pol90)
                && this.getNA(Polarization.pol135) == na.getNA(Polarization.pol135);
    }

}