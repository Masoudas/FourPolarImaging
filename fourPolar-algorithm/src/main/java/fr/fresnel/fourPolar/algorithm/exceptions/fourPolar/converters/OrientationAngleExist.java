package fr.fresnel.fourPolar.algorithm.exceptions.fourPolar.converters;

/**
 * An exception that is thrown in case it's impossible to calculate the
 * orientation angle. Note that this happens when this angle does not exist
 * phyisically (which would only happen due to computational issues or noise).
 */

public class OrientationAngleExist extends Exception {
    private static final long serialVersionUID = 7423333309820445025L;
    private final String _message;

    public OrientationAngleExist(String message){
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }



}