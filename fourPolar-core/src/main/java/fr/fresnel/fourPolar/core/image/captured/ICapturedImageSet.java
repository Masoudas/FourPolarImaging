package fr.fresnel.fourPolar.core.image.captured;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * An interface for accessing a set of {@link ICapturedImage} that belong to
 * together (i.e, indicate different polarizations of a sample or different
 * wavelengths.)
 */
public interface ICapturedImageSet {
    /**
     * Returns the captured image associated with this label, where label is as
     * defined by {@link Cameras#getLabels(Cameras)}.
     * 
     */
    public ICapturedImage[] getCapturedImage(String label);

    /**
     * Returns the file set associated with this captured image.
     */
    public ICapturedImageFileSet fileSet();

    /**
     * Returns true if there exists a multi-channel image in this set.
     */
    public boolean hasMultiChannelImage();
}