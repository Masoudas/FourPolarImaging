package fr.fresnel.fourPolar.io.image.orientation.file;

import java.io.File;

import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;

/**
 * An interface for accessing the files that correspond to a given
 * {@link IOrientationImage}.
 */
public interface IOrientationImageFileSet {
    /**
     * Return the disk file that corresponds to this angle image.
     * 
     * @param angle
     * @return
     */
    public File getFile(OrientationAngle angle);

    /**
     * Returns the set name designated to this set of angle images that form the
     * orientation image. This set name is exactly the same as the set name assigned
     * by {@link ICapturedImageFileSet}.
     * 
     * @return
     */
    public String getSetName();

}