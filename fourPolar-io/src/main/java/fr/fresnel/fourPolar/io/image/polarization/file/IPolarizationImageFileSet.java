package fr.fresnel.fourPolar.io.image.polarization.file;

import java.io.File;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for accessing the files corresponding to a set of polarization
 * images of a particular channel.
 */
public interface IPolarizationImageFileSet {
    /**
     * Returns the file corresponding to the given polarization image.
     * 
     * @param pol
     * @return
     */
    public File getFile(Polarization pol);
}