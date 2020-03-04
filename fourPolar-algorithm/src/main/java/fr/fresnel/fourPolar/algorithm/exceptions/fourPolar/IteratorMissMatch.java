package fr.fresnel.fourPolar.algorithm.exceptions.fourPolar;

public class IteratorMissMatch extends Exception {
    private static final long serialVersionUID = 132478932749223L;

    final private String _message;

    public IteratorMissMatch(String message){
        this._message = message;
    }

    @Override
    public String getMessage() {
        return this._message;
    }
}
