package fr.fresnel.fourPolar.algorithm.util.image.generic.transform;

import fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.ConverterToImgLib2NotFound;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImageToImgLib2Converter;
import fr.fresnel.fourPolar.core.image.generic.imgLib2Model.ImgLib2ImageFactory;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.Float32;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.PixelType;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;

/**
 * An static class for applying an {@link AffineTransform} to an {@link Image}.
 */
public class ImageAffineTransformer {
    private ImageAffineTransformer() {
        throw new AssertionError();
    }

    /**
     * Apply affine transform to the given image type using nearest neighbor method
     * for interpolation. The transformation is applied to every plane of the image.
     * 
     * @param <T>             is the pixel type of the image
     * @param image           is the image
     * @param affineTransform is the affine transform
     * 
     * @throws IllegalArgumentException if there's no transformation implementation
     *                                  for the given image type, or if the given
     *                                  affine transform is not invertible.
     */
    @SuppressWarnings("unchecked")
    public static <T extends PixelType> void apply2DNearestNeighborInterplation(Image<T> image,
            Affine2D affineTransform) {
        if (image.getFactory() instanceof ImgLib2ImageFactory) {
            try {

                T type = image.getCursor().next().value();
                // TODO how to get rid of this?
                if (type instanceof UINT16) {
                    ImgLib2Affine2DTransformer.applyWithNearestNeighbor(
                            ImageToImgLib2Converter.getImg((Image<UINT16>) image, (UINT16) type), affineTransform);
                } else if (type instanceof Float32) {
                    ImgLib2Affine2DTransformer.applyWithNearestNeighbor(
                            ImageToImgLib2Converter.getImg((Image<Float32>) image, (Float32) type), affineTransform);
                } else if (type instanceof ARGB8) {
                    ImgLib2Affine2DTransformer.applyWithNearestNeighbor(
                            ImageToImgLib2Converter.getImg((Image<Float32>) image, (Float32) type), affineTransform);

                } else {
                    throw new IllegalArgumentException("Implementation need to be extended for the new type!");
                }
            } catch (ConverterToImgLib2NotFound e) {

            }

        } else {
            throw new IllegalArgumentException("No affine transform applier is found for this image");
        }
    }

}