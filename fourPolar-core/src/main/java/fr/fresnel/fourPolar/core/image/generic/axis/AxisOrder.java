package fr.fresnel.fourPolar.core.image.generic.axis;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;

/**
 * Defines the order of axis associated with a data set. NO FURTHER ORDERS
 * SHOULD BE ADDED WITHOUT CAREFUL CONSIDERATION BY CHECKING THE ENTIRE CODE
 * STRUCTURE, WHERE AXIS ORDER CONTRIBUTES.
 */
public enum AxisOrder {
    NoOrder(-1, -1, -1, -1), XY(-1, -1, -1, 2), XYC(2, -1, -1, 3), XYCT(2, -1, 3, 4), XYCZT(2, 3, 4, 5),
    XYT(-1, -1, 3, 3), XYTC(3, -1, 2, 4), XYZ(-1, 2, -1, 3), XYZC(3, 2, -1, 4), XYZCT(3, 2, 4, 5), XYZT(-1, 2, 3, 4),
    XYZTC(4, 2, 3, 5), XYTZC(4, 3, 2, 5);

    public static AxisOrder fromString(String axisOrder) throws UnsupportedAxisOrder {
        try {
            return AxisOrder.valueOf(axisOrder);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedAxisOrder();
        }
    }

    public final int z_axis;
    public final int c_axis;
    public final int t_axis;
    public final int numAxis;

    AxisOrder(int c_axis, int z_axis, int t_axis, int numAxis) {
        this.z_axis = z_axis;
        this.c_axis = c_axis;
        this.t_axis = t_axis;
        this.numAxis = numAxis;
    }

}