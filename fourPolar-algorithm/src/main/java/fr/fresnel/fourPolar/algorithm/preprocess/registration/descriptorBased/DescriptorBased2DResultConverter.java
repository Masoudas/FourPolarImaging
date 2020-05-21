package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.Hashtable;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata.MetadataBuilder;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelTypes;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.preprocess.registration.ChannelRegistrationOrder;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.descriptorBased.DescriptorBasedRegistrationResult;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;
import ij.ImagePlus;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

/**
 * Wraps a {@link DescriptorBased2DResult} of the three polarizations into an
 * {@link DescriptorBasedRegistrationResult}.
 */
class DescriptorBased2DResultConverter {
    Hashtable<ChannelRegistrationOrder, DescriptorBased2DResult> _results;

    public DescriptorBased2DResultConverter() {
        _results = new Hashtable<>();
    }

    public DescriptorBased2DResultConverter set(ChannelRegistrationOrder order, DescriptorBased2DResult result) {
        _results.put(order, result);
        return this;
    }

    public IChannelRegistrationResult convert() {
        DescriptorBasedRegistrationResult result = new DescriptorBasedRegistrationResult();

        for (ChannelRegistrationOrder order : ChannelRegistrationOrder.values()) {
            DescriptorBased2DResult polResult = _results.get(order);

            result.setError(order, polResult.error());
            result.setIsSuccessfulRegistration(order, polResult.isSuccessful());
            result.setAffineTransform(order, this._getAffineTransform(polResult));
            result.setColoredRegistrationResultImage(order, this._convertImage(polResult.compositeImage()));
            result.setDescription(order, this._getDescription(polResult));
        }

        return result;
    }

    public AffineTransform2D _getAffineTransform(DescriptorBased2DResult result) {
        AffineTransform2D transform2d = new AffineTransform2D();
        transform2d.set(result.affineTransform());
        return transform2d;
    }

    public String _getDescription(DescriptorBased2DResult result) {
        if (result.description() != FailureCause.NOT_ENOUGH_FP) {
            return DescriptorBasedRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION;
        } else if ((result.description() != FailureCause.NO_INLIER_AFTER_RANSAC)) {
            return DescriptorBasedRegistrationResult._NO_TRANSFORMATION_DESCRIPTION;
        } else {
            return "";
        }

    }

    private Image<RGB16> _convertImage(ImagePlus image) {
        int[] dim = image.getDimensions();
        long[] dimAsLong = { dim[0], dim[1], dim[2], dim[3], dim[4] };

        IMetadata metadata = new MetadataBuilder(dimAsLong).axisOrder(AxisOrder.XYCZT).bitPerPixel(PixelTypes.RGB_16)
                .build();
        return new ImgLib2ImageFactory().create(ImageJFunctions.wrap(image), new ARGBType(), metadata);

    }

}