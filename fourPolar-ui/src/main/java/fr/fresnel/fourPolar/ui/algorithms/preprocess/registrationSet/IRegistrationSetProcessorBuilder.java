package fr.fresnel.fourPolar.ui.algorithms.preprocess.registrationSet;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.visualization.figures.polarization.IPolarizationImageSetCompositesCreater;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;

/**
 * An interface for accessing the parameters of the builder.
 */
abstract class IRegistrationSetProcessorBuilder {
    abstract IPolarizationImageSetCompositesCreater getCompositeImageCreator();
    abstract IChannelDarkBackgroundEstimator getDarkBackgroundEstimator();
    abstract ICapturedImageSetReader getRegistrationImageReader();
    abstract IChannelRegistrator getRegistrator();
    abstract ICapturedImageSetSegmenter getSegmenter();
    abstract int getNumChannels();
}
