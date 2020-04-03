package fr.fresnel.fourPolar.core.physics.dipole;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.exceptions.physics.dipole.OrientationAngleOutOfRange;


public class OrientationVectorTest {
    @Test
    public void getAngle_NaNAngle_AllAngelsNan() {
        OrientationVector vec1 = new OrientationVector(Double.NaN, 0, 0);
        OrientationVector vec2 = new OrientationVector(0, Double.NaN, 0);
        OrientationVector vec3 = new OrientationVector(0, 0, Double.NaN);

        assertTrue(
            Double.isNaN(vec1.getAngle(OrientationAngle.delta)) &&
            Double.isNaN(vec1.getAngle(OrientationAngle.rho)) &&
            Double.isNaN(vec1.getAngle(OrientationAngle.eta)) &&
            Double.isNaN(vec2.getAngle(OrientationAngle.delta)) &&
            Double.isNaN(vec2.getAngle(OrientationAngle.rho)) &&
            Double.isNaN(vec2.getAngle(OrientationAngle.eta)) &&
            Double.isNaN(vec3.getAngle(OrientationAngle.delta)) &&
            Double.isNaN(vec3.getAngle(OrientationAngle.rho)) &&
            Double.isNaN(vec3.getAngle(OrientationAngle.eta)));

    }
    
    @Test
    public void getAngle_RhoOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector((double)(2 * Math.PI), Double.NaN, Double.NaN);});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector((double)(-Math.PI), Double.NaN, Double.NaN);});
    }

    @Test
    public void getAngle_DeltaOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Double.NaN, (double)(2 * Math.PI), Double.NaN);});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Double.NaN, (double)(-Math.PI), Double.NaN);});
    }

    @Test
    public void getAngle_EtaOutOfRange_RaisesException() {
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Double.NaN, Double.NaN, (double)(Math.PI));});
        assertThrows(OrientationAngleOutOfRange.class , ()->{new OrientationVector(Double.NaN, Double.NaN, (double)(-Math.PI));});
    }

}