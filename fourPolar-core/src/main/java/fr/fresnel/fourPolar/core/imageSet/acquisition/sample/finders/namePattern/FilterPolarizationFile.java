package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import java.io.File;
import java.io.FilenameFilter;
 

/**
 * This filter ensures that only tiff images in the root folder are considered.
 */

class FilterPolarizationFile implements FilenameFilter {
    String newFileName;
    File originalFile;

    /**
     * Replaces the existing polarization label with the replacement label to filter polarization file.
     * The new file is searched in the exact same parent as the originalFile.
     * @param file : {@link File}
     * @param polLabelTarget : {@link String}
     * @param polLabelReplacement : {@link String}
     */
    public FilterPolarizationFile(File originalFile, String polLabelTarget, String polLabelReplacement){
        String[] originalName = originalFile.getName().split("[.]");
        
        // Create the new name by replacing the polarization label and appending the extension.
        this.newFileName = originalName[0].replace(polLabelTarget, polLabelReplacement) + "." + originalName[1];
        
        this.originalFile = originalFile;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (!dir.equals(this.originalFile.getParentFile()) || !name.equals(this.newFileName))
            return false;
        
        return true;
    }
}