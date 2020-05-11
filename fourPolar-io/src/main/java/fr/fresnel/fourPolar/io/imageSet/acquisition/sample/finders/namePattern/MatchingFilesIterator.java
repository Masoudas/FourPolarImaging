package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * An iterator implementation for iterating over files retrieved that match the
 * pattern.
 */
public class MatchingFilesIterator {
    private ArrayList<File[]> _rowFiles = new ArrayList<>();

    /**
     * Adds a row (equivalent to a set of files) to the iterator.
     * 
     * @param files
     */
    public void add(File[] files) {
        _rowFiles.add(files);
    }

    /**
     * Return the iterator corresponding to the rows.
     * 
     * @return
     */
    public Iterator<File[]> iterator() {
        return _rowFiles.iterator();
    }

}