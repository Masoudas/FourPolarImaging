package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;
import java.security.KeyException;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

/**
 * Servers as a container for a set of {@linkplain ICapturedImageFileSets} that
 * are supposed to be analyzed together. These captured images are linked
 * together, in the sense that they were taken by the same setup, hence the
 * properties of the imaging setup has remained the same for all of them. We
 * normally require a {@link RegistrationImageSet} for a set of
 * {@link SampleImageSet} of arbitrary number of sample images.
 */
public interface AcquisitionSet {

    /**
     * Adds a captured file set to this set, where every file is checked against
     * {@link ICapturedImageChecker} provided for the class.
     * 
     * @param fileSet is the file set to be added.
     * 
     *
     * @throws KeyAlreadyExistsException in case the file set has already been
     *                                   added.
     */
    public void addCapturedImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException;

    /**
     * Returns a particular image set file using its set name.
     * 
     * @param setName
     * 
     * @throws KeyAlreadyExistsException in case the set name does not exist.
     */
    public ICapturedImageFileSet getCapturedImageSet(String setName) throws KeyException;

    /**
     * Return an iterator that over all file sets inside this set.
     * 
     */
    public Iterator<ICapturedImageFileSet> getIterator();

    /**
     * Removes an image using set name.
     * 
     * @param channel
     * @param setName
     */
    public void removeCapturedImageSet(String setName) throws KeyException;

    /**
     * Returns the number of {@link ICapturedImageFileSet} in this set.
     */
    public int setSize();

    /**
     * Get the root folder of where all the images are located.
     */
    public File rootFolder();

}