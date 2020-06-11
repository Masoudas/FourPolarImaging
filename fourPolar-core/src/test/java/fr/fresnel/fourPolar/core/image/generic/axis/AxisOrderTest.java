package fr.fresnel.fourPolar.core.image.generic.axis;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AxisOrderTest {
    @Test
    public void channelAxis_ReturnsCorrectChannelAxis() {
        assertTrue(AxisOrder.XY.c_axis == -1 && AxisOrder.NoOrder.c_axis == -1 && AxisOrder.XYC.c_axis == 2
                && AxisOrder.XYTC.c_axis == 3 && AxisOrder.XYZTC.c_axis == 4);

    }

    @Test
    public void zAxis_ReturnsCorrectaAxis() {
        assertTrue(AxisOrder.XY.z_axis == -1 && AxisOrder.NoOrder.z_axis == -1 && AxisOrder.XYC.z_axis == -1
                && AxisOrder.XYTC.z_axis == -1 && AxisOrder.XYZTC.z_axis == 2 && AxisOrder.XYTZC.z_axis == 3);

    }

    @Test
    public void getNumDefinedAxis_ReturnsCorrectNumAxis() {
        assertTrue(AxisOrder.NoOrder.numAxis == -1 && AxisOrder.XYC.numAxis == 3 && AxisOrder.XYTC.numAxis == 4
                && AxisOrder.XYZTC.numAxis == 5 && AxisOrder.XYTZC.numAxis == 5);

    }

    @Test
    public void append_zAxis_ReturnsAppendedZAxis() {
        assertTrue(AxisOrder.append_zAxis(AxisOrder.NoOrder) == AxisOrder.NoOrder
                && AxisOrder.append_zAxis(AxisOrder.XYC) == AxisOrder.XYZC
                && AxisOrder.append_zAxis(AxisOrder.XYTC) == AxisOrder.XYCZT
                && AxisOrder.append_zAxis(AxisOrder.XYZTC) == AxisOrder.XYZTC
                && AxisOrder.append_zAxis(AxisOrder.XYTZC) == AxisOrder.XYTZC);

    }

    @Test
    public void appendChannelToEnd_ReturnsOrderWithAppendedChannel() {
        assertThrows(IllegalArgumentException.class, () -> AxisOrder.appendChannelToEnd(AxisOrder.NoOrder));

        assertTrue(AxisOrder.appendChannelToEnd(AxisOrder.XYC) == AxisOrder.XYC
                && AxisOrder.appendChannelToEnd(AxisOrder.XY) == AxisOrder.XYC
                && AxisOrder.appendChannelToEnd(AxisOrder.XYT) == AxisOrder.XYTC);
    }

}