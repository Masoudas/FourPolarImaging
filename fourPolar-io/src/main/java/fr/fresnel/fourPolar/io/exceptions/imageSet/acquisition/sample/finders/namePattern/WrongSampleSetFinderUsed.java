package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern;

/**
 * Thrown when number of cameras does not align with the number of labels provided
 * for the given labels.
 */
public class WrongSampleSetFinderUsed extends IllegalArgumentException{
    private static final long serialVersionUID = -1355397245846720553L;

    public WrongSampleSetFinderUsed(String message) {
        super(message);
    }

    
}