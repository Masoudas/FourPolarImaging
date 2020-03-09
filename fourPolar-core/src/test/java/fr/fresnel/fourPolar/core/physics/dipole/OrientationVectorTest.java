package fr.fresnel.fourPolar.core.physics.dipole;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;


public class OrientationVectorTest {
    @Test
    public void getAngle_AllNaNAngles_ReturnsNanForAngles() {
        OrientationVector vector = new OrientationVector(Float.NaN, Float.NaN, Float.NaN);

        assertTrue(
            Float.isNaN(vector.getAngle(OrientationAngle.delta)) &&
            Float.isNaN(vector.getAngle(OrientationAngle.rho)) &&
            Float.isNaN(vector.getAngle(OrientationAngle.eta)));
    }
    
    @Test
    public void getAngle_RhoOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector((float)(2 * Math.PI), Float.NaN, Float.NaN);});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector((float)(-Math.PI), Float.NaN, Float.NaN);});
    }

    @Test
    public void getAngle_DeltaOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Float.NaN, (float)(2 * Math.PI), Float.NaN);});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Float.NaN, (float)(-Math.PI), Float.NaN);});
    }

    @Test
    public void getAngle_EtaOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Float.NaN, Float.NaN, (float)(Math.PI));});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Float.NaN, Float.NaN, (float)(-Math.PI));});
    }

}