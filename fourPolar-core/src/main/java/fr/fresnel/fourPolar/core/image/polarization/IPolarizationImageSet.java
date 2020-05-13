package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.fourPolar.IIntensityVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for accessing the four {@link IPolarizationImage} of the same
 * sample for a particular channel.
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
     * Returns the implementation of {@link IIntensityVectorIterator} for the given
     * image set.
     * 
     * @return
     */
    public IIntensityVectorIterator getIterator();

    /**
     * Returns the channel number.
     */
    public int channel();

}