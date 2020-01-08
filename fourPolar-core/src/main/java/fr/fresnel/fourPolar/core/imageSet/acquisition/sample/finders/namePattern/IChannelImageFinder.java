package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;

import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;

/**
 * An interface for finding the channel images with respect to cameras.
 */
interface IChannelImageFinder {
    public void find(File rootFolder, SampleImageSet sampleSet, int channel, String channelLabel, String[] polLabel, IImageChecker imageChecker);
   
}