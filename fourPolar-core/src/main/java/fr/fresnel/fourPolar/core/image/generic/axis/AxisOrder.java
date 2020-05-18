package fr.fresnel.fourPolar.core.image.generic.axis;

import java.util.Objects;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;

/**
 * Defines the order of axis associated with a data set. NO FURTHER ORDERS
 * SHOULD BE ADDED WITHOUT CAREFUL CONSIDERATION BY CHECKING THE ENTIRE CODE
 * STRUCTURE, WHERE AXIS ORDER CONTRIBUTES.
 */
public enum AxisOrder {
    NoOrder(-1, -1, -1), XY(-1, -1, -1), XYC(2, -1, -1), XYCT(2, -1, 3), XYCZT(2, 3, 4), XYT(-1, -1, 3), XYTC(3, -1, 2),
    XYZ(-1, 2, -1), XYZC(3, 2, -1), XYZCT(3, 2, 4), XYZT(-1, 2, 3), XYZTC(4, 2, 3), XYTZC(4, 3, 2);

    /**
     * Returns the number of labeled axis corresponding to the given order.
     * <p>
     * WARNING: This method should not be used to check how many dimensions an
     * entity has, given that it returns -1 for NoOrder. For example for the
     * {@link Image} interface, we should directly work with the dimension vector of
     * image.
     * 
     * @return -1 if no order is defined, otherwise the number of axis.
     */
    public static int getNumDefinedAxis(AxisOrder axisOrder) {
        if (axisOrder == NoOrder) {
            return -1;
        } else {
            return axisOrder.name().length();
        }
    }

    /**
     * Returns the dimension (from zero) of channel axis (if exists) and -1
     * otherwise.
     */
    public static int getChannelAxis(AxisOrder axisOrder) {
        return axisOrder.name().indexOf('C');
    }

    /**
     * Returns the dimension (from zero) of time axis (if exists) and -1 otherwise.
     */
    public static int getTimeAxis(AxisOrder axisOrder) {
        return axisOrder.name().indexOf('T');
    }

    /**
     * Returns the dimension (from zero) of z-axis (if exists) and -1 otherwise.
     */
    public static int getZAxis(AxisOrder axisOrder) {
        return axisOrder.name().indexOf('Z');
    }

    /**
     * If z-axis already present, returns the originial order. Else, appends z-axis
     * to first convenient location of the given order. The goal is to increase the
     * number of axis by one.
     * 
     * Any change to the above decision should seriously be entertained (@see
     * WholeSampleStick3DPainterBuilder).
     */
    public static AxisOrder append_zAxis(AxisOrder axisOrder) {
        if (AxisOrder.getZAxis(axisOrder) > 0) {
            return axisOrder;
        }

        if (axisOrder == XY) {
            return XYZ;
        } else if (axisOrder == XYT) {
            return XYZT;
        } else if (axisOrder == XYC) {
            return XYZC;
        } else if (axisOrder == XYTC || axisOrder == XYCT) {
            return XYCZT;
        } else {
            return NoOrder;
        }

    }

    /**
     * Throws exception if NoOrder is given. Does nothing if order with existing
     * channel is given. Appends a channel axis to the end of this axis-order.
     * 
     */
    public static AxisOrder appendChannelToEnd(AxisOrder axisOrder) {
        Objects.requireNonNull(axisOrder, "axisOrder cannot be null.");
        if (axisOrder == AxisOrder.NoOrder) {
            throw new IllegalArgumentException("Can't append channel to NoOrder.");
        }

        if (AxisOrder.getChannelAxis(axisOrder) > 0) {
            return axisOrder;
        }

        return AxisOrder.valueOf(axisOrder.name() + "C");
    }

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

    AxisOrder(int c_axis, int z_axis, int t_axis) {
        this.z_axis = z_axis;
        this.c_axis = c_axis;
        this.t_axis = t_axis;
    }

}