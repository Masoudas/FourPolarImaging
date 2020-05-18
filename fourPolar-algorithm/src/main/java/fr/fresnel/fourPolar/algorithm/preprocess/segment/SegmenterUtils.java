package fr.fresnel.fourPolar.algorithm.preprocess.segment;

import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.util.image.axis.AxisReassigner;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.ICapturedImageSet;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

class SegmenterUtils {
    /**
     * Creates a channel array of images of one camera from the captured image set.
     * Channel images are set in ascending number of channels in the array. /For
     * this end, we break all the channels of each captured image, and we put them
     * in the corresponding place in the output array.
     * 
     * @param capturedImageSet is the captured image set.
     * @param label            is the label as defined by
     *                         {@code Cameras#getLabels(Cameras)}.
     * @param numChannels      is the number of channels.
     */
    public static IPixelCursor<UINT16>[] extractChannelCursors(ICapturedImageSet capturedImageSet, String label,
            int numChannels) {
        Objects.requireNonNull(capturedImageSet);
        Objects.requireNonNull(label);

        if (numChannels < 1) {
            throw new IllegalArgumentException("Number of channels must be greater than one.");
        }

        ICapturedImage[] capturedImages = capturedImageSet.getCapturedImage(label);

        IPixelCursor<UINT16>[] channelCursors = new IPixelCursor[numChannels];
        for (ICapturedImage iCapturedImage : capturedImages) {
            IMetadata metadata = iCapturedImage.getImage().getMetadata();

            long[] top = metadata.getDim();
            long[] bottom = new long[top.length];
            for (int channel = 0; channel < iCapturedImage.numChannels(); channel++) {
                bottom[metadata.axisOrder().c_axis] = channel;
                top[metadata.axisOrder().c_axis] = channel;

                channelCursors[channel - 1] = iCapturedImage.getImage().getCursor(bottom, top);
            }
        }

        return channelCursors;
    }

    public static IMetadata[] getChannelsMetadata(ICapturedImageSet capturedImageSet, String label, int numChannels) {
        Objects.requireNonNull(capturedImageSet);
        Objects.requireNonNull(label);

        if (numChannels < 1) {
            throw new IllegalArgumentException("Number of channels must be greater than one.");
        }

        ICapturedImage[] capturedImages = capturedImageSet.getCapturedImage(label);

        IMetadata[] channelsMetadata = new IMetadata[numChannels];
        for (ICapturedImage iCapturedImage : capturedImages) {
            IMetadata metadata = iCapturedImage.getImage().getMetadata();

            long[] dim = metadata.getDim();
            for (int channel = 0; channel < iCapturedImage.numChannels(); channel++) {
                dim[metadata.axisOrder().c_axis] = 1;

                channelsMetadata[channel - 1] = new Metadata.MetadataBuilder(dim).axisOrder(metadata.axisOrder())
                        .bitPerPixel(metadata.bitPerPixel()).build();
            }
        }

        return channelsMetadata;

    }

    /**
     * Extract the portion of the given image that belongs to the given
     * polarization, using the {@link IFieldOfView} for that polarization.
     * 
     * @param channelImage is the image that contains the given polarization.
     * @param fov          is the field of view.
     * @param pol          is the polarization to be extract.
     * 
     * @return an image interface that contains only the given polarization.
     */
    public static Image<UINT16> segmentPolarization(IPixelCursor<UINT16> polCursor, IMetadata polImgMetadata,
            ImageFactory factory, IFieldOfView fov, Polarization pol) {
        Objects.requireNonNull(polCursor);
        Objects.requireNonNull(polImgMetadata);
        Objects.requireNonNull(fov);
        Objects.requireNonNull(pol);

        IBoxShape fovBox = fov.getFoV(pol);

        long[] min = fovBox.min();
        long[] max = fovBox.max();
        long[] len = { max[0] - min[0], max[1] - min[1] };

        return AxisReassigner.reassignToXYCZT(polCursor, polImgMetadata, factory, UINT16.zero());
    }
}