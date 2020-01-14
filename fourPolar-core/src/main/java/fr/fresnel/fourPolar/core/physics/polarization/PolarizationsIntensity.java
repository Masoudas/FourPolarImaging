package fr.fresnel.fourPolar.core.physics.polarization;

import java.util.Hashtable;

/**
 * Models the propagated intensity from the sample for all polarizations.
 */
public class PolarizationsIntensity implements IPolarizationsIntensity{
    private Hashtable<Polarizations, Double> _intensity = new Hashtable<Polarizations, Double>(4);
    
    public PolarizationsIntensity(double pol0, double pol45, double pol90, double pol135){
        this._intensity.put(Polarizations.pol0, pol0);
        this._intensity.put(Polarizations.pol45, pol45);
        this._intensity.put(Polarizations.pol90, pol90);
        this._intensity.put(Polarizations.pol135, pol135);
        
    }

    @Override
    public double getIntensity(Polarizations pol) {
        return this._intensity.get(pol);
    }
    
    
}