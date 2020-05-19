package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

public class FourCameraImageSegmenter implements CameraImageSegmenter {
    /**
     * Holds the captured images of the corresponding channels in ascending order.
     */
    private final IFieldOfView _fov;
    private final int _numChannels;
    private final PolarizationSegmenter _singleChannelSegmenter;
    private final PolarizationSegmenter _multiChannelSegmenter;

    private Image<UINT16>[] _pol0;
    private Image<UINT16>[] _pol45;
    private Image<UINT16>[] _pol90;
    private Image<UINT16>[] _pol135;

    private ICapturedImageFileSet _fileSet;

    public FourCameraImageSegmenter(IFieldOfView fov, int numChannels) {
        this._numChannels = numChannels;
        this._fov = fov;
        this._singleChannelSegmenter = new SingleChannelPolarizationSegmenter();
        this._multiChannelSegmenter = new MultiChannelPolarizationSegmenter();
    }

    public void setCapturedImageSet(ICapturedImageSet capturedImageSet) {
        PolarizationSegmenter segmenter = _selectSegmenter(capturedImageSet);

        String[] imageLabels = Cameras.getLabels(Cameras.Four);
        ICapturedImage[] capturedImages_pol0 = capturedImageSet.getCapturedImage(imageLabels[0]);
        ICapturedImage[] capturedImages_pol45 = capturedImageSet.getCapturedImage(imageLabels[1]);
        ICapturedImage[] capturedImages_pol90 = capturedImageSet.getCapturedImage(imageLabels[2]);
        ICapturedImage[] capturedImages_pol135 = capturedImageSet.getCapturedImage(imageLabels[3]);

        IBoxShape pol0FoV = this._fov.getFoV(Polarization.pol0);
        this._pol0 = segmenter.segment(capturedImages_pol0, pol0FoV, this._numChannels);

        IBoxShape pol45FoV = this._fov.getFoV(Polarization.pol45);
        this._pol45 = segmenter.segment(capturedImages_pol45, pol45FoV, this._numChannels);

        IBoxShape pol90FoV = this._fov.getFoV(Polarization.pol90);
        this._pol90 = segmenter.segment(capturedImages_pol90, pol90FoV, this._numChannels);

        IBoxShape pol135FoV = this._fov.getFoV(Polarization.pol135);
        this._pol135 = segmenter.segment(capturedImages_pol135, pol135FoV, this._numChannels);

        this._fileSet = capturedImageSet.fileSet();
    }

    private PolarizationSegmenter _selectSegmenter(ICapturedImageSet capturedImageSet) {
        PolarizationSegmenter segmenter;
        if (capturedImageSet.hasMultiChannelImage()) {
            segmenter = _singleChannelSegmenter;
        } else {
            segmenter = _multiChannelSegmenter;
        }
        return segmenter;
    }

    @Override
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