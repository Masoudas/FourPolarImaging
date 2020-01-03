package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Defines the bead image set, which accompanies the sample image set.
 * Each channel must contain a separate bead image set, which is stored
 * as a {@link CapturedImageFileSet}. 
 */
public class BeadImageSet {
    private ICapturedImageFileSet[] imageFileSet = null;

    public BeadImageSet(FourPolarImagingSetup imagingSetup){
        this.imageFileSet = new ICapturedImageFileSet[imagingSetup.getnChannel()];
    }

    public void setChannelImage(int channelNo, ICapturedImageFileSet fileSet) {                
        imageFileSet[channelNo - 1] = fileSet;
    }

    public ICapturedImageFileSet getChannelImage(int channelNo) {
        return imageFileSet[channelNo];
    }
    
}