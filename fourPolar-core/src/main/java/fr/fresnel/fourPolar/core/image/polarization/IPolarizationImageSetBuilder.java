package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Builds an {@link IPolarizationImageSet} for the given channel of sample.
 */
abstract class IPolarizationImageSetBuilder{ 
    
    abstract int getChannel();

    /**
     * @return the fileSet
     */
    abstract ICapturedImageFileSet getFileSet();

    /**
     * @return the pol0
     */
    abstract Image<UINT16> getPol0();

    /**
     * @return the pol45
     */
    abstract Image<UINT16> getPol45();

    /**
     * @return the pol90
     */
    abstract Image<UINT16> getPol90();

    /**
     * @return the pol135
     */
    abstract Image<UINT16> getPol135();

    /**
     * @return the numChannels
     */
    abstract int getNumChannels();
}