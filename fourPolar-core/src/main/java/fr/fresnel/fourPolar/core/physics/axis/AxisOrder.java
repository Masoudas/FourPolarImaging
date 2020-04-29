package fr.fresnel.fourPolar.core.physics.axis;

/**
 * Defines the order of axis associated with a data set.
 */
public enum AxisOrder {
    NoOrder, XY, XYC, XYCT, XYCZT, XYT, XYTC, XYZ, XYZC, XYZCT, XYZT, XYZTC;

    /**
     * Returns the number of axis corresponding to the given order.
     * 
     * @return -1 if no order is defined, otherwise the number of axis.
     */
    public static int getNumAxis(AxisOrder axisOrder) {
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
}