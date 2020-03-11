package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.fourPolar.IPolarizationsIntensityIterator;
import fr.fresnel.fourPolar.core.image.captured.fileSet.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for accessing a group of polarization images (of the same
 * sample).
 */
public interface IPolarizationImageSet {
    /**
     * Returns the {@link IPolarizationImage} for the given polarization.
     * 
     * @param pol
     * @return
     */
    public IPolarizationImage getPolarizationImage(Polarization pol);

    /**
     * Returns the file set corresponding to this image set.
     * 
     * @return
     */
    public ICapturedImageFileSet getFileSet();

    /**
     * Returns the implementation of {@link IPolarizationsIntensityIterator} for the
     * given image set.
     * 
     * @return
     */
    public IPolarizationsIntensityIterator getIterator();

}