package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import fr.fresnel.fourPolar.core.imageSet.acquisition.IConstellationFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.PolarizationConstellation;

/**
 * Defines the bead image set, which accompanies the sample image set.
 * Each channel must contain a separate bead image set, which is stored
 * as a {@link ConstellationFileSet}. 
 */


public class BeadImageSet {
    private IConstellationFileSet[] imageFileSet = null;

    /**
     * 
     * @param nChannel : (> 1) Number of distinct wavelengths of the system
     */
    public BeadImageSet(int nChannel){
        this.imageFileSet = new IConstellationFileSet[nChannel];
    }

    public void setChannelImage(IConstellationFileSet fileSet, int channelNo) {                
        imageFileSet[channelNo - 1] = fileSet;
    }

    public IConstellationFileSet getChannelImage(int channelNo) {
        return imageFileSet[channelNo];
    }
    
}