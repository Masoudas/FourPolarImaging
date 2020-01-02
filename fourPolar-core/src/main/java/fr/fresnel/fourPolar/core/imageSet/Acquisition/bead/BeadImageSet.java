package fr.fresnel.fourPolar.core.imageSet.Acquisition.bead;

import fr.fresnel.fourPolar.core.imageSet.Acquisition.IConstellationFileSet;

/**
 * Defines the bead image set, which accompanies the sample image set.
 * Each channel must contain a separate bead image set, which is stored
 * as a {@link ConstellationFileSet}. 
 */


public class BeadImageSet {
    private IConstellationFileSet[] imageFileSet = null;


    public BeadImageSet(int nChannels){
        this.imageFileSet = new IConstellationFileSet[nChannels];
    }

    public void setChannelBeadImage(IConstellationFileSet fileSet, int channelNo) {        
        imageFileSet[channelNo - 1] = fileSet;
    }

    public IConstellationFileSet getChannelBeadImage(int channelNo) {
        return imageFileSet[channelNo];
    }
    
}