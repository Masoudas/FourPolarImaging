package fr.fresnel.fourPolar.core.util.transform;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Affine3DTest {
    @Test
    public void get_DefaultAffineMatrix_ReturnsCorrectAffineMatrix() {
        Affine3D affine2d = new Affine3D();

        assertArrayEquals(affine2d.get()[0], new double[] { 1, 0, 0, 0 });
        assertArrayEquals(affine2d.get()[1], new double[] { 0, 1, 0, 0 });
        assertArrayEquals(affine2d.get()[2], new double[] { 0, 0, 1, 0 });

    }

    @Test
    public void get_SetElementWise_DoesNotThrowRunTimeException() {
        // To check that after wrapping the setter method of Affine2DTransform, no
        // exception is thrown when matrix becomes singular.
        Affine3D affine2d = new Affine3D();
        assertDoesNotThrow(() -> {
            affine2d.set(0, 0, 1);
        });

    }

    @Test
    public void inInvertible_ZeroDeterminantAndNonzeroDeterminant_ReturnsFalseAndTrueRespectively() {
        Affine3D affine3d_nonInvertible = new Affine3D();
        affine3d_nonInvertible.set(0, 0, 0);
        assertTrue(!affine3d_nonInvertible.isInvertible());

        Affine3D affine3d = new Affine3D();
        assertTrue(affine3d.isInvertible());
    }

}