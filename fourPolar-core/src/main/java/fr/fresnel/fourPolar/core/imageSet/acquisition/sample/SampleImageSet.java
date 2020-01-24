package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageChecker;
import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet extends AcquisitionSet {
    private ArrayList<Hashtable<String, ICapturedImageFileSet>> fileSuperSet;

    public SampleImageSet(FourPolarImagingSetup imagingSetup, ICapturedImageChecker imageChecker) {
        super(imagingSetup, imageChecker);
        fileSuperSet = new ArrayList<Hashtable<String, ICapturedImageFileSet>>(imagingSetup.getnChannel());

        for (int channel = 0; channel < imagingSetup.getnChannel(); channel++) {
            fileSuperSet.add(channel, new Hashtable<String, ICapturedImageFileSet>());
        }
    }

    @Override
    public void removeImage(int channel, String setName) throws KeyException {
        this._checkChannel(channel);
        
        if (!fileSuperSet.get(channel - 1).containsKey(setName))
            throw new KeyException("The given file set does not exist!");
                  
        fileSuperSet.get(channel - 1).remove(setName);
    }

    /**
     * Returns the images of a channel as a set as of {@link ICapturedImageFileSet}.
     * @param channel
     * @return : All images of this channel as an array list.
     * @throws KeyException : In case channel number is zero or greater than number of channels.
     */
    public List<ICapturedImageFileSet> getChannelImages(int channel) throws KeyException {
        this._checkChannel(channel);
        
        return new ArrayList<ICapturedImageFileSet>(fileSuperSet.get(channel - 1).values());
    }

    @Override
    protected void _addImage(int channel, ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        if (fileSuperSet.get(channel - 1).containsKey(fileSet.getSetName()))
            throw new KeyAlreadyExistsException("The given file set already exists for this channel");

        fileSuperSet.get(channel - 1).put(fileSet.getSetName(), fileSet);

    }

    @Override
    public ICapturedImageFileSet getImage(int channel, String setName) throws KeyException {
        this._checkChannel(channel);

        if (!fileSuperSet.get(channel - 1).containsKey(setName))
            throw new KeyAlreadyExistsException("The given file set already exists for this channel");
        return null;
    }
}