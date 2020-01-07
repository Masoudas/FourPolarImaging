package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet {
    private ArrayList<Hashtable<String, ICapturedImageFileSet>> fileSuperSet;
    private FourPolarImagingSetup imagingSetup;

    public SampleImageSet(FourPolarImagingSetup imagingSetup) {
        fileSuperSet = new ArrayList<Hashtable<String, ICapturedImageFileSet>>(imagingSetup.getnChannel());

        for (int channel = 0; channel < imagingSetup.getnChannel(); channel++) {
            fileSuperSet.add(channel, new Hashtable<String, ICapturedImageFileSet>());
        }

        this.imagingSetup = imagingSetup;
    }

    /**
     * Adds the given image fileSet to the channel if the name extract does not
     * already exist, otherwise returns Exception.
     * 
     * @param fileSet
     * @return
     */
    public void addImage(int channel, ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException, IllegalArgumentException {
        if (fileSet.getnCameras() != this.imagingSetup.getCameras())
            throw new IllegalArgumentException("The captured file set corresponds to different number of cameras");

        if (fileSuperSet.get(channel - 1).containsKey(fileSet.getSetName()))
            throw new KeyAlreadyExistsException("The given file set already exists for this channel");
        
        fileSuperSet.get(channel - 1).put(fileSet.getSetName(), fileSet);
    }

    /**
     * Removes an image file set from the channel.
     * 
     * @param fileSet
     * @return
     */
    public void removeImage(int channel, String setName) throws IllegalArgumentException {
        if (!fileSuperSet.get(channel - 1).containsKey(setName))
            throw new IllegalArgumentException("The given file set does not exist!");

        fileSuperSet.get(channel - 1).remove(setName);
    }

    /**
     * Returns the images of a channel as a set as of {@link ICapturedImageFileSet}.
     * 
     * @param channel
     * @return
     */
    public Set<ICapturedImageFileSet> getChannelImages(int channel) {
        HashSet<ICapturedImageFileSet> channelSet = new HashSet<ICapturedImageFileSet>();
        for (String setName : fileSuperSet.get(channel - 1).keySet()){
            channelSet.add(fileSuperSet.get(channel - 1).get(setName));
        }

        return channelSet;
    }
}