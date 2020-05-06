package fr.fresnel.fourPolar.core.exceptions.image.generic.axis;

/**
 * Thrown in case the axis order is not found in {@link AxisOrder}.
 */
public class AxisOrderUndefined extends Exception {

    private static final long serialVersionUID = 421565378256344121L;

    @Override
    public String getMessage() {
        return "Axis order is undefined";
    }
}