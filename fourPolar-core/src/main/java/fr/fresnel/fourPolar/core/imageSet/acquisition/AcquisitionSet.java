package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.security.KeyException;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;

/**
 * Servers as a model for image containters like {@link BeadImageSet} and
 * {@link SampleImageSet}
 */
public interface AcquisitionSet {

    /**
     * Creates the set of images for the given imaging setup. All images are checker
     * against the imageChecker to ensure they satisfy the imposed checks.
     * 
     * @param imagingSetup is the image setup associated with this acquired image
     *                     set.

    /**
     * Add a captured file set to this set, where every file is checked against
     * {@link ICapturedImageChecker} provided for the class.
     *
     * @throws KeyAlreadyExistsException in case the file set has already been
     *                                   added.
     */
    public void addImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException;

    /**
     * Returns a particular image set using the channel number and set name.
     * 
     * @param setName
     * 
     * @throws KeyAlreadyExistsException in case the set name does not exist.
     */
    public ICapturedImageFileSet getImageSet(String setName) throws KeyException;

    /**
     * Return an iterator that contains all image sets.
     * 
     */
    public Iterator<ICapturedImageFileSet> getIterator();

    /**
     * Remove an image using channel number and set name.
     * 
     * @param channel
     * @param setName
     */
    public void removeImageSet(String setName) throws KeyException;

}