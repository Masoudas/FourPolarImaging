package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.HashSet;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.IConstellationFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.PolarizationConstellation;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet {
    private ArrayList<HashSet<IConstellationFileSet>> fileSuperSet;

    /**
     * 
     * @param nChannel      : (> 1) Number of distinct wavelengths of the system
     */
    public SampleImageSet(int nChannel) {
        fileSuperSet = new ArrayList<HashSet<IConstellationFileSet>>(nChannel);
        
        for (int channel = 0; channel < nChannel; channel++) {
            fileSuperSet.add(channel, new HashSet<IConstellationFileSet>());
        }
    }

    /**
     * Adds the given image fileSet to the channel if the name extract does not
     * already exist, otherwise returns Exception.
     * 
     * @param fileSet
     * @return
     */
    public void addImage(int channel, IConstellationFileSet fileSet) throws KeyAlreadyExistsException {
        if (fileSuperSet.get(channel - 1).contains(fileSet))
            throw new KeyAlreadyExistsException("The given fileSet already exists for this channel.");

        fileSuperSet.get(channel - 1).add(fileSet);
    }

    /**
     * Removes an image file set from the channel.
     * 
     * @param fileSet
     * @return
     */
    public void removeImage(int channel, IConstellationFileSet fileSet) throws IllegalArgumentException {
        if (!fileSuperSet.get(channel - 1).contains(fileSet))
            throw new IllegalArgumentException("The given file set does not exist.");

        fileSuperSet.get(channel - 1).remove(fileSet);
    }

    /**
     * Returns the images of a channel as a set (a clone of the actuall set).
     * 
     * @param channel
     * @return
     */
    public Set<IConstellationFileSet> getChannelImages(int channel) {
        return (Set<IConstellationFileSet>) fileSuperSet.get(channel - 1).clone();
    }

    // /**
    // * Returns the file set of polarization images corresponding to this
    // constellation set.
    // * @param fileSet
    // * @return
    // */
    // public IPolarizationsFileSet getPolarizationsFileSet(IConstellationFileSet
    // fileSet);

    // /**
    // * Returns the file set of four polar images corresponding to a polarization
    // image set.
    // * @param fileSet
    // * @return
    // */
    // public IFourPolarFileSet getFourPolarFileSet(IConstellationFileSet fileSet);

}