package fr.fresnel.fourPolar.core.exceptions.image.orientation;

/**
 * An exception that is thrown when an orientation image cannot be formed.
 */
public class CannotFormOrientationImage extends Exception{
    private static final long serialVersionUID = 3892335660609429542L;
    final private String _message;

    public CannotFormOrientationImage(String message){
        this._message = message;
    }
    
    @Override
    public String getMessage() {
        return _message;
    }
    
}