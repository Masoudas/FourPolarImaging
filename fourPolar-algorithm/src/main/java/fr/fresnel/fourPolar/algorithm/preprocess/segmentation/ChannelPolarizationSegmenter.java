package fr.fresnel.fourPolar.algorithm.preprocess.segmentation;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;

/**
 * An abstract class for segmenting a paritcular polarization for different
 * channels. Different channels might be containted inside a particular captured
 * image {@see MultiChannelPolarizationSegmenter}, or they might be inside
 * single channel images {@see SingleChannelPolarizationSegmenter}.
 */
abstract class ChannelPolarizationSegmenter {
    public abstract Image<UINT16>[] segment(ICapturedImage[] capturedImages, IBoxShape polFoV);

    protected Image<UINT16> _createSegmentedImageFromInterval(Image<UINT16> image, long[] bottomCorner, long[] len) {
        IPixelCursor<UINT16> segmentedImgCursor = image.getCursor(bottomCorner, len);
        IMetadata segmentedImgMetadata = new Metadata.MetadataBuilder(len).axisOrder(image.getMetadata().axisOrder())
                .bitPerPixel(PixelTypes.UINT_16).build();
        Image<UINT16> segmentedImage = new SegmentedImage(segmentedImgCursor, image.getFactory(), segmentedImgMetadata);
        return segmentedImage;
    }

    protected int _getTotalNumChannels(ICapturedImage[] capturedImages){
        int numChannels = 0;

        for (ICapturedImage iCapturedImage : capturedImages) {
            numChannels += iCapturedImage.numChannels();
        }

        return numChannels;
    }
}