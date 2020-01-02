package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import fr.fresnel.fourPolar.core.imageSet.acquisition.IConstellationFileSet;

/**
 * The interface for accessing the image files of a particular channel of sample
 * set.
 */
public interface IChannelFileSet {
    /**
     * Removes a file set from the channel file set.
     * @param nameExtract : Name extract of the {@link ConstellationFileSet}.
     * @return True if fileSet has been removed.
     */
    public Boolean removeFileSet(String nameExtract);

    // /**
    //  * Returns the file set of polarization images corresponding to this constellation set.
    //  * @param fileSet
    //  * @return
    //  */
    // public IPolarizationsFileSet getPolarizationsFileSet(IConstellationFileSet fileSet); 
    
    // /**
    //  * Returns the file set of four polar images corresponding to a polarization image set.
    //  * @param fileSet
    //  * @return
    //  */
    // public IFourPolarFileSet getFourPolarFileSet(IConstellationFileSet fileSet);
    
    
    public ?? getIterator() {
        
    }
}