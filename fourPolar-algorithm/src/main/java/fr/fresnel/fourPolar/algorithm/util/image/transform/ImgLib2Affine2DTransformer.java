package fr.fresnel.fourPolar.algorithm.util.image.transform;

import java.util.Objects;
import java.util.stream.IntStream;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.util.transform.Affine2D;
import fr.fresnel.fourPolar.core.util.transform.AffineTransform;
import net.imglib2.RandomAccessible;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.FloorInterpolatorFactory;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRandomAccessible;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.ImgUtil;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

/**
 * Using this class, we can apply an {@link AffineTransform} to an ImgLib2
 * implementation of {@link Image}
 * 
 */
class ImgLib2Affine2DTransformer {

    /**
     * Apply affine transform to Imglib2 type using nearest neighbor method for
     * interpolation. The transformation is applied to every plane of the image.
     * 
     * @throws IllegalArgumentException if the given affine transform is not
     *                                  invertible.
     */
    public static <T extends NumericType<T>> void applyfromWithNearestNeighbor(Img<T> image, Affine2D affine2D) {
        Objects.requireNonNull(image, "image can't be null");
        Objects.requireNonNull(affine2D, "affine2D can't be null");
        _checkAffineTransformIsInvertible(affine2D);

        final InterpolatorFactory<T, RandomAccessible<T>> imageInterpolator = new FloorInterpolatorFactory<>();

        final AffineRandomAccessible<T, AffineGet> affineTransformedImage = _applyAffineTransform(image, affine2D,
                imageInterpolator);

        final IntervalView<T> boundedTransformedImage = _boundTheTransformedImageToOriginalSize(image,
                affineTransformedImage);

        _copyTransformedImageToOriginalImage(image, boundedTransformedImage);
    }

    private static void _checkAffineTransformIsInvertible(AffineTransform affineTransform) {
        if (!affineTransform.isInvertible()) {
            throw new IllegalArgumentException("The given affine transform is not invvertible");
        }
    }

    private static <T extends NumericType<T>> AffineRandomAccessible<T, AffineGet> _applyAffineTransform(Img<T> image,
            final Affine2D transform, InterpolatorFactory<T, RandomAccessible<T>> imageInterpolator) {
        final AffineGet expandedTransform = _expandTransfromToAffineOfImageSize(image.numDimensions(), transform);

        final RealRandomAccessible<T> interpolatedImage = _getInterPolatedImage(image, imageInterpolator);
        return RealViews.affine(interpolatedImage, expandedTransform);
    }

    private static <T extends NumericType<T>> RealRandomAccessible<T> _getInterPolatedImage(Img<T> image,
            InterpolatorFactory<T, RandomAccessible<T>> interpolator) {
        return Views.interpolate(Views.extendZero(image), interpolator);
    }

    /**
     * After applying the affine transform, we need to redefine the boundary of the
     * new image, so that it's the same as the original image. Otherwise, the bound
     * is infinite.
     */
    private static <T extends NumericType<T>> IntervalView<T> _boundTheTransformedImageToOriginalSize(
            Img<T> originalImage, AffineRandomAccessible<T, AffineGet> affineTransformedImage) {
        return Views.interval(affineTransformedImage, originalImage);
    }

    /**
     * Using the cursor of ImgLib2, set the content of the original image to the
     * transformed image.
     */
    private static <T extends NumericType<T>> void _copyTransformedImageToOriginalImage(Img<T> originalImg,
            final IntervalView<T> transformedImg) {
        Img<T> concreteTransformedImg = _createAnImgInstanceOfTransformed(transformedImg);
        ImgUtil.copy(concreteTransformedImg, originalImg);
    }

    /**
     * Realize the transformed image as a concrete instance of img.
     * 
     * @param transformedImg
     * @return
     */
    private static <T extends NumericType<T>> Img<T> _createAnImgInstanceOfTransformed(
            final IntervalView<T> transformedImg) {
        Img<T> img = Util.getSuitableImgFactory(transformedImg, Util.getTypeFromInterval(transformedImg))
                .create(transformedImg);

        LoopBuilder.setImages(img, transformedImg).forEachPixel(T::set);
        return img;
    }

    /**
     * In order to transform each plane, we need to define an affine transform that
     * has the same length as the image, but only applies to the plane.
     * 
     * The reason we create an equivalent matrix and put it inside is that the set
     * method checks for invertibility of affine at every set!!!
     */
    private static AffineGet _expandTransfromToAffineOfImageSize(final int imgDim, final Affine2D transform) {
        final net.imglib2.realtransform.AffineTransform expandedTransform = new net.imglib2.realtransform.AffineTransform(
                imgDim);
        double[][] matrix = _createNDimTransform(imgDim, transform);

        expandedTransform.set(matrix);
        return expandedTransform;
    }

    private static double[][] _createNDimTransform(final int imgDim, final Affine2D transform) {
        double[][] matrix = new double[imgDim][imgDim + 1];

        matrix[0][0] = transform.get(0, 0);
        matrix[0][1] = transform.get(0, 1);
        matrix[0][imgDim] = transform.get(0, 2);

        matrix[1][0] = transform.get(1, 0);
        matrix[1][1] = transform.get(1, 1);
        matrix[1][imgDim] = transform.get(1, 2);

        // Set all remaining diagonal elements to 1.
        IntStream.range(2, imgDim).forEach((index) -> matrix[index][index] = 1);
        return matrix;
    }
}