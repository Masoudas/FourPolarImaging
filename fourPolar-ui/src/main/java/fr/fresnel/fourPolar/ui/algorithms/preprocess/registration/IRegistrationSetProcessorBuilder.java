package fr.fresnel.fourPolar.ui.algorithms.preprocess.registration;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.IChannelDarkBackgroundEstimator;
import fr.fresnel.fourPolar.algorithm.preprocess.registration.IChannelRegistrator;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.algorithm.util.image.color.GrayScaleToColorConverter.Color;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;
import fr.fresnel.fourPolar.io.visualization.figures.polarization.IPolarizationImageSetCompositesWriter;

/**
 * An interface for accessing the parameters of the builder.
 */
abstract class IRegistrationSetProcessorBuilder {
    abstract Color getCompositeBaseImageColor();
    abstract Color getCompositeImageToRegisterColor();
    abstract IPolarizationImageSetCompositesWriter getCompositeWriters();
    abstract IChannelDarkBackgroundEstimator getDarkBackgroundEstimator();
    abstract ICapturedImageSetReader getRegistrationImageReader();
    abstract IChannelRegistrator getRegistrator();
    abstract ICapturedImageSetSegmenter getSegmenter();
}
