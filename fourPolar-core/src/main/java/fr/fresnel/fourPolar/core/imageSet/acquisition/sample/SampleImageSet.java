package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.management.openmbean.KeyAlreadyExistsException;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;

/**
 * Encapsulates the sample image set files as provided by the user.
 */
public class SampleImageSet implements AcquisitionSet {
    private ArrayList<ICapturedImageFileSet> _fileSuperSet;

    public SampleImageSet() {
        _fileSuperSet = new ArrayList<ICapturedImageFileSet>();
    }

    @Override
    public void removeImageSet(String setName) throws KeyException {
        if (!_fileSuperSet.remove(setName)) {
            throw new KeyException("The given set name does not exist.");
        }

    }

    @Override
    public void addImageSet(ICapturedImageFileSet fileSet) throws KeyAlreadyExistsException {
        if (_fileSuperSet.contains(fileSet))
            throw new KeyAlreadyExistsException("The given file set already exists for this channel");

        _fileSuperSet.add(fileSet);

    }

    @Override
    public ICapturedImageFileSet getImageSet(String setName) throws KeyException {
        if (!_fileSuperSet.contains(setName))
            throw new KeyAlreadyExistsException("The given file set does not exist.");
        return _fileSuperSet.get(_fileSuperSet.indexOf(setName));
    }

    @Override
    public Iterator<ICapturedImageFileSet> getIterator() {
        return _fileSuperSet.iterator();
    }
}