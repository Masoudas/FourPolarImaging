package fr.fresnel.fourPolar.core.physics.polarization;

/**
 * The interface for accessing the propagated intensity of each polarization
 */
public interface IPolarizationsIntensity {
    public double getIntensity(Polarization pol);
}