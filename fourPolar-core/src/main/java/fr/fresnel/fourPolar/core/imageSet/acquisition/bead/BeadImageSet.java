package fr.fresnel.fourPolar.core.imageSet.acquisition.bead;

import java.io.File;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;

/**
 * Defines the bead image set, which accompanies the sample image set.
 */
public class BeadImageSet implements AcquisitionSet {
    private ICapturedImageFileSet imageFileSet = null;
    private final File _rootFolder;

    /**
     * Create set for the given project. See {@link PathFactoryOfProject}.
     * 
     * @param rootFolder is the root folder of where all the images are located.
     */
    public BeadImageSet(File rootFolder) {
        Objects.requireNonNull(rootFolder);
        this._rootFolder = rootFolder;
    }

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
        return Arrays.stream(new ICapturedImageFileSet[] { imageFileSet }).iterator();
    }

    @Override
    public int setSize() {
        if (this.imageFileSet == null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public File rootFolder() {
        return this._rootFolder;
    }

}