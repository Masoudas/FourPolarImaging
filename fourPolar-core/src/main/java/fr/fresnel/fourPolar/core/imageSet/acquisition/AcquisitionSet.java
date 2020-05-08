package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;
import java.security.KeyException;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.checker.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Servers as a model for image containters like {@link BeadImageSet} and
 * {@link SampleImageSet}
 */
public abstract class AcquisitionSet {
    protected FourPolarImagingSetup _imagingSetup;
    protected ICapturedImageChecker _imageChecker;

    /**
     * Creates the set of images for the given imaging setup. All images are checker
     * against the imageChecker to ensure they satisfy the imposed checks.
     * 
     * @param imagingSetup is the image setup associated with this acquired image
     *                     set.
     * @param imageChecker is the image checker for the acquired images.
     */
    public AcquisitionSet(FourPolarImagingSetup imagingSetup) {
        this._imagingSetup = imagingSetup;
    }

    /**
     * Add a captured file set to this set, where every file is checked against
     * {@link ICapturedImageChecker} provided for the class.
     *
     * @throws KeyAlreadyExistsException in case the file set has already been
     *                                   added.
     */
    public abstract void addImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException;

    /**
     * Returns a particular image set using the channel number and set name.
     * 
     * @param setName
     * 
     * @throws KeyAlreadyExistsException in case the set name does not exist.
     */
    public abstract ICapturedImageFileSet getImageSet(String setName) throws KeyException;

    /**
     * Return an iterator that contains all image sets.
     * 
     */
    public abstract Iterator<ICapturedImageFileSet> getIterator();

    /**
     * Remove an image using channel number and set name.
     * 
     * @param channel
     * @param setName
     */
    public abstract void removeImageSet(String setName) throws KeyException;

}