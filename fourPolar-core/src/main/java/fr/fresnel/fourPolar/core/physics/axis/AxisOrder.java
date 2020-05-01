package fr.fresnel.fourPolar.core.physics.axis;

/**
 * Defines the order of axis associated with a data set. NO FURTHER ORDERS
 * SHOULD BE ADDED WITHOUT CAREFUL CONSIDERATION BY CHECKING THE ENTIRE CODE
 * STRUCTURE, WHERE AXIS ORDER CONTRIBUTES.
 */
public enum AxisOrder {
    NoOrder, XY, XYC, XYCT, XYCZT, XYT, XYTC, XYZ, XYZC, XYZCT, XYZT, XYZTC;

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
        if (axisOrder == XYC || axisOrder == XYCT || axisOrder == XYCZT) {
            return 2;
        } else if (axisOrder == XYZC || axisOrder == XYTC || axisOrder == XYZCT) {
            return 3;
        } else if (axisOrder == XYZTC) {
            return 4;
        } else {
            return -1;
        }

    }

    /**
     * Returns the dimension (from zero) of z-axis (if exists) and -1 otherwise.
     */
    public static int getZAxis(AxisOrder axisOrder) {
        if (axisOrder == XYCZT) {
            return 3;
        } else if (axisOrder == XYZ || axisOrder == XYZT || axisOrder == XYZC || axisOrder == XYZTC
                || axisOrder == XYZCT) {
            return 2;
        } else {
            return -1;
        }

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
}