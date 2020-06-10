package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.sampleSet;

/**
 * Thrown in case {@link SampleImageSetPreprocessorBuilder} fails.
 */
public class SampleSetPreprocessBuildFailure extends Exception {

    private static final long serialVersionUID = 34172387427309L;

    public SampleSetPreprocessBuildFailure(String message) {
        super(message);
    }

}