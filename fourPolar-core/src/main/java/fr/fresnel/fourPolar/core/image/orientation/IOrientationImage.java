package fr.fresnel.fourPolar.core.image.orientation;

import fr.fresnel.fourPolar.core.fourPolar.IOrientationVectorIterator;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * An interface for accessing the three {@link IAngleImage} of the same sample
 * for a particular channel.
 */
public interface IOrientationImage {
    /**
     * Returns the {@link IAngleImage}.
     * 
     * @param angle
     * @return
     */
    public IAngleImage getAngleImage(OrientationAngle angle);

    /**
     * Returns the {@link ICapturedImageFileSet} that this orientation image is made
     * from.
     * 
     * @return
     */
    public ICapturedImageFileSet getCapturedSet();

    /**
     * Return the implementation of {@link IOrientationVectorIterator} for the
     * orientation image.
     * 
     * @return
     */
    public IOrientationVectorIterator getOrientationVectorIterator();

    /**
     * Returns an interface for randomly accessing the underlying orientation
     * vectors.
     * 
     * @return
     */
    public IOrientationImageRandomAccess getRandomAccess();

    /**
     * @return channel number of this image.
     */
    public int channel();
}