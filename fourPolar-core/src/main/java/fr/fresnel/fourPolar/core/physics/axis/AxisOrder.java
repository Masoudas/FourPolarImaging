package fr.fresnel.fourPolar.core.physics.axis;

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

}