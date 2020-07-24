package fr.fresnel.fourPolar.core.util.image.generic.metadata.axis;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.util.image.generic.pixel.PixelTypeUtil;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Defines an {@link AxisOrder} for the given image that does not have an axis
 * order.
 */
class AxisDefiner {
    private static final ImgLib2ImageFactory _imglib2Factory = new ImgLib2ImageFactory();

    private AxisDefiner() {
        throw new AssertionError();
    }

    /**
     * Defines an axis order for the given image. Note that the new image refers to
     * the same underlying image, i.e, even though {@link IMetadata} changes, the
     * underlying image pixels are not.
     * 
     * @param <T>          is the pixel type of the image.
     * @param image        is the image.
     * @param newAxisOrder is the axis order to be assigned to the image.
     * 
     * @throws IllegalArgumentException if the given image already has an axis
     *                                  order, or the new axis order has unequal
     *                                  dimension to the image.
     * @return an image instance with axis order that contains the reference to the
     *         image with no axis order.
     */
    public static <T extends PixelType> Image<T> defineAxis(Image<T> image, AxisOrder newAxisOrder) {
        Objects.requireNonNull(image, "image can't be null.");
        Objects.requireNonNull(newAxisOrder, "axisOrder can't be null.");

        _checkAxisIsUndefined(image);
        _checkNewAxisOrderMatchesImageDimension(image, newAxisOrder);

        return _createImageWithNewAxisOrder(image, newAxisOrder);
    }

    private static <T extends PixelType> void _checkAxisIsUndefined(Image<T> image) {
        if (image.getMetadata().axisOrder() != AxisOrder.NoOrder) {
            throw new IllegalArgumentException("Axis order is already defined.");
        }

    }

    private static <T extends PixelType> void _checkNewAxisOrderMatchesImageDimension(Image<T> image,
            AxisOrder newAxisOrder) {
        if (!MetadataUtil.numAxisEqualsDimension(newAxisOrder, image.getMetadata().getDim())) {
            throw new IllegalArgumentException("The new axis order does not equal image dimension.");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends PixelType> Image<T> _createImageWithNewAxisOrder(Image<T> image, AxisOrder newAxisOrder) {
        IMetadata metadataWithAxis = _createMetadataWithNewAxisOrder(image.getMetadata(), newAxisOrder);

        ImageFactory factoryType = image.getFactory();
        PixelType pixelType = PixelTypeUtil.getPixelType(image);
        if (factoryType instanceof ImgLib2ImageFactory) {
            try {
                if (pixelType instanceof ARGB8) {
                    return (Image<T>) _imglib2Factory.create(
                            ImageToImgLib2Converter.getImg((Image<ARGB8>) image, ARGB8.zero()), new ARGBType(),
                            metadataWithAxis);
                } else if (pixelType instanceof UINT16) {
                    return (Image<T>) _imglib2Factory.create(
                            ImageToImgLib2Converter.getImg((Image<UINT16>) image, UINT16.zero()),
                            new UnsignedShortType(), metadataWithAxis);
                } else if (pixelType instanceof Float32) {
                    return (Image<T>) _imglib2Factory.create(
                            ImageToImgLib2Converter.getImg((Image<Float32>) image, Float32.zero()), new FloatType(),
                            metadataWithAxis);
                } else {
                    throw new IllegalArgumentException(
                            "Can't define axis order because image pixel type is undefined.");
                }

            } catch (ConverterToImgLib2NotFound e) {
                return null;
            }

        } else {
            throw new IllegalArgumentException("Can't define axis order because image factory is undefined.");
        }

    }

    private static IMetadata _createMetadataWithNewAxisOrder(IMetadata noAxisMetadata, AxisOrder newAxisOrder) {
        return new Metadata.MetadataBuilder(noAxisMetadata).axisOrder(newAxisOrder).build();
    }
}