package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import java.security.KeyException;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Defines the bead image set, which accompanies the sample image set. Each
 * channel must contain a separate bead image set, which is stored as a
 * {@link CapturedImageFileSet}.
 */
public class BeadImageSet extends AcquisitionSet {
    private ICapturedImageFileSet[] imageFileSet = null;

    /**
     *
     * Defines the bead image set, which accompanies the sample image set. Each
     * channel must contain a separate bead image set, which is stored as a
     * {@link CapturedImageFileSet}.
     * 
     * @param imagingSetup
     * @param imageChecker
     */
    public BeadImageSet(FourPolarImagingSetup imagingSetup, ICapturedImageChecker imageChecker) {
        super(imagingSetup, imageChecker);

        this.imageFileSet = new ICapturedImageFileSet[imagingSetup.getNumChannel()];
    }

    @Override
    protected void _addImage(int channel, ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        if (imageFileSet[channel - 1] != null) {
            throw new KeyAlreadyExistsException("A bead image is already defined for channel " + channel);
        }

        imageFileSet[channel - 1] = fileSet;
    }

    @Override
    public ICapturedImageFileSet getImage(int channel, String setName) throws KeyException {
        return imageFileSet[channel];
    }

    /**
     * This method only requires the channel number to function.
     */
    @Override
    public void removeImage(int channel, String setName) {
        imageFileSet[channel - 1] = null;

    }

}