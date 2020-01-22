package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Defines the bead image set, which accompanies the sample image set. Each
 * channel must contain a separate bead image set, which is stored as a
 * {@link CapturedImageFileSet}.
 */
public class BeadImageSet {
    private ICapturedImageFileSet[] imageFileSet = null;
    private ICapturedImageChecker _imageChecker;
    private FourPolarImagingSetup _imagingSetup;

    public BeadImageSet(FourPolarImagingSetup imagingSetup, ICapturedImageChecker imageChecker) {
        this.imageFileSet = new ICapturedImageFileSet[imagingSetup.getnChannel()];
        this._imagingSetup = imagingSetup;
    }

    public void setChannelImage(int channelNo, ICapturedImageFileSet fileSet) throws CorruptCapturedImage {
        for (String label : Cameras.getLabels(this._imagingSetup.getCameras())) {
            this._imageChecker.checkCompatible(fileSet.getFile(label));    
        }
        
        imageFileSet[channelNo - 1] = fileSet;
    }

    public ICapturedImageFileSet getChannelImage(int channelNo) {
        return imageFileSet[channelNo];
    }
    
}