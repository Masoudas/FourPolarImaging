package fr.fresnel.fourPolar.core.image.generic.axis;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AxisOrderTest {
    @Test
    public void channelAxis_ReturnsCorrectChannelAxis() {
        assertTrue(AxisOrder.getChannelAxis(AxisOrder.XY) == -1 && AxisOrder.getZAxis(AxisOrder.NoOrder) == -1
                && AxisOrder.getChannelAxis(AxisOrder.XYC) == 2 && AxisOrder.getChannelAxis(AxisOrder.XYTC) == 3
                && AxisOrder.getChannelAxis(AxisOrder.XYZTC) == 4);

    }

    @Test
    public void zAxis_ReturnsCorrectaAxis() {
        assertTrue(AxisOrder.getZAxis(AxisOrder.XY) == -1 && AxisOrder.getZAxis(AxisOrder.NoOrder) == -1
                && AxisOrder.getZAxis(AxisOrder.XYC) == -1 && AxisOrder.getZAxis(AxisOrder.XYTC) == -1
                && AxisOrder.getZAxis(AxisOrder.XYZTC) == 2 && AxisOrder.getZAxis(AxisOrder.XYTZC) == 3);

    }

    @Test
    public void getNumDefinedAxis_ReturnsCorrectNumAxis() {
        assertTrue(AxisOrder.getNumDefinedAxis(AxisOrder.NoOrder) == -1
                && AxisOrder.getNumDefinedAxis(AxisOrder.XYC) == 3 && AxisOrder.getNumDefinedAxis(AxisOrder.XYTC) == 4
                && AxisOrder.getNumDefinedAxis(AxisOrder.XYZTC) == 5
                && AxisOrder.getNumDefinedAxis(AxisOrder.XYTZC) == 5);

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