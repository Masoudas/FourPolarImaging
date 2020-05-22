package fr.fresnel.fourPolar.algorithm.preprocess.realignment;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.pixel.IPixel;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.BeadSetProcessResult;
import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationOrder;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform2D;
import net.imglib2.Cursor;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRandomAccessible;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

/**
 * Realign polarization images using ImgLib2 library.
 */
public class ImgLib2Realigner implements IChannelRealigner {
    private final BeadSetProcessResult _beadSetProcessResult;

    public ImgLib2Realigner(BeadSetProcessResult beadSetProcessResult) {
        this._beadSetProcessResult = beadSetProcessResult;
    }

    @Override
    public void realign(IPolarizationImageSet imageSet) {
        int channel = imageSet.channel();
        IChannelRegistrationResult registrationResult = this._beadSetProcessResult.getRegistrationResult(channel);

        Image<UINT16> pol45 = imageSet.getPolarizationImage(Polarization.pol45).getImage();
        this._realignPolarization(pol45, registrationResult.getAffineTransform(RegistrationOrder.Pol45_to_Pol0));

        Image<UINT16> pol90 = imageSet.getPolarizationImage(Polarization.pol90).getImage();
        this._realignPolarization(pol90, registrationResult.getAffineTransform(RegistrationOrder.Pol90_to_Pol0));

        Image<UINT16> pol135 = imageSet.getPolarizationImage(Polarization.pol135).getImage();
        this._realignPolarization(pol135, registrationResult.getAffineTransform(RegistrationOrder.Pol135_to_Pol0));
    }

    private void _realignPolarization(Image<UINT16> image, AffineTransform2D transform) {
        Img<UnsignedShortType> imageAsImgLib2 = null;
        try {
            imageAsImgLib2 = ImageToImgLib2Converter.getImg(image, UINT16.zero());
        } catch (ConverterToImgLib2NotFound e) {
            // TODO Get rid of this!
        }
        final AffineGet expandedTransform = _expandTransfromToAffineOfImageSize(image.getMetadata(), transform);

        final RealRandomAccessible<UnsignedShortType> field = Views.interpolate(Views.extendZero(imageAsImgLib2),
                new NLinearInterpolatorFactory<>());

        final AffineRandomAccessible<UnsignedShortType, AffineGet> sheared = RealViews.affine(field, expandedTransform);

        // apply the original bounding box to the transformed image
        final IntervalView<UnsignedShortType> bounded = Views.interval(sheared, imageAsImgLib2);

        Cursor<UnsignedShortType> transformedCursor = bounded.cursor();
        for (IPixelCursor<UINT16> cursor = image.getCursor(); cursor.hasNext();) {
            IPixel<UINT16> pixel = cursor.next();
            pixel.value().set(transformedCursor.get().get());
            cursor.setPixel(pixel);
        }
    }

    /**
     * In order to transform each plane, we need to define an affine transform that
     * has the same length as the image, but only applies to the plane.
     */
    private AffineGet _expandTransfromToAffineOfImageSize(IMetadata metadata, AffineTransform2D transform) {
        int numDimensions = metadata.getDim().length;

        net.imglib2.realtransform.AffineTransform expandedTransform = new net.imglib2.realtransform.AffineTransform(
                numDimensions);
        expandedTransform.set(transform.get(0, 0), 0, 0);
        expandedTransform.set(transform.get(0, 1), 0, 1);
        expandedTransform.set(transform.get(0, 2), 0, numDimensions);

        expandedTransform.set(transform.get(1, 0), 1, 0);
        expandedTransform.set(transform.get(1, 1), 1, 1);
        expandedTransform.set(transform.get(1, 2), 1, numDimensions);

        return expandedTransform;
    }
}