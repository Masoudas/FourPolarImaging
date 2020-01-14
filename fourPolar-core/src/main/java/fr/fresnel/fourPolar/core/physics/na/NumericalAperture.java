package fr.fresnel.fourPolar.core.physics.na;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Models the numerical aperture of the imaging setup.
 */
public class NumericalAperture implements INumericalAperture {
    Hashtable<Polarizations, Double> na = new Hashtable<Polarizations, Double>(4);
    public NumericalAperture(double pol0, double pol45, double pol90, double pol135){
        na.put(Polarizations.pol0, pol0);
        na.put(Polarizations.pol45, pol45);
        na.put(Polarizations.pol90, pol90);
        na.put(Polarizations.pol135, pol135);
    }

    @Override
    public double getNA(Polarizations pol) {
        return na.get(pol);
    }

    
}