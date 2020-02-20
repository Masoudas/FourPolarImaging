package fr.fresnel.fourPolar.core.exceptions.image.generic.imgLib2Model.types;

/**
 * Raised when no converter is found from the data types of ImgLib2 to our
 * data types.
 */
public class ConverterNotFound extends Exception{

    private static final long serialVersionUID = 5835893475890347L;

    @Override
    public String getMessage() {
        return "No suitable converter was found for this type.";
    }
}