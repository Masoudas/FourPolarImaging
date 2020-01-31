package fr.fresnel.fourPolar.core.physics.propagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * By optical propagation we mean propagation from optical dipole to electrical
 * intensity. This interface is for accessing the propagation coefficients based
 * on the {@link DipoleSquaredComponent} and {@link Polarization}.
 */
public interface IOpticalPropagation {
    /**
     * Returns the propagation coefficient from the given dipole direction to the
     * given polarization intensity. See {@link DipoleSquaredComponent} and
     * {@link Polarization}.
     * 
     * @param direction
     * @param polarization
     * @return
     */
    public double getPropagationFactor(DipoleSquaredComponent direction, Polarization polarization);

    /**
     * Sets the propagation coefficient, from the given dipole direction to the
     * given polarization intensity. See {@link DipoleSquaredComponent} and
     * {@link Polarization}.
     * 
     * @param direction
     * @param polarization
     * @param factor
     */
    public void setPropagationFactor(DipoleSquaredComponent direction, Polarization polarization, double factor);

}