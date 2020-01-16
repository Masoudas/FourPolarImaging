package fr.fresnel.fourPolar.core.physics.na;

import java.util.Hashtable;
import fr.fresnel.fourPolar.core.physics.polarization.Polarizations;

/**
 * Models the numerical aperture of the imaging setup.
 */
public class NumericalAperture implements INumericalAperture {
    private Hashtable<Polarizations, Double> numericalAperture = new Hashtable<Polarizations, Double>(4);
    
    public NumericalAperture(double pol0, double pol45, double pol90, double pol135){
        numericalAperture.put(Polarizations.pol0, pol0);
        numericalAperture.put(Polarizations.pol45, pol45);
        numericalAperture.put(Polarizations.pol90, pol90);
        numericalAperture.put(Polarizations.pol135, pol135);
    }

    @Override
    public double getNA(Polarizations pol) {
        return numericalAperture.get(pol);
    }
    
}