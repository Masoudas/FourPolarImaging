package fr.fresnel.fourPolar.core.image.captured;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * An interface for accessing a set of {@link ICapturedImage} that belong
 * together (i.e, indicate different polarizations of a sample or different
 * wavelengths, for a paricular number of cameras). See also
 * {@link AcquisitionSet}.
 */
public interface ICapturedImageSet {
    /**
     * Returns the captured image associated with this label, where label is as
     * defined by {@link Cameras#getLabels(Cameras)}.
     * 
     */
    public ICapturedImage[] getCapturedImage(String label);

    /**
     * Get the captured image that contains this channel. Note that the captured
     * image may contain other channels.
     * 
     * @param channel is the desired channel.
     * 
     * @throws IllegalArgumentException if channel does not exist.
     * 
     * @return the captured image that contains this channel.
     */
    public ICapturedImage getChannelCapturedImage(int channel);

    /**
     * Returns the file set associated with this captured image.
     */
    public ICapturedImageFileSet fileSet();

    /**
     * Returns true if there exists a multi-channel image in this set.
     * <p>
     * 
     * @apiNote It is the case that the user either provides single channel images,
     *          or one image containing all channels. It's yet to be seen whether
     *          they provide several files each of which may be multi-channel.
     */
    public boolean hasMultiChannelImage();

}