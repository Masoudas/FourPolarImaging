package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.io.File;
import java.util.Set;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;


/**
 * An interface for accessing the image files provided in the original constellation format.
 */
public interface ICapturedImageFileSet  {
    /**
     * Returns the labels of the constellation.
     * For 2*2 constellation, the string is Pol0_45_90_135.
     * For 2*1 constellation, the strings are Pol0_90 and Pol45_135.
     * For 1*1 constellation, the strings are Pol0, Pol45, Pol90, Pol135
     * @return
     */ 
    public Set<String> getLabels();
    
    /**
     * Returns the file. The labels are the one given by the labels();
     * @return
     */ 
    public File getFile(String label);

    /**
     * Returns the name extract (a name abbrevation) for the file set.
     * @return
     */
    public String getSetName();

    /**
     * Returns the number of cameras corresponding to this file set.
     * @return
     */
    public Cameras getnCameras();


    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();
	

}