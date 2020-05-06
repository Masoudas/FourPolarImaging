package fr.fresnel.fourPolar.io.exceptions.image.generic.metadata;

/**
 * Thrown in case two Metadata can't be converted to one another.
 */
public class IncompatibleMetadata extends Exception{
    private static final long serialVersionUID = 8537985374582312125L;

    public IncompatibleMetadata(String message){
        super(message);
    }
}