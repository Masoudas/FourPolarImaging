package fr.fresnel.fourPolar.core.util.transform;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Affine2DTest {
    @Test
    public void get_DefaultAffineMatrix_ReturnsCorrectAffineMatrix() {
        Affine2D affine2d = new Affine2D();

        assertArrayEquals(affine2d.get()[0], new double[] { 1, 0, 0 });
        assertArrayEquals(affine2d.get()[1], new double[] { 0, 1, 0 });

    }

    @Test
    public void get_SetElementWise_DoesNotThrowRunTimeException() {
        // To check that after wrapping the setter method of Affine2DTransform, no
        // exception is thrown when matrix becomes singular.
        Affine2D affine2d = new Affine2D();
        assertDoesNotThrow(() -> {
            affine2d.set(0, 0, 1);
        });

    }

    @Test
    public void inInvertible_ZeroDeterminantAndNonzeroDeterminant_ReturnsFalseAndTrueRespectively() {
        Affine2D affine2d_nonInvertible = new Affine2D();
        affine2d_nonInvertible.set(0, 0, 0);
        assertTrue(!affine2d_nonInvertible.isInvertible());
        
        Affine2D affine2d = new Affine2D();
        assertTrue(affine2d.isInvertible());
    }

}