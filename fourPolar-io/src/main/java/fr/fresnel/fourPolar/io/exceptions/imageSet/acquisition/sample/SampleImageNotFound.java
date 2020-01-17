package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample;

import java.io.File;
import java.io.IOException;

/**
 * Thrown when the desired sample image is not found.
 */
public class SampleImageNotFound extends IOException{
    private static final long serialVersionUID = -1691535755601684781L;

    public SampleImageNotFound(File file) {
        super("Sample image at " + file.getAbsolutePath() + " not found" );
        
    }
    
}