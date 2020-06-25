package fr.fresnel.fourPolar.io.imageSet.acquisition;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters all .txt files in a path.
 */
class TextFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        int index = name.lastIndexOf('.');
        String extension = index > 0 ? name.substring(index + 1) : null;

        if (extension != null && extension.equals("txt")) {
            return true;
        }
        return false;
    }

}