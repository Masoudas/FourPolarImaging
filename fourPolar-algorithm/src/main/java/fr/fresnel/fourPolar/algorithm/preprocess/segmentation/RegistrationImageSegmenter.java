package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.exceptions.image.polarization.CannotFormPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImage;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageSetBuilder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * An interface for segmenting a registration captured image. To segment a
 * registration image, the FIRST PLANE of polarizations are extracted from that
 * image. Finally, all extracted polarizations are resized with respect to
 * {@link IFieldOfView#getMaximumSize()}, so that they all have the same size.
 * For this end, it might be necessary to add some black pixels to the image,
 * which is accomplished using {@link AxisReassigner#reassignXYCZTAndResize()}
 */
public class RegistrationImageSegmenter implements ICapturedImageSetSegmenter {
    private final int _numChannels;
    private final IFieldOfView _fov;

    private final RegistrationImagePolarizationViewCreator _polViewCreator;
    private final PolarizationImageSetBuilder _polSetBuilder;

    public RegistrationImageSegmenter(IFieldOfView fov, int numChannels) {
        Objects.requireNonNull(fov, "fov can't be null");
        ChannelUtils.checkNumChannelsNonZero(numChannels);

        _numChannels = numChannels;
        _fov = fov;

        _polViewCreator = new RegistrationImagePolarizationViewCreator(_fov);
        _polSetBuilder = new PolarizationImageSetBuilder(_numChannels);
    }

    @Override
    public IPolarizationImageSet segment(ICapturedImageSet capturedImageSet, int channel) {
        ChannelUtils.checkChannel(channel, _numChannels);
        HashMap<Polarization, Image<UINT16>> polImages = _createPolarizationImages(capturedImageSet, channel);
        return _createPolarizationImageSet(capturedImageSet, channel, polImages);
    }

    /**
     * Creates the polarization image set from the polarization image.
     */
    private IPolarizationImageSet _createPolarizationImageSet(ICapturedImageSet capturedImageSet, int channel,
            HashMap<Polarization, Image<UINT16>> polImages) {
        this._polSetBuilder.channel(channel);
        this._polSetBuilder.fileSet(capturedImageSet.fileSet());
        this._polSetBuilder.pol0(polImages.get(Polarization.pol0));
        this._polSetBuilder.pol45(polImages.get(Polarization.pol45));
        this._polSetBuilder.pol90(polImages.get(Polarization.pol90));
        this._polSetBuilder.pol135(polImages.get(Polarization.pol135));

        IPolarizationImageSet polImageSet = null;
        try {
            polImageSet = this._polSetBuilder.build();
        } catch (CannotFormPolarizationImageSet e) {
            // This exception is not caught, because images have same size.
            e.printStackTrace();
        }

        return polImageSet;
    }

    /**
     * Creates the polarization images from polarization images.
     */
    private HashMap<Polarization, Image<UINT16>> _createPolarizationImages(ICapturedImageSet capturedImageSet,
            int channel) {
        Map<Polarization, PolarizationView> polViews = _createPolarizationViews(capturedImageSet, channel);

        HashMap<Polarization, Image<UINT16>> polImages = new HashMap<>();
        for (Polarization polarization : Polarization.values()) {
            Image<UINT16> polImage = this._resizeAndCreatePolImage(polViews.get(polarization));
            polImages.put(polarization, polImage);
        }

        return polImages;
    }

    /**
     * Create the polarization view for the given channel of captured image set.
     */
    private Map<Polarization, PolarizationView> _createPolarizationViews(ICapturedImageSet capturedImageSet,
            int channel) {
        return this._polViewCreator.create(capturedImageSet, channel);
    }

    private Image<UINT16> _resizeAndCreatePolImage(PolarizationView polView) {
        long[] resizeDimension = this._createPolarizationImageResizeDimension(polView.getMetadata());
        return AxisReassigner.reassignToXYCZTAndResize(polView, UINT16.zero(), resizeDimension);
    }

    /**
     * Create the resize dimension for the polarization image. The x and y would be
     * the length derived from {@link IFieldOfView#getMaximumSize()}, channel length
     * would be one, and z and t length would be equal to polarization view size.
     * 
     * @return an [x, y, c, z, t] vector that can be used for resizing polarzation
     *         view.
     */
    private long[] _createPolarizationImageResizeDimension(IMetadata polViewMetadata) {
        long[] resizeDimension = new long[5];

        resizeDimension[0] = _fov.getMaximumLength()[0];
        resizeDimension[1] = _fov.getMaximumLength()[1];

        resizeDimension[IPolarizationImage.AXIS_ORDER.c_axis] = 1;
        resizeDimension[IPolarizationImage.AXIS_ORDER.z_axis] = 1;
        resizeDimension[IPolarizationImage.AXIS_ORDER.t_axis] = 1;

        return resizeDimension;
    }

}