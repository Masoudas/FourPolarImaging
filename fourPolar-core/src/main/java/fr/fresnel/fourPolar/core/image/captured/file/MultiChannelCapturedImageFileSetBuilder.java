package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Using this builder, we can create {@link ICapturedImageFileSet} for images
 * with several channels.
 */
public class MultiChannelCapturedImageFileSetBuilder {
    final private Cameras _cameras;
    final private IMetadata _metadataIntersection;

    /**
     * @param imagingSetup         is the corresponding imaging setup.
     */
    public MultiChannelCapturedImageFileSetBuilder(FourPolarImagingSetup imagingSetup) {
        this._cameras = Objects.requireNonNull(imagingSetup, "imaging setup cannot be null.").getCameras();

    }

    /**
     * Add files to the fileSet being built.
     * <p>
     * Used for the case when only one camera is present.
     * 
     * @param channel        is the channel number, starting from one.
     * @param pol0_45_90_135 is the captured image file that corresponds to all four
     *                       polarizations.
     */
    public ICapturedImageFileSet build(File pol0_45_90_135) {
        if (this._cameras != Cameras.One) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0_45_90_135, "pol0_45_90_135 can't be null");

        return new CapturedImageFileSet(this._metadataIntersection, new File[] { pol0_45_90_135 });
    }

    /**
     * Add files to the fileSet being built. Each time this method is used, the
     * given files are interpeted as the new channel. After using the build method,
     * the counter is set to zero for reuse. Returns the current instance to allow
     * method chaining.
     * <p>
     * Used for the case when two cameras are present.
     * 
     * @param channel   is the channel number.
     * @param pol0_90   is the captured image file that corresponds to polarizations
     *                  0 and 90.
     * @param pol45_135 is the captured image file that corresponds to polarizations
     *                  45 and 135.
     */
    public ICapturedImageFileSet build(File pol0_90, File pol45_135) {
        if (this._cameras != Cameras.Two) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0_90, "pol0_90 can't be null");
        Objects.requireNonNull(pol45_135, "pol45_135 can't be null");

        return new CapturedImageFileSet(this._metadataIntersection, new File[] { pol0_90 }, new File[] { pol45_135 });
    }

    /**
     * Add files to the fileSet being built. Each time this method is used, the
     * given files are interpeted as the new channel. After using the build method,
     * the counter is set to zero for reuse. Returns the current instance to allow
     * method chaining.
     * <p>
     * Used for the case when four cameras are present.
     * 
     * @param channel is the channel number.
     * @param pol0    is the captured image file that has polarization 0.
     * @param pol45   is the captured image file that has polarization 45.
     * @param pol90   is the captured image file that has polarization 90.
     * @param pol135  is the captured image file that has polarization 135.
     */
    public ICapturedImageFileSet build(File pol0, File pol45, File pol90, File pol135) {
        if (this._cameras != Cameras.Four) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        Objects.requireNonNull(pol0, "pol0 can't be null");
        Objects.requireNonNull(pol45, "pol45 can't be null");
        Objects.requireNonNull(pol90, "pol90 can't be null");
        Objects.requireNonNull(pol135, "pol135 can't be null");

        return new CapturedImageFileSet(this._metadataIntersection, new File[] { pol0 }, new File[] { pol45 },
                new File[] { pol90 }, new File[] { pol135 });
    }

}