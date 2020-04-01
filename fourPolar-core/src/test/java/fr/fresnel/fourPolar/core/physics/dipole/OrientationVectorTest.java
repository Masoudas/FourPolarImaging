package fr.fresnel.fourPolar.core.physics.dipole;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;


public class OrientationVectorTest {
    @Test
    public void getAngle_NaNAngle_AllAngelsNan() {
        OrientationVector vec1 = new OrientationVector(Float.NaN, 0, 0);
        OrientationVector vec2 = new OrientationVector(0, Float.NaN, 0);
        OrientationVector vec3 = new OrientationVector(0, 0, Float.NaN);

        assertTrue(
            Float.isNaN(vec1.getAngle(OrientationAngle.delta)) &&
            Float.isNaN(vec1.getAngle(OrientationAngle.rho)) &&
            Float.isNaN(vec1.getAngle(OrientationAngle.eta)) &&
            Float.isNaN(vec2.getAngle(OrientationAngle.delta)) &&
            Float.isNaN(vec2.getAngle(OrientationAngle.rho)) &&
            Float.isNaN(vec2.getAngle(OrientationAngle.eta)) &&
            Float.isNaN(vec3.getAngle(OrientationAngle.delta)) &&
            Float.isNaN(vec3.getAngle(OrientationAngle.rho)) &&
            Float.isNaN(vec3.getAngle(OrientationAngle.eta)));

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