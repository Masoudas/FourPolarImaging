package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;

/**
 * Utility methods used for estimating dark background.
 */
class PercentileDarkBackgroundUtil {
    private PercentileDarkBackgroundUtil() {
        new AssertionError();
    }

    public static double[] getFirstPlaneAsArray(Image<UINT16> image) {
        IPixelCursor<UINT16> planeCursor = _getFirstPlaneCursor(image);

        long planeSize = MetadataUtil.getPlaneSize(image.getMetadata());
        double[] imgAsArray = new double[(int) planeSize];

        for (int pixel = 0; pixel < imgAsArray.length; pixel++) {
            imgAsArray[pixel] = planeCursor.next().value().get();
        }

        return imgAsArray;
    }

    public static double computePercentile(double[] values, int quantile) {
        Percentile percentileCalculator = new Percentile(quantile);

        return percentileCalculator.evaluate(values);
    }

    public static double computePercentile(double[] values1, double[] values2, int quantile) {
        Percentile percentileCalculator = new Percentile(quantile);

        double[] concatenated = Arrays.copyOf(values1, values1.length + values2.length);
        System.arraycopy(values2, 0, concatenated, values1.length, values2.length);

        return percentileCalculator.evaluate(concatenated);
    }

    public static double computePercentile(double[] values1, double[] values2, double[] values3, double[] values4,
            int quantile) {
        Percentile percentileCalculator = new Percentile(quantile);

        double[] concatenated12 = Arrays.copyOf(values1, values1.length + values2.length);
        System.arraycopy(values2, 0, concatenated12, values1.length, values2.length);

        double[] concatenated34 = Arrays.copyOf(values3, values3.length + values4.length);
        System.arraycopy(values4, 0, concatenated34, values3.length, values4.length);

        double[] concatenated = Arrays.copyOf(concatenated12, concatenated12.length + concatenated34.length);
        concatenated12 = null;
        System.arraycopy(concatenated34, 0, concatenated, values1.length + values2.length, concatenated34.length);
        concatenated34 = null;

        return percentileCalculator.evaluate(concatenated);
    }

    private static IPixelCursor<UINT16> _getFirstPlaneCursor(Image<UINT16> image) {
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

}