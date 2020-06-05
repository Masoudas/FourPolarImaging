package fr.fresnel.fourPolar.ui.algorithms.preprocess.sample;

import java.io.IOException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.soi.ISoIImage;

/**
 * an interface for processing a set of {@link ICapturedImageFileSet} that are
 * brought together using a {@link SampleImageSet}. The end goal is to generate
 * a {@link IPolarizationImageSet} for the given {@link ICapturedImageFileSet}.
 * For this end, this processor does the following:
 * <ol>
 * <li>Loads each captured image set using the user supported
 * {@link ICapturedImageSetReader}</li>
 * <li>Segments each captured set using a user provided
 * {@link ICapturedImageSetSegmenter} to its contained channels (one or
 * several), and creates a {@link IPolarizationImageSet} for each channel.</li>
 * <li>Realigns each channel using a user provided
 * {@link IChannelRealigner}</li>
 * <li>Removes the dark background for each channel using a user provided
 * {@link IChannelDarkBackgroundRemover}</li>
 * <li>Creates the {@link ISoIImage} for each polarization image set</li>
 * <li>Writes each polarization image set to the disk, using the write interface
 * provided by the user</li>
 * <li>Writes each SoI image to the disk</li>
 * <ol/>
 */
public interface ISampleImageSetPreprocessor {
    /**
     * Set the captured image to be processed.
     * 
     * @param capturedImageFileSet
     * 
     * @throws IOException in case of IO issues while reading this captured image
     *                     set from the disk.
     */
    public void setCapturedImageSet(ICapturedImageFileSet capturedImageFileSet) throws IOException;

    /**
     * Return the polarization image set for the given channel of the captured image
     * set.
     */
    public IPolarizationImageSet getPolarizationImageSet(int channel);

    /**
     * Close all IO resources associated with this processor.
     */
    public void closeResources() throws IOException;

}