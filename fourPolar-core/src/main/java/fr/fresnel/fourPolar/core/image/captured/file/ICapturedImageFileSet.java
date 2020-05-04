package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;


/**
 * An interface for accessing the image files provided in the original constellation format.
 */
public interface ICapturedImageFileSet  {    
    /**
     * Returns the file. The labels are the one given by {@link Cameras};
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

    /**
     * Checks whether the given label is in the file set labels.
     */
    public boolean hasLabel(String label);


    @Override
    public boolean equals(Object obj);


    @Override
    public int hashCode();

    /**
     * Returns the part of metadata that is common to all the files in this fileSet.
     */
    public IMetadata getMetadataIntersecion();
}