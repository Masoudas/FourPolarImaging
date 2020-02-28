package fr.fresnel.fourPolar.core.image.polarization.fileContainer;

import java.io.File;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for accessing the files corresponding to set of polarization
 * images.
 */
public interface IPolarizationImageFileSet {
    /**
     * Returns the file corresponding to the given polarization.
     * 
     * @param pol
     * @return
     */
    public File getFile(Polarization pol);

    /**
     * Returns the set name designated to this set of polarization images. This set
     * name is exactly the same as the set name assigned by
     * {@link ICapturedImageFileSet}.
     * 
     * @return
     */
    public String getSetName();
}