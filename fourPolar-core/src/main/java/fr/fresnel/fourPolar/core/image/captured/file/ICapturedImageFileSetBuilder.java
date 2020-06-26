package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;

import fr.fresnel.fourPolar.core.exceptions.imageSet.acquisition.IncompatibleCapturedImage;
import javassist.tools.reflect.CannotCreateException;

/**
 * The interface for building a {@link ICapturedImageFileSet} from a set of
 * files.
 */
public interface ICapturedImageFileSetBuilder {
    /**
     * Add files to the fileSet being built. Returns the current instance to allow
     * method chaining. Note that the channels must exactly correspond to the
     * channel axis of the image. Suppose image has two channels, and they
     * correspond to channel 1 and 2. The channel array then must be [1, 2].
     * <p>
     * Used for the case when only one camera is present.
     * 
     * @param channels       is the channel(s) this file is associated with.
     * @param pol0_45_90_135 is the captured image file that corresponds to all four
     *                       polarizations.
     * 
     * @throws IllegalArgumentException if method is used for incorrect
     *                                  {@link Cameras}
     */
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_45_90_135);

    /**
     * Add files to the fileSet being built. If multiple channels per image, this
     * method should be used one before using the build method. Returns the current
     * instance to allow method chaining. Suppose image has two channels, and they
     * correspond to channel 1 and 2. The channel array then must be [1, 2].
     * <p>
     * Used for the case when two cameras are present.
     * 
     * @param channels  is the channel(s) this file is associated with.
     * @param pol0_90   is the captured image file that corresponds to polarizations
     *                  0 and 90.
     * @param pol45_135 is the captured image file that corresponds to polarizations
     *                  45 and 135.
     * 
     * @throws IllegalArgumentException if method is used for incorrect
     *                                  {@link Cameras}
     */
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0_90, File pol45_135);

    /**
     * Add files to the fileSet being built. If multiple channels per image, this
     * method should be used one before using the build method. Returns the current
     * instance to allow method chaining. Suppose image has two channels, and they
     * correspond to channel 1 and 2. The channel array then must be [1, 2].
     * <p>
     * Used for the case when four cameras are present.
     * 
     * @param channels is the channel(s) this file is associated with.
     * @param pol0     is the captured image file that corresponds to polarization
     *                 0.
     * @param pol45    is the captured image file that corresponds to polarization
     *                 45.
     * @param pol90    is the captured image file that corresponds to polarization
     *                 90.
     * @param pol135   is the captured image file that corresponds to polarization
     *                 135.
     * 
     * @throws IllegalArgumentException if method is used for incorrect
     *                                  {@link Cameras}.
     */
    public ICapturedImageFileSetBuilder add(int[] channels, File pol0, File pol45, File pol90, File pol135);

    /**
     * Returns the {@link ICapturedImageFileSet} that is created based on the added
     * files.
     *
     * @throws CannotCreateException     in case not enough files are given for all
     *                                   channels.
     * @throws IncompatibleCapturedImage in case a captured image file does not
     *                                   satisfy the constraints put forth by
     *                                   {@link ICapturedImageChecker}.
     * 
     */
    public ICapturedImageFileSet build() throws CannotCreateException, IncompatibleCapturedImage;

}