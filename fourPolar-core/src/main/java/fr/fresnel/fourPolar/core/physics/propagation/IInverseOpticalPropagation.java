package fr.fresnel.fourPolar.core.physics.propagation;

import fr.fresnel.fourPolar.core.physics.dipole.DipoleSquaredComponent;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * By inverse optical propagation we imply back propagation from (electrical)
 * intensity to an optical dipole. This interface is for accessing the inverse
 * propagation coefficients based on the {@link DipoleSquaredComponent} and
 * {@link Polarization}.
 */
public interface IInverseOpticalPropagation {
    /**
     * Returns the inverse propagation factor from the given polarization to the
     * given dipole sqquared component.
     * 
     * @param polarization is the desired polarization
     * @param component is the desired dipole squared component
     * @return the inverse factor.
     */
    public double getInverseFactor(Polarization polarization, DipoleSquaredComponent component);

    /**
     * Sets the inverse propagation coefficient, from the given polarization intensity to the
     * given dipole direction. See {@link DipoleSquaredComponent} and
     * {@link Polarization}.
     * 
     * @param direction
     * @param polarization
     * @param factor
     */
    public void setInverseFactor(Polarization polarization, DipoleSquaredComponent component, double factor);

    /**
     * Returns the optical propagation that corresponds to this inverse optical propagation.
     * @return
     */
    public IOpticalPropagation getOpticalPropagation();

}