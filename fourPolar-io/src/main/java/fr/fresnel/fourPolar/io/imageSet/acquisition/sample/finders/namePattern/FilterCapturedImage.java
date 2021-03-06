package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
 

/**
 * This filter ensures that only tiff images in the root folder are considered.
 */

class FilterCapturedImage implements FilenameFilter {
    String channelLabel;
    String polLabel;

    public FilterCapturedImage(String polLabel, String channelLabel){
        this.channelLabel = channelLabel;
        this.polLabel = polLabel;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (this.channelLabel != null && !Pattern.compile(this.channelLabel).matcher(name).find())
            return false;

        
        if (this.polLabel != null && !Pattern.compile(this.polLabel).matcher(name).find())
            return false;
        
        return true;
    }
    
}