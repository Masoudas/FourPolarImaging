package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import java.security.KeyException;
import java.util.Arrays;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;

/**
 * Defines the bead image set, which accompanies the sample image set. 
 */
public class BeadImageSet implements AcquisitionSet {
    private ICapturedImageFileSet imageFileSet = null;

    @Override
    public void addImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        if (imageFileSet != null) {
            throw new KeyAlreadyExistsException("Bead image set has already been defined.");
        }

        imageFileSet = fileSet;
    }

    @Override
    public ICapturedImageFileSet getImageSet(String setName) throws KeyException {
        if (imageFileSet.getSetName().equals(setName)) {
            return imageFileSet;
        } else {
            throw new KeyException("The given set name does not exist.");
        }
    }

    /**
     * This method only requires the channel number to function.
     */
    @Override
    public void removeImageSet(String setName) throws KeyException {
        if (imageFileSet.getSetName().equals(setName)) {
            imageFileSet = null;
        } else {
            throw new KeyException("The given set name does not exist.");
        }

    }

    @Override
    public Iterator<ICapturedImageFileSet> getIterator() {
        return Arrays.stream(new ICapturedImageFileSet[]{imageFileSet}).iterator();
    }

}