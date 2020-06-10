package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.sampleSet;

/**
 * Thrown in case {@link ISampleImageSetPreprocessor} fails.
 */
public class SampleSetPreprocessFailure extends Exception {

    private static final long serialVersionUID = 34172387427309L;

    public SampleSetPreprocessFailure(String message) {
        super(message);
    }

}