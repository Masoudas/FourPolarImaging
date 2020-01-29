package fr.fresnel.fourPolar.core.physics.polarization;

import java.util.Hashtable;

/**
 * Models the propagated intensity from the sample for all polarizations.
 */
public class PolarizationsIntensity implements IPolarizationsIntensity{
    private Hashtable<Polarization, Double> _intensity = new Hashtable<Polarization, Double>(4);
    
    public PolarizationsIntensity(double pol0, double pol45, double pol90, double pol135){
        this._intensity.put(Polarization.pol0, pol0);
        this._intensity.put(Polarization.pol45, pol45);
        this._intensity.put(Polarization.pol90, pol90);
        this._intensity.put(Polarization.pol135, pol135);
        
    }

    @Override
    public double getIntensity(Polarization pol) {
        return this._intensity.get(pol);
    }
    
    
}