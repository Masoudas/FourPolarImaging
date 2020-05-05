package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;
import java.security.KeyException;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Servers as a model for image containters like {@link BeadImageSet} and
 * {@link SampleImageSet}
 */
public abstract class AcquisitionSet {
    protected FourPolarImagingSetup _imagingSetup;
    protected ICapturedImageChecker _imageChecker;
    private String[] _imageLabels;

    /**
     * Creates the set of images for the given imaging setup. All images are checker
     * against the imageChecker to ensure they satisfy the imposed checks.
     * 
     * @param imagingSetup is the image setup associated with this acquired image
     *                     set.
     * @param imageChecker is the image checker for the acquired images.
     */
    public AcquisitionSet(FourPolarImagingSetup imagingSetup, ICapturedImageChecker imageChecker) {
        this._imagingSetup = imagingSetup;
        this._imageChecker = imageChecker;
        this._imageLabels = Cameras.getLabels(imagingSetup.getCameras());
    }

    /**
     * Add a captured file set to this set, where every file is checked against
     * {@link ICapturedImageChecker} provided for the class.
     *
     * @throws KeyAlreadyExistsException in case the file set has already been
     *                                   added.
     * @throws IllegalArgumentException  in case the wrong addImage method is used.
     * @throws IncompatibleCapturedImage in case at least one image is incompatible.
     */
    public void addImage(ICapturedImageFileSet fileSet)
            throws KeyAlreadyExistsException, IllegalArgumentException, IncompatibleCapturedImage {
        if (this._imagingSetup.getCameras() != fileSet.getnCameras()) {
            throw new IllegalArgumentException("File set corresponds to different number of cameras.");
        }

        for (String label : this._imageLabels) {
            for (File imagePath : fileSet.getFile(label)) {
                this._imageChecker.checkCompatible(imagePath);
            }
        }
        
        this._addImage(fileSet);

    }

    /**
     * Method to add image to the set for classes that extend this class.
     */
    protected abstract void _addImage(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException;

    /**
     * Returns a particular image set using the channel number and set name.
     * 
     * @param channel
     * @param fileName
     * @return
     * @throws KeyAlreadyExistsException : In case the key does not exist.
     */
    public abstract ICapturedImageFileSet getImage(int channel, String setName) throws KeyException;

    /**
     * Remove an image using channel number and set name.
     * 
     * @param channel
     * @param setName
     */
    public abstract void removeImage(int channel, String setName);

    /**
     * @return the imagingSetup
     */
    public FourPolarImagingSetup getImagingSetup() {
        return _imagingSetup;
    }

    /**
     * Returns the associated image checker.
     * 
     * @return
     */
    public ICapturedImageChecker getCapturedImageChecker() {
        return _imageChecker;
    }
}