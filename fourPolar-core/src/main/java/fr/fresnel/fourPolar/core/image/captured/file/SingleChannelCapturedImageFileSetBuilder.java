package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.physics.axis.AxisOrder;
import javassist.tools.reflect.CannotCreateException;

/**
 * Using this builder, we can create {@link ICapturedImageFileSet} for the case
 * where there's a separate file for each channel.
 */
public class SingleChannelCapturedImageFileSetBuilder {
    final private Cameras _cameras;
    final private int _numChannels;
    final private IMetadata _metadataIntersection;

    /**
     * List of files added by the user. Each row corresponds to the images of a
     * camera, and the columns correspond to different channels.
     */
    final private File[][] _files;

    /**
     * For keeping count of how many channels have been inserted.
     */
    private int _channelCtr = 0;

    /**
     * @param metadataIntersection is the common part of metadata among all the
     *                             images of this file set. Of special intereset are
     *                             {@link IMetadata#axisOrder()} and
     *                             {@link IMetadata#numChannels()}
     * @param imagingSetup         is the corresponding imaging setup.
     */
    public SingleChannelCapturedImageFileSetBuilder(IMetadata metadataIntersection,
            FourPolarImagingSetup imagingSetup) {
        this._cameras = Objects.requireNonNull(imagingSetup, "imaging setup cannot be null.").getCameras();
        this._metadataIntersection = Objects.requireNonNull(metadataIntersection,
                "metadataIntersection cannot be null.");

        if (AxisOrder.getChannelAxis(metadataIntersection.axisOrder()) > 0) {
            throw new IllegalArgumentException(
                    "Single channel captured file set must not be used for multi-channel images.");
        }
        this._numChannels = imagingSetup.getNumChannel();
        this._files = new File[Cameras.getNImages(this._cameras)][this._numChannels];
    }

    /**
     * Add files to the fileSet being built. Each time this method is used, the
     * given files are interpeted as the new channel. After using the build method,
     * the counter is set to zero for reuse. Returns the current instance to allow
     * method chaining.
     * <p>
     * Used for the case when only one camera is present.
     * 
     * @param channel        is the channel number, starting from one.
     * @param pol0_45_90_135 is the captured image file that corresponds to all four
     *                       polarizations.
     */
    public SingleChannelCapturedImageFileSetBuilder add(int channel, File pol0_45_90_135) {
        _checkChannel(channel);
        if (this._cameras != Cameras.One) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        this._files[0][channel - 1] = Objects.requireNonNull(pol0_45_90_135, "pol0_45_90_135 can't be null");

        this._channelCtr++;
        return this;
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
    public SingleChannelCapturedImageFileSetBuilder add(int channel, File pol0_90, File pol45_135) {
        _checkChannel(channel);
        if (this._cameras != Cameras.Two) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        this._files[0][channel - 1] = Objects.requireNonNull(pol0_90, "pol0_90 can't be null");
        this._files[1][channel - 1] = Objects.requireNonNull(pol45_135, "pol45_135 can't be null");

        this._channelCtr++;

        return this;
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
    public SingleChannelCapturedImageFileSetBuilder add(int channel, File pol0, File pol45, File pol90, File pol135) {
        _checkChannel(channel);
        if (this._cameras != Cameras.Four) {
            throw new IllegalArgumentException("Use add method for " + this._cameras + " cameras");
        }
        this._files[0][channel - 1] = Objects.requireNonNull(pol0, "pol0 can't be null");
        this._files[1][channel - 1] = Objects.requireNonNull(pol45, "pol45 can't be null");
        this._files[2][channel - 1] = Objects.requireNonNull(pol90, "pol90 can't be null");
        this._files[3][channel - 1] = Objects.requireNonNull(pol135, "pol135 can't be null");

        this._channelCtr++;
        return this;
    }

    /**
     * Returns the {@link ICapturedImageFileSet} that is created based on the added
     * files.
     *
     * @throws FileSetBuildError in case the number of added files and number of
     *                           channels don't match.
     */
    public ICapturedImageFileSet build() throws CannotCreateException {
        if (this._channelCtr != this._numChannels) {
            throw new CannotCreateException(
                    "Can't creat fileset because number of added files does not match the number of channels.");
        }

        this._channelCtr = 0; // Reset channel counter for later reuse of the class.

        ICapturedImageFileSet fileSet = null;
        if (this._cameras == Cameras.One) {
            fileSet = new CapturedImageFileSet(this._metadataIntersection, this._files[0].clone());
        } else if (this._cameras == Cameras.Two) {
            fileSet = new CapturedImageFileSet(this._metadataIntersection, this._files[0].clone(),
                    this._files[1].clone());
        } else if (this._cameras == Cameras.Four) {
            fileSet = new CapturedImageFileSet(this._metadataIntersection, this._files[0].clone(),
                    this._files[1].clone(), this._files[2].clone(), this._files[3].clone());
        }

        return fileSet;
    }

    private void _checkChannel(int channel) {
        if (channel <= 0 || channel > _numChannels) {
            throw new IllegalArgumentException(
                    "Channel must be greater than zero and less than total number of channels.");
        }
    }

}