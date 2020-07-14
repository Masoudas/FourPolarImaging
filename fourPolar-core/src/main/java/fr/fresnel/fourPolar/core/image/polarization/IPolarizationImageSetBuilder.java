package fr.fresnel.fourPolar.core.image.polarization;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

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
    abstract IPolarizationImage getPol0();

    /**
     * @return the pol45
     */
    abstract IPolarizationImage getPol45();

    /**
     * @return the pol90
     */
    abstract IPolarizationImage getPol90();

    /**
     * @return the pol135
     */
    abstract IPolarizationImage getPol135();

    /**
     * @return the numChannels
     */
    abstract int getNumChannels();
}