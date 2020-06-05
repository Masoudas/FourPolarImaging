package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

class RegistrationSingleChannelPolarizationSegmenter extends ChannelPolarizationSegmenter {
    /**
     * Creates an array of Image interfaces that correspond to the given fov of the
     * desired polarization over the given single channel captured images, that only
     * contains THE FIRST PLANE OF THE CAPTURED IMAGE, as this is the registration
     * rule (@see IChannelRegistrator). To avoid memory consumption, the produced
     * image interfaces are of type {@link SegmentedImage}, which are just wrappers
     * for cursors over the original images.
     * 
     * @param capturedImages are the captured images that contain the desired
     *                       polarization.
     * @param polFoV         is the fov of the desired polarization in the given
     *                       captured image.
     * @param numChannels    is the total number of channels.
     */
    @Override
    public Image<UINT16>[] segment(ICapturedImage[] capturedImages, IBoxShape polFoV) {
        Objects.requireNonNull(capturedImages);
        Objects.requireNonNull(polFoV);

        int numChannels = this._getTotalNumChannels(capturedImages);
        Image<UINT16>[] channelImages = new Image[numChannels];
        for (ICapturedImage iCapturedImage : capturedImages) {
            Image<UINT16> segmentedPolarizationImage = _createPolarizationImage(iCapturedImage, polFoV);

            int channel = iCapturedImage.channels()[0];
            channelImages[channel - 1] = segmentedPolarizationImage;
        }

        return channelImages;
    }

    /**
     * Creates a {@link SegmentedImage} over the polarization fov of this captured
     * image, using an interval cursor over the captured image.
     * 
     */
    private Image<UINT16> _createPolarizationImage(ICapturedImage capturedImage, IBoxShape polFoV) {
        Image<UINT16> image = capturedImage.getImage();

        long[] bottomCorner = _createBottomCorner(polFoV, image.getMetadata());
        long[] len = _createLen(polFoV, image.getMetadata());

        Image<UINT16> segmentedImage = _createSegmentedImageFromInterval(image, bottomCorner, len);

        return segmentedImage;
    }

    /**
     * The bottom corner corresponds to the first pixel of the first plane.
     */
    private long[] _createBottomCorner(IBoxShape polFoV, IMetadata imageMetadata) {
        long[] min_fov = polFoV.min();

        long[] bottomCorner = new long[imageMetadata.getDim().length];
        bottomCorner[0] = min_fov[0] - 1;
        bottomCorner[1] = min_fov[1] - 1;

        return bottomCorner;
    }

    /**
     * As we only seek to get the first plane, the len corresponds to the length of
     * the plane, and all other dimensions would be one.
     */
    private long[] _createLen(IBoxShape polFoV, IMetadata imageMetadata) {
        long[] min_fov = polFoV.min();
        long[] max_fov = polFoV.max();

        long[] len = new long[imageMetadata.getDim().length];
        len[0] = max_fov[0] - min_fov[0] + 1;
        len[1] = max_fov[1] - min_fov[1] + 1;

        if (imageMetadata.axisOrder().z_axis > 0) {
            len[imageMetadata.axisOrder().z_axis] = 1;
        }

        if (imageMetadata.axisOrder().t_axis > 0) {
            len[imageMetadata.axisOrder().t_axis] = 1;
        }

        if (imageMetadata.axisOrder().c_axis > 0) {
            len[imageMetadata.axisOrder().c_axis] = 1;
        }

        return len;
    }
}