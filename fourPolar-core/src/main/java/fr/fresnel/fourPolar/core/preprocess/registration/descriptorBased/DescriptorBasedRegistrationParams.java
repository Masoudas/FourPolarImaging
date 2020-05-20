package fr.fresnel.fourPolar.core.preprocess.registration.descriptorBased;

/**
 * Contains the parameters to be set when applying the descriptor based
 * registration algorithm.
 */
public class DescriptorBasedRegistrationParams {
    /**
     * std1 of Difference of Gaussian in filter gaussian(0, std1) - gaussian(0,
     * std2).
     */
    private double _std1_dog = 1.2;

    /**
     * The threshold for detecting feature points in the image.
     */
    private double _detectionThreshold = 0.05;

    /**
     * The accepted ransac error
     */
    private int _ransac_error = 5;

    /**
     * Returns an instance of the parameter set that is set to default values.
     */
    public static DescriptorBasedRegistrationParams startFromDefault() {
        return new DescriptorBasedRegistrationParams();
    }

    private DescriptorBasedRegistrationParams() {

    }

    /**
     * @return the _detectionThreshold
     */
    public double getDetectionThreshold() {
        return _detectionThreshold;
    }

    /**
     * @return the _ransac_error
     */
    public int getRansacError() {
        return _ransac_error;
    }

    /**
     * @return the _std1_dog
     */
    public double get_std1_dog() {
        return _std1_dog;
    }

    public void setDetectionThreshold(double detectionThreshold) {
        this._detectionThreshold = detectionThreshold;
    }

    public void setRansacError(int ransacError) {
        this._ransac_error = ransacError;
    }

    public void set_std1_dog(double std1_dog) {
        this._std1_dog = std1_dog;
    }

}