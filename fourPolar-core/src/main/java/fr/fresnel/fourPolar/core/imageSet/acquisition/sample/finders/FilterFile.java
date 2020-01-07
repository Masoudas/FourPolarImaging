package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 

/**
 * This filter ensures that only tiff images in the root folder are considered.
 */

public class FilterCapturedImage implements FilenameFilter {
    File file;

    public FilterCapturedImage(File file){
        this.file = file;
    }

    @Override
    public boolean accept(File dir, String name) {
        // Make sure that the files are equal
        if (file.getName() != name || file.g        )
            return false;
        
        return true;

        
    }
    
}