package fr.fresnel.fourPolar.core.dataset.polarization;

/**
 * An interface for accessing polarization intensity set, irrespective of the
 * underlying arrangement of the polarization data.
 */
public interface IPolarizationsIntensitySet {
    /**
     * Returns a cursor for iterating over the intensity data of the set.
     * @return
     */
    public IPolarizationsIntensityCursor getCursor();
    
}