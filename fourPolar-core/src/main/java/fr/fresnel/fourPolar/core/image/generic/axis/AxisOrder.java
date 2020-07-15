package fr.fresnel.fourPolar.core.image.generic.axis;

import fr.fresnel.fourPolar.core.exceptions.image.generic.axis.UnsupportedAxisOrder;

/**
 * Defines the order of axis associated with a data set.
 * <p>
 * Note: Every axis-order should start with XY. @see {@link #planeAxisOrder()}.
 * 
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

    /**
     * Returns the axis order in the plane (i.e, first two dimensions). This method
     * always returns XY (because all axis-order start with XY), and for
     * {@link #NoOrder} returns {@value #NoOrder}
     * 
     * @param axisOrder is the axis order.
     * @return axis order of plane.
     */
    public static AxisOrder planeAxisOrder(AxisOrder axisOrder) {
        if (axisOrder == NoOrder) {
            return NoOrder;
        }

        return XY;
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