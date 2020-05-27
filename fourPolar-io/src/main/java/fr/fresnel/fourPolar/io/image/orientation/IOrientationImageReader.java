package fr.fresnel.fourPolar.io.image.orientation;

import java.io.File;
import java.io.IOException;

import fr.fresnel.fourPolar.core.exceptions.image.orientation.CannotFormOrientationImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.orientation.IOrientationImage;

/**
 * And interface for reading an orientation image from the disk.
 */
public interface IOrientationImageReader {
    /**
     * Read the orientation and return the corresponding {@link IOrientationImage}.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param fileSet       is the captured file set corresponding to this
     *                      orientation image.
     * @param channel       is the channel number.
     * 
     * @throws IOException                in case of IO issues.
     * @throws CannotFormOrientationImage if orientation image cannot be created
     *                                    (for example, if images are not of the
     *                                    same size).
     */
    public IOrientationImage read(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage;

    /**
     * Read the orientation image that is in degrees (@see
     * IOrientationImageWriter#writeInDegrees) and return the corresponding
     * {@link IOrientationImage}, which would be in radian.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @param fileSet       is the captured file set corresponding to this
     *                      orientation image.
     * @param channel       is the channel number.
     * 
     * @throws IOException                in case of IO issues.
     * @throws CannotFormOrientationImage if orientation image cannot be created
     *                                    (for example, if images are not of the
     *                                    same size).
     */
    public IOrientationImage readFromDegrees(File root4PProject, ICapturedImageFileSet fileSet, int channel)
            throws IOException, CannotFormOrientationImage;

    /**
     * Close all resources associated with this reader.
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}