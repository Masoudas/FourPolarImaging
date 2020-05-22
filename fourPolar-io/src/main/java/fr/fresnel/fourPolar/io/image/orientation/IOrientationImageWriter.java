package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;

/**
 * And interface for writing an orientation image to the disk.
 */
public interface IOrientationImageWriter {
    /**
     * Write the orientation image to the {@link IOrientationImageFileSet} paths.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param image         is the orientation image.
     * @throws IOException thrown in case of low-level problems.
     */
    public void write(File root4PProject, IOrientationImage image) throws IOException;

    /**
     * Write the orientation image to the {@link IOrientationImageFileSet}, but
     * before doing so, converts the angles to degrees. The stored
     * {@link IAngleImage} on the disk would have an additional "inDegrees" to
     * indicate this transformation.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param image         is the orientation image.
     * @throws IOException thrown in case of low-level problems.
     */
    public void writeInDegrees(File root4PProject, IOrientationImage image) throws IOException;

    /**
     * Close all resources associated with this writer.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}