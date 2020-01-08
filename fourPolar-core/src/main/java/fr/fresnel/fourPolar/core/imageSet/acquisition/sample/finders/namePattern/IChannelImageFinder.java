package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;

/**
 * An interface for finding the channel images with respect to cameras.
 */
interface IChannelImageFinder {
    public void find(SampleImageSetByNamePatternFinder sampleSetFinder, SampleImageSet sampleImageSet, int channel, String channelLabel);
   
}