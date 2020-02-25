package fr.fresnel.fourPolar.core.dataset.polarization;

import java.util.Iterator;

import fr.fresnel.fourPolar.core.physics.polarization.IPolarizationsIntensity;

/**
 * A cursor for iterating over the elements of a
 * {@link IPolarizationsIntensitySet}.
 */
public interface IPolarizationsIntensityCursor extends Iterator<IPolarizationsIntensity> {
    /**
     * Return the current position associated with the cursor.
     * 
     * @return
     */
    public long[] localize();

}