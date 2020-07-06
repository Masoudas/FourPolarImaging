package fr.fresnel.fourPolar.algorithm.util.image.stats;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RealType;
import fr.fresnel.fourPolar.core.util.image.metadata.MetadataUtil;

class ImagePercentileCalculator {
    private ImagePercentileCalculator() {
        new AssertionError();
    }

    /**
     * Calculates the demanded percentile of the first plane of the image.
     * 
     * @param image    is the image instance.
     * @param quantile is the desired percentile.
     * @return the percentile as documented in {@link Percentile}.
     */
    public static <T extends RealType> double computePercentileFirstPlane(Image<T> image, int quantile) {
        double[] firstPlane = getFirstPlaneAsArray(image);
        return _computePercentile(firstPlane, quantile);
    }

    private static <T extends RealType> double[] getFirstPlaneAsArray(Image<T> image) {
        IPixelCursor<T> planeCursor = _getFirstPlaneCursor(image);

        long planeSize = MetadataUtil.getPlaneSize(image.getMetadata());
        double[] imgAsArray = new double[(int) planeSize];

        for (int pixel = 0; pixel < imgAsArray.length; pixel++) {
            imgAsArray[pixel] = planeCursor.next().value().getRealValue();
        }

        return imgAsArray;
    }

    private static <T extends RealType> IPixelCursor<T> _getFirstPlaneCursor(Image<T> image) {
        long[] bottomCorner = _getFirstPlaneBottomCorner(image.getMetadata());
        long[] length = _getFirstPlaneLength(image.getMetadata());

        return image.getCursor(bottomCorner, length);
    }

    private static long[] _getFirstPlaneBottomCorner(IMetadata metadata) {
        return new long[metadata.getDim().length];
    }

    private static long[] _getFirstPlaneLength(IMetadata metadata) {
        long[] planeLen = metadata.getDim().clone();

        for (int dim = 2; dim < planeLen.length; dim++) {
            planeLen[dim] = 1;
        }
        return planeLen;
    }

    private static double _computePercentile(double[] values, int quantile) {
        Percentile percentileCalculator = new Percentile(quantile);
        return percentileCalculator.evaluate(values);
    }

}