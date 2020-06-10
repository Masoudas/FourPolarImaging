package fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet;

import java.io.IOException;
import java.util.Optional;

import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.preprocess.RegistrationSetProcessResult;
import fr.fresnel.fourPolar.core.visualization.figures.polarization.IPolarizationImageSetComposites;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationSetProcessFailure;

/**
 * An interface for processing the given {@link RegistrationImageSet}, that
 * returns a {@link RegistrationSetProcessResult} as a consequence, which in
 * turn can be used to process the sample set. By processing the registration
 * image set we imply:
 * <ol>
 * <li>Segmenting each captured set using {@link ICapturedImageSetSegmenter} to
 * its corresponding channels (one or several), and creating a
 * {@link IPolarizationImageSet} for each channel.</li>
 * <li>Registering each channel using an {@link IChannelRegistrator}</li>
 * <li>Calculates the dark background for each channel</li>
 * <li>Create composite realigned images (for each channel), which can be used
 * to investigate the subjective quality of registration (@see
 * IPolarizationImageSetCompositesCreater).</li>
 * <ol/>
 */
public interface IRegistrationSetProcessor {
    /**
     * Process the given registration image set.
     * 
     * @param registrationImageSet is the set we wish to process.
     * @return the result of process on this set.
     * 
     * @throws RegistrationSetProcessFailure is thrown in case there's a problem
     *                                       when processing the registration image
     *                                       set. For example, IO issues, or
     *                                       registration failure, etc.
     * 
     */
    public RegistrationSetProcessResult process(RegistrationImageSet registrationImageSet)
            throws RegistrationSetProcessFailure;

    /**
     * Get the generated composite image by the processor for the given channel.
     * This method should be called after the {@link process} method and only if
     * process did not fail, otherwise the optional would be empty.
     * 
     * @param channel is the channel number.
     */
    public Optional<IPolarizationImageSetComposites> getRegistrationComposite(int channel);
}