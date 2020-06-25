package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample;

import java.io.IOException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;

/**
 * Exception thrown in case the {@link AcquisitionSet} does not exist, or it's
 * captured images could not be found at all.
 */
public class AcquisitionSetNotFound extends IOException{
    private static final long serialVersionUID = -3437678261374157670L;
    
    @Override
    public String getMessage() {
        return "Acquisition does not exist on the disk or is empty.";
    }
}