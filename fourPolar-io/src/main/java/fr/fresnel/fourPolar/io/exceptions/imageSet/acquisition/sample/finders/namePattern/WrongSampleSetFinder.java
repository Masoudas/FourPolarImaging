package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern;

/**
 * Thrown when number of cameras does not align with the number of labels provided
 * for the given labels.
 */
public class WrongSampleSetFinder extends IllegalArgumentException{
    private static final long serialVersionUID = -1355397245846720553L;

    public WrongSampleSetFinder(String message) {
        super(message);
    }

    
}