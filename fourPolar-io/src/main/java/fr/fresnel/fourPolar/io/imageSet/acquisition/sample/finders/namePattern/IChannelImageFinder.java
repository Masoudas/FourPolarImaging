package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.Iterator;

/**
 * An interface for finding the channel images with respect to cameras. 
 * The resulting iterator would be empty if no files are found.
 */
interface IChannelImageFinder {
    public Iterator<File[]> find(SampleImageSetByNamePatternFinder sampleSetFinder,
            String channelLabel);

}