package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;
import java.security.KeyException;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.exceptions.image.acquisition.CorruptCapturedImage;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Servers as a model for image containters like {@link BeadImageSet} and
 * {@link SampleImageSet}
 */
public abstract class AcquisitionSet {
    protected FourPolarImagingSetup _imagingSetup;
    protected ICapturedImageChecker _imageChecker;

    public AcquisitionSet(FourPolarImagingSetup imagingSetup, ICapturedImageChecker imageChecker) {
        this._imagingSetup = imagingSetup;
        this._imageChecker = imageChecker;
    }

    /**
     * Add an image to the set in the case of one camera.
     * 
     * @param channel
     * @param pol0_45_90_135
     * @throws KeyAlreadyExistsException : In case the file set has already been
     *                                   added.
     * @throws IllegalArgumentException  : In case the wrong addImage method is
     *                                   used.
     * @throws CorruptCapturedImage      : In case at least one image is corrupt.
     */
    public void addImage(int channel, File pol0_45_90_135)
            throws KeyAlreadyExistsException, IllegalArgumentException, CorruptCapturedImage, KeyException {
        this._checkChannel(channel);

        if (this._imagingSetup.getCameras() != Cameras.One) {
            throw new IllegalArgumentException(
                    "Use addImage method for " + this._imagingSetup.getCameras() + " cameras");
        }

        this._imageChecker.checkCompatible(pol0_45_90_135);
        this._addImage(channel, new CapturedImageFileSet(pol0_45_90_135));
    }

    /**
     * Add two images of the same sample in case of two cameras.
     * 
     * @param channel
     * @param pol0_90
     * @param pol45_135
     * @throws KeyAlreadyExistsException : In case the file set has already been
     *                                   added.
     * @throws IllegalArgumentException  : In case the wrong addImage method is
     *                                   used.
     * @throws CorruptCapturedImage      : In case at least one image is corrupt.
     */
    public void addImage(int channel, File pol0_90, File pol45_135)
            throws KeyAlreadyExistsException, IllegalArgumentException, CorruptCapturedImage, KeyException {
        this._checkChannel(channel);
        
        if (this._imagingSetup.getCameras() != Cameras.Two) {
            throw new IllegalArgumentException(
                    "Use addImage method for " + this._imagingSetup.getCameras() + " cameras");
        }

        this._imageChecker.checkCompatible(pol0_90);
        this._imageChecker.checkCompatible(pol45_135);
        this._addImage(channel, new CapturedImageFileSet(pol0_90, pol45_135));

    }

    /**
     * Add four images of the same sampl in case of four cameras.
     * 
     * @param channel
     * @param pol0
     * @param pol45
     * @param pol90
     * @param pol135
     * @throws KeyAlreadyExistsException : In case the file set has already been
     *                                   added.
     * @throws IllegalArgumentException  : In case the wrong addImage method is
     *                                   used.
     * @throws CorruptCapturedImage      : In case at least one image is corrupt.
     */
    public void addImage(int channel, File pol0, File pol45, File pol90, File pol135)
            throws KeyAlreadyExistsException, IllegalArgumentException, CorruptCapturedImage, KeyException {
        this._checkChannel(channel);

        if (this._imagingSetup.getCameras() != Cameras.Two) {
            throw new IllegalArgumentException(
                    "Use addImage method for " + this._imagingSetup.getCameras() + " cameras");
        }

        this._imageChecker.checkCompatible(pol0);
        this._imageChecker.checkCompatible(pol45);
        this._imageChecker.checkCompatible(pol90);
        this._imageChecker.checkCompatible(pol135);
        this._addImage(channel, new CapturedImageFileSet(pol0, pol45, pol90, pol135));

    }

    /**
     * Method to add image to the set for classes that extend this class.
     */
    protected abstract void _addImage(int channel, ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException;

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
     * @param channel
     * @param setName
     * @throws KeyException : In case the given channel or setName does not exist.
     */
    public abstract void removeImage(int channel, String setName) throws KeyException;

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

    protected void _checkChannel(int channel) throws KeyException {
        if (channel == 0 || channel > this._imagingSetup.getnChannel()) {
            throw new KeyException("Channel " + channel + " does not exist.");
        }        
    }

}