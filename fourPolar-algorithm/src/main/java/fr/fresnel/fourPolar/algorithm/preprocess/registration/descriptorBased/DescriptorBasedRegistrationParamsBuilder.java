package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import java.util.Objects;

import registration.descriptorBased.plugin.DescriptorParameters;

class DescriptorBasedRegistrationParamsBuilder {
    /**
     * Three sets of dog sigma values, starting from rather large (row one) to
     * rather small (row three) bead detection. The larger the detector, the lesser
     * the chances of detecting noise as FP.
     */
    public static double[][] DOG_SIGMA_CHOICES = { { 2.23, 2.65 }, { 2.04, 2.42 }, { 1.787, 2.21 } };

    /**
     * Three choices for intensity thresholding the image. Note that the smaller the
     * threshold, the greater the chance of detecting noise.
     */
    public static double[] THRESHOLD_CHOICES = { 0.04, 0.02, 0.01 };

    private double[] _dog_sigma = DOG_SIGMA_CHOICES[0];
    private double _fpDetectionThr = THRESHOLD_CHOICES[0];

    // Two RANSAC related params. Increasing these relaxes RANSAC.
    private int _numNeighborsRansac = 3;
    private int _redundancyRansac = 1;

    /**
     * Copy constructor.
     */
    public DescriptorBasedRegistrationParamsBuilder(DescriptorParameters params) {
        Objects.requireNonNull(params);

        _dog_sigma = new double[] { params.sigma1, params.sigma2 };
        _fpDetectionThr = params.threshold;
        _numNeighborsRansac = params.numNeighbors;
        _redundancyRansac = params.redundancy;
    }

    public DescriptorBasedRegistrationParamsBuilder() {

    }

    /**
     * Set DoG sigma values with a 2D row vector.
     */
    public DescriptorBasedRegistrationParamsBuilder dog_sigma(double[] sigma) {
        Objects.requireNonNull(sigma);
        if (sigma.length != 2) {
            throw new IllegalArgumentException("sigma must have two elements");
        }
        this._dog_sigma = sigma;

        return this;
    }

    /**
     * Set the intensity threshold for detecting FPs.
     */
    public DescriptorBasedRegistrationParamsBuilder detectionThresholdFP(double threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("threshold must be greater than zero");
        }

        this._fpDetectionThr = threshold;

        return this;
    }

    /**
     * Increasing this parameter (from 3 upwards) relaxes the RANSAC.
     */
    public DescriptorBasedRegistrationParamsBuilder numNeighborsRansac(int n) {
        if (n < 3) {
            throw new IllegalArgumentException("Number of RANSAC neighbors should be greater than equal 3.");
        }
        this._numNeighborsRansac = n;
        return this;
    }

    /**
     * Increasing this parameter (from 1 upwards) relaxes the RANSAC.
     */
    public DescriptorBasedRegistrationParamsBuilder redundancyRansac(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Redundancy of RANSAC should be greater than equal 1.");
        }
        this._redundancyRansac = n;
        return this;
    }

    public DescriptorParameters build() {
        DescriptorParameters params = new DescriptorParameters();
        // Channel of images to be used.
        params.channel1 = 0;
        params.channel2 = 0;

        // No idea what this parameter does.
        params.globalOpt = 0;

        // Ransac related parameters.
        params.numNeighbors = this._numNeighborsRansac;
        params.redundancy = this._redundancyRansac;
        params.range = 0;
        params.ransacThreshold = 5;

        // Put little markers on top of detected FPs.
        params.setPointsRois = false;

        // Dimension of the images to be registered, which should always be two.
        params.dimensionality = 2;

        // look for bright points surrounded by dark points, not vice versa.
        params.lookForMaxima = true;
        params.lookForMinima = false;

        // Dog sigma values
        params.sigma1 = this._dog_sigma[0];
        params.sigma2 = this._dog_sigma[1];

        // Detection threshold.
        params.threshold = this._fpDetectionThr;

        return params;
    }
}