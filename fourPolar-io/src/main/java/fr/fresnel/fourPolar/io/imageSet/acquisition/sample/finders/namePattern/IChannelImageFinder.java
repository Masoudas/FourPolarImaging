package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.util.List;

import fr.fresnel.fourPolar.core.imageSet.acquisition.RejectedCapturedImage;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern.NoImageFoundOnRoot;

/**
 * An interface for finding the channel images with respect to cameras.
 */
interface IChannelImageFinder {
    public List<RejectedCapturedImage> find(SampleImageSetByNamePatternFinder sampleSetFinder,
            SampleImageSet sampleImageSet, int channel, String channelLabel) throws NoImageFoundOnRoot;

}