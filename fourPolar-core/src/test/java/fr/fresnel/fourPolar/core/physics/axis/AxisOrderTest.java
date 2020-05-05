package fr.fresnel.fourPolar.core.physics.axis;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;

public class AxisOrderTest {
    @Test
    public void appendZ_NoZPresent_appendsToEndOfOrder() {
        
        assertTrue(AxisOrder.append_zAxis(AxisOrder.XY) == AxisOrder.XYZ);
        assertTrue(AxisOrder.append_zAxis(AxisOrder.XYCT) == AxisOrder.XYCZT);
        
    }
    
}