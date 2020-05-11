package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.security.KeyException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet implements AcquisitionSet {
    private Hashtable<String, ICapturedImageFileSet> _fileSuperSet;

    public SampleImageSet() {
        _fileSuperSet = new Hashtable<>();
    }

    @Override
    public void removeImageSet(String setName) throws KeyException {
        if (!_fileSuperSet.containsKey(setName)) {
            throw new KeyException("The given set name does not exist.");
        } else {
            _fileSuperSet.remove(setName);
        }

    }

    @Override
    public void addImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        if (_fileSuperSet.values().stream().anyMatch(t -> t.deepEquals(fileSet))) {
            throw new KeyAlreadyExistsException("The given file set already exists for this channel");
        }

        _fileSuperSet.put(fileSet.getSetName(), fileSet);
    }

    @Override
    public ICapturedImageFileSet getImageSet(String setName) throws KeyException {
        if (!_fileSuperSet.containsKey(setName))
            throw new KeyAlreadyExistsException("The given file set does not exist.");

        return _fileSuperSet.get(setName);
    }

    @Override
    public Iterator<ICapturedImageFileSet> getIterator() {
        return _fileSuperSet.values().iterator();
    }

    @Override
    public int setSize() {
        return _fileSuperSet.size();
    }
}