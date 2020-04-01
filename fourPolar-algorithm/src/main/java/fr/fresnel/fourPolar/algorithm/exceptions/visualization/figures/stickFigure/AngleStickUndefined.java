package fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure;

/**
 * Thrown when the angle stick (i.e, {@link IAngleStick}) cannot be defined for a dipole,
 * especially due to the fact that an orientation angle is Nan.
 */
public class AngleStickUndefined extends Exception {
    private static final long serialVersionUID = 7597428975892748902L;

    public AngleStickUndefined(String message) {
        super(message);
    }

    
}




