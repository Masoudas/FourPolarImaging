package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

class SampleMultiChannelPolarizationSegmenter extends ChannelPolarizationSegmenter {
    /**
     * Creates an array of Image interfaces that correspond to the given fov of the
     * desired polarization over the given multi-channel captured images. To avoid
     * memory consumption, the produced image interfaces are of type
     * {@link SegmentedImage}, which are just wrappers for cursors over the original
     * images.
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
            Image<UINT16>[] segmentedPolarizationImages = _createsSegmentedPolarizationImages(iCapturedImage, polFoV);

            for (int image_c = 0; image_c < iCapturedImage.numChannels(); image_c++) {
                int channel = iCapturedImage.channels()[image_c];
                channelImages[channel - 1] = segmentedPolarizationImages[image_c];
            }
        }

        return channelImages;
    }

    /**
     * Creates an array of {@link SegmentedImage} over the polarization fov of this
     * captured image, using an interval cursor over the captured image. Each
     * element of the array corresponds to one channel.
     * 
     */
    private Image<UINT16>[] _createsSegmentedPolarizationImages(ICapturedImage capturedImage, IBoxShape polFoV) {
        Image<UINT16> image = capturedImage.getImage();
        int c_axis = image.getMetadata().axisOrder().c_axis;

        Image<UINT16>[] segmentedPolImageChannels = new Image[capturedImage.numChannels()];
        for (int channel = 0; channel < capturedImage.numChannels(); channel++) {
            long[] bottomCorner = _createBottomCorner(polFoV, image.getMetadata(), channel);
            long[] len = _createLen(polFoV, image.getMetadata(), c_axis);
            segmentedPolImageChannels[channel] = this._createSegmentedImageFromInterval(image, bottomCorner, len);
        }
        return segmentedPolImageChannels;
    }

    private long[] _createBottomCorner(IBoxShape polFoV, IMetadata imageMetadata, int imageChannel) {
        long[] min_fov = polFoV.min();

        long[] bottomCorner = new long[imageMetadata.getDim().length];
        bottomCorner[0] = min_fov[0];
        bottomCorner[1] = min_fov[1];
        bottomCorner[imageMetadata.axisOrder().c_axis] = imageChannel;

        return bottomCorner;
    }

    private long[] _createLen(IBoxShape polFoV, IMetadata imageMetadata, int c_axis) {
        long[] min_fov = polFoV.min();
        long[] max_fov = polFoV.max();

        long[] len = imageMetadata.getDim().clone();
        len[0] = max_fov[0] - min_fov[0] + 1;
        len[1] = max_fov[1] - min_fov[1] + 1;
        len[c_axis] = 1;

        return len;
    }

}