package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * An interface for accessing the image files provided in the original
 * constellation format.
 */
public interface ICapturedImageFileSet {
    /**
     * For each polarization label, returns an array of files that correspond to the
     * given label. If there's a file present for each channel, this array would
     * correspond to the number of channels. Otherwise, if multiple channels are
     * present in a single image, the size of the array is one. The labels are the
     * one given by {@link Cameras};
     * 
     * @return
     */
    public File[] getFile(String label);

    /**
     * Returns the set name (a name abbrevation) associated with the given fileSet.
     * 
     * @return
     */
    public String getSetName();

    /**
     * Returns the number of cameras corresponding to this file set.
     * 
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
     * Of special intereset are {@link IMetadata#axisOrder()} and
     * {@link IMetadata#numChannels()}. Using this denominator of metadata we can
     * determine, for example, if the underlying fileset is that of a multi-channel
     * image.
     */
    public IMetadata getMetadataIntersecion();
}