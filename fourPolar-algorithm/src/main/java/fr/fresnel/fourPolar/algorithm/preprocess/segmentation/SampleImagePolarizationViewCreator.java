package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * Creates polarization view for sample images for the given channel. Note that
 * by a view we simply a cursor that is wrapped in an image. Hence the process
 * of creating a view does not add any memory overhead.
 * <p>
 * Each polarization view corresponds to the x and y dimension specified using
 * {@link IFieldOfView}. The z and t axis if exist remain in tact, and of course
 * the view only contains the demanded channel. Note also that views are created
 * regardless of whether or not image is single channel or multi-channel.
 */
class SampleImagePolarizationViewCreator {
    private final IFieldOfView _fov;

    public SampleImagePolarizationViewCreator(IFieldOfView fov) {
        this._fov = fov;
    }

    /**
     * Create the views for the given channel of the captured image.
     * 
     * @param capturedImageSet is the captured image set.
     * @param fov              is the field of view of polarizations.
     * @param channel          is the desired channel.
     * @return a map between a polarization and its {@link PolarizationView}.
     */
    public Map<Polarization, PolarizationView> create(ICapturedImageSet capturedImageSet, int channel) {
        HashMap<Polarization, PolarizationView> channelPolViews = new HashMap<>();
        for (Polarization pol : Polarization.values()) {
            ICapturedImage channelPolImage = capturedImageSet.getChannelPolarizationImage(channel, pol);

            PolarizationView channelPolView = this._createPolarizationView(channelPolImage, channel, pol);
            channelPolViews.put(pol, channelPolView);
        }

        return channelPolViews;
    }

    /**
     * Create an image interface (more precisely a {@link PolarizationView}) of this
     * polarization and this channel from the captured image that contains it.
     */
    private PolarizationView _createPolarizationView(ICapturedImage capturedImage, int channel, Polarization pol) {
        Image<UINT16> image = capturedImage.getImage();

        int channelAxis = this._convertChannelToAxisNumber(capturedImage, channel);
        long[] viewBottomCorner = this._createViewBottomCorner(image.getMetadata(), _fov.getFoV(pol), channelAxis);
        long[] viewLength = this._createViewLength(image.getMetadata(), _fov.getFoV(pol), channelAxis);

        IPixelCursor<UINT16> channelPolCursor = image.getCursor(viewBottomCorner, viewLength);
        IMetadata viewImageMetadata = this._createChannelPolViewMetadata(image.getMetadata(), viewLength);

        return new PolarizationView(channelPolCursor, image.getFactory(), viewImageMetadata);
    }

    /**
     * If captured image has no channel axis, returns -1. Otherwise, returns the
     * channel number on this captured image channel axis that corresponds to this
     * channel number (if any). For example if channel number is 7, and image has
     * two channels [6, 7], the channel number would be 1 (0 for 6).
     * 
     * @return
     */
    private int _convertChannelToAxisNumber(ICapturedImage capturedImage, int channel) {
        if (capturedImage.getImage().getMetadata().axisOrder().c_axis < 0) {
            return -1;
        }

        if (capturedImage.channels().length == 1) {
            return 0;
        }

        // Find the index of channel from capturedImage.channels(), which in turn
        // corresponds to channel axis number.
        return IntStream.range(0, capturedImage.channels().length)
                .filter((axisNumber) -> capturedImage.channels()[axisNumber] == channel).findAny().getAsInt();

    }

    /**
     * The bottom corner of this view is created from the minimum point located by
     * fov. If the channel axis is also not -1, it will be set as well.
     * 
     * @return
     */
    private long[] _createViewBottomCorner(IMetadata capturedImageMetadata, IBoxShape polFoV, int c_axis) {
        long[] bottomCorner = new long[capturedImageMetadata.getDim().length];

        if (capturedImageMetadata.axisOrder().z_axis > 0) {
            bottomCorner[capturedImageMetadata.axisOrder().z_axis] = 0;
        }

        if (capturedImageMetadata.axisOrder().t_axis > 0) {
            bottomCorner[capturedImageMetadata.axisOrder().t_axis] = 0;
        }

        if (c_axis != -1) {
            bottomCorner[capturedImageMetadata.axisOrder().c_axis] = c_axis;
        }

        long[] min_fov = polFoV.min();
        // Setting the x and y axis to the point indicated by the fov.
        bottomCorner[0] = min_fov[0];
        bottomCorner[1] = min_fov[1];

        return bottomCorner;
    }

    /**
     * Create the length of this polarization view in each dimension. The x and y
     * length would be the difference between min and max point of fov box.
     * Moreover, if there's a channel axis, we set its length to one, so that only a
     * one channel view is created. Note that all the length of z and t axis would
     * be the same as image dimension.
     * 
     * @param capturedImageMetadata is the metadata of the captured image that
     *                              contains this channel and pol.
     * @param polFoV                is the fov of this pol.
     * @param c_axis                is the channel axis on the image, derived using
     *                              {@link #_convertChannelToImageChannelNumber(ICapturedImage, int)}.
     */
    private long[] _createViewLength(IMetadata capturedImageMetadata, IBoxShape polFoV, int c_axis) {
        // Set all lengths to image dimension first.
        long[] len = capturedImageMetadata.getDim().clone();

        // Set x and y to the length of the fov box.
        len[0] = IBoxShape.len(polFoV, 0);
        len[1] = IBoxShape.len(polFoV, 1);

        if (c_axis != -1) {
            len[capturedImageMetadata.axisOrder().c_axis] = 1;
        }

        return len;
    }

    /**
     * Create a metadata for the channel polarization view. In essence, the
     * dimension of this image equals the length of the cursor on each dimension,
     * and it's axis order would be the same as the original captured image.
     */
    private IMetadata _createChannelPolViewMetadata(IMetadata capturedImageMetadata, long[] viewLength) {
        return new Metadata.MetadataBuilder(viewLength).axisOrder(capturedImageMetadata.axisOrder())
                .bitPerPixel(capturedImageMetadata.bitPerPixel()).build();
    }

}