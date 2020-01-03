package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet {
    private ArrayList<HashSet<ICapturedImageFileSet>> fileSuperSet;

    public SampleImageSet(FourPolarImagingSetup imagingSetup) {
        fileSuperSet = new ArrayList<HashSet<ICapturedImageFileSet>>(imagingSetup.getnChannel());

        for (int channel = 0; channel < imagingSetup.getnChannel(); channel++) {
            fileSuperSet.add(channel, new HashSet<ICapturedImageFileSet>());
        }
    }

    /**
     * Adds the given image fileSet to the channel if the name extract does not
     * already exist, otherwise returns Exception.
     * 
     * @param fileSet
     * @return
     */
    public void addImage(int channel, ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
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
    public void removeImage(int channel, ICapturedImageFileSet fileSet) throws IllegalArgumentException {
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
    public Set<ICapturedImageFileSet> getChannelImages(int channel) {
        return (Set<ICapturedImageFileSet>) fileSuperSet.get(channel - 1).clone();
    }

}