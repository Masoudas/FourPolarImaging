package fr.fresnel.fourPolar.core.exceptions.fourPolar;


/**
 * The exception that is thrown in case there's no {@link OpticalPropagation} 
 * is not in the database of optical propagations.
 */
public class PropagationChannelNotInDatabase extends Exception{
    private static final long serialVersionUID = 4234549789843L;

    @Override
    public String getMessage() {
        return "No optical propagation is defined for the given propagation channel in the database";
    }
}