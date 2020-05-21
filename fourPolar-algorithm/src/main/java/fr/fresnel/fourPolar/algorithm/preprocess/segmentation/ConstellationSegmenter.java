package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;

/**
 * An interface for segmenting the captured images that comply to a particular
 * constellation (i.e, four polarizations in one image, two polarizations in two
 * images, one polarization in four images).
 */
abstract class ConstellationSegmenter {
    protected IFieldOfView _fov;
    protected int _numChannels;

    protected Image<UINT16>[] _pol0;
    protected Image<UINT16>[] _pol45;
    protected Image<UINT16>[] _pol90;
    protected Image<UINT16>[] _pol135;

    /**
     * The file set that corresponds to the segmented captured images.
     */
    protected ICapturedImageFileSet _fileSet;

    public ConstellationSegmenter(IFieldOfView fov, int numChannels) {
        this._numChannels = numChannels;
        this._fov = fov;
    }

    /**
     * Define the captured image set to be segmented.
     */
    public abstract void setCapturedImageSet(ICapturedImageSet capturedImageSet);

    protected ChannelPolarizationSegmenter _selectSegmenter(ICapturedImageSet capturedImageSet) {
        if (capturedImageSet.hasMultiChannelImage()) {
            return new MultiChannelPolarizationSegmenter();
        } else {
            return new SingleChannelPolarizationSegmenter();
        }
    }

    /**
     * Returns the polarization image set of the given channel.
     * 
     * @param channel
     * @return
     */
    public IPolarizationImageSet segment(int channel) {
        ChannelUtils.checkChannel(channel, this._numChannels);

        Image<UINT16> pol0 = AxisReassigner.reassignToXYCZT(this._pol0[channel - 1], UINT16.zero());
        Image<UINT16> pol45 = AxisReassigner.reassignToXYCZT(this._pol45[channel - 1], UINT16.zero());
        Image<UINT16> pol90 = AxisReassigner.reassignToXYCZT(this._pol90[channel - 1], UINT16.zero());
        Image<UINT16> pol135 = AxisReassigner.reassignToXYCZT(this._pol135[channel - 1], UINT16.zero());

        IPolarizationImageSet polImageSet = null;
        try {
            polImageSet = new PolarizationImageSetBuilder(this._numChannels).pol0(pol0).pol45(pol45).pol90(pol90)
                    .pol135(pol135).channel(channel).fileSet(this._fileSet).build();
        } catch (CannotFormPolarizationImageSet e) {
            // Since all images have the same dimension, exception is never caught.
        }

        return polImageSet;
    }

}