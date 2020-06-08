package fr.fresnel.fourPolar.ui.algorithms.preprocess.sampleSet;

import fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.IChannelDarkBackgroundRemover;
import fr.fresnel.fourPolar.algorithm.preprocess.realignment.IChannelRealigner;
import fr.fresnel.fourPolar.algorithm.preprocess.segmentation.ICapturedImageSetSegmenter;
import fr.fresnel.fourPolar.io.image.captured.ICapturedImageSetReader;

/**
 * An interface for accessing the builder parameters.
 */
abstract class ISampleImageSetPreprocessorBuilder {

    abstract IChannelDarkBackgroundRemover getBackgroundRemovers(int channel);

    abstract ICapturedImageSetReader getCapturedImageSetReader();

    abstract int getNumChannels();

    abstract IChannelRealigner getRealigners(int channel);

    abstract ICapturedImageSetSegmenter getSegmenter();

}