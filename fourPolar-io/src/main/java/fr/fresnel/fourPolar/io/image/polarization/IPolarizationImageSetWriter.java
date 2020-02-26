package fr.fresnel.fourPolar.io.image.polarization;

import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;

/**
 * An interface for writing a {@link IPolarizationImageSet}. Note that several images can
 * be written with the same interface.
 */
public interface IPolarizationImageSetWriter {
    /**
     * Writes the image set to the destinations inherent in the {@link IPolarizationImageSet}.
     * @param imageSet
     */
    public void write(IPolarizationImageSet imageSet);
    
}
