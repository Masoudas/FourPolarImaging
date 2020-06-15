package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.IPixelCursor;
import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

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

    public static double computePercentile(double[] values, int quantile) {
        Percentile percentileCalculator = new Percentile(quantile);
        return percentileCalculator.evaluate(values);
    }


}