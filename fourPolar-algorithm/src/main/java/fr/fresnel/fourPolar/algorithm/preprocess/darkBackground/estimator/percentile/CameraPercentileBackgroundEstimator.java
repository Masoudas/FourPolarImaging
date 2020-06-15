package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import fr.fresnel.fourPolar.core.image.generic.Image;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.UINT16;
import fr.fresnel.fourPolar.core.image.polarization.IPolarizationImageSet;
import fr.fresnel.fourPolar.core.image.polarization.PolarizationImageUtils;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Using this class, we can estimate the background of one camera using a
 * polarization image set and with the knowledge of polarizations present in
 * that camera. The background is estimates using the first plane of the
 * polarization images.
 */
class CameraPercentileBackgroundEstimator {
    /**
     * Estimate background for the given camera, using the polarization image set
     * and polarizations present in that camera.
     */
    public static double estimate(IPolarizationImageSet imageSet, Polarization[] polarizations,
            int percentileThreshold) {
        double[] cameraIntensities = _getCameraFirstImagePlaneAsArray(imageSet, polarizations);
        return PercentileDarkBackgroundUtil.computePercentile(cameraIntensities, percentileThreshold);
    }

    private static double[] _getCameraFirstImagePlaneAsArray(IPolarizationImageSet imageSet,
            Polarization[] polarizations) {
        int polarizationsPerCamera = polarizations.length;
        int polarizationPlaneSize = _getPolarizationImagePlaneSize(imageSet);
        double[] cameraIntensities = new double[polarizationsPerCamera * polarizationPlaneSize];

        for (int polIndex = 0; polIndex < polarizationsPerCamera; polIndex++) {
            double[] polAsArray = _getPolarizationImageFirstPlaneAsArray(imageSet, polarizations[polIndex]);
            System.arraycopy(polAsArray, 0, cameraIntensities, polarizationPlaneSize * polIndex, polarizationPlaneSize);
        }

        return cameraIntensities;
    }

    private static double[] _getPolarizationImageFirstPlaneAsArray(IPolarizationImageSet imageSet,
            Polarization polarization) {
        Image<UINT16> polImage = imageSet.getPolarizationImage(polarization).getImage();
        return PercentileDarkBackgroundUtil.getFirstPlaneAsArray(polImage);
    }

    private static int _getPolarizationImagePlaneSize(IPolarizationImageSet imageSet) {
        return (int) PolarizationImageUtils.getPlaneSize(imageSet);
    }

}