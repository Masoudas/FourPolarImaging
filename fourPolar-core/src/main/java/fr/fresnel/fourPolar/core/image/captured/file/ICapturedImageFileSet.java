package fr.fresnel.fourPolar.core.image.captured.file;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * An interface for accessing the image files provided in the original
 * constellation format.
 * 
 * The assumption is that the captured images of a sample are either all single
 * channel, or there's a multi-channel image (we can't have a combination of
 * multi-channel and single channel for an image). See
 * {@link #isMultiChannel()}.
 * 
 */
public interface ICapturedImageFileSet {
    /**
     * For each polarization label, returns an array of {@link ICapturedImageFile}.
     * For example, in the case of all channels in a single file, this method
     * returns an array of size one. In case of single channel per file, this array
     * would have the same size as the number of channels. The labels are the one
     * given by {@link Cameras};
     * 
     * @return
     */
    public ICapturedImageFile[] getFile(String label);

    /**
     * Returns the set name (a name abbrevation) associated with the given fileSet.
     * The set name is defined using the flag file name + hash of flag file full
     * path with the following rules:
     * <ul>
     * <li>One camera: Flag file is first channel (or all channels in multi-channel
     * case).</li>
     * <li>Two cameras: Flag file is pol0-90 of first channel (or all channels in
     * multi-channel case).</li>
     * <li>Two cameras: Flag file is pol0 of first channel (or all channels in
     * multi-channel case).</li>
     * 
     * </ul>
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

    /**
     * For any object other than ICapturedImageFileSet or String returns false. With
     * this equal method, we check whether the {@link #getSetName()} is equal for
     * the two sets.
     */
    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    /**
     * With this method, we check that all files, together with labels and number of
     * cameras are equal. Compare to {@link #equals(Object)}.
     */
    public boolean deepEquals(ICapturedImageFileSet fileset);

}