package fr.fresnel.fourPolar.core.util.transform;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class Affine3DTest {
    @Test
    public void get_DefaultAffineMatrix_ReturnsCorrectAffineMatrix() {
        Affine3D affine2d = new Affine3D();

        assertArrayEquals(affine2d.get()[0], new double[] { 1, 0, 0, 0 });
        assertArrayEquals(affine2d.get()[1], new double[] { 0, 1, 0, 0 });
        assertArrayEquals(affine2d.get()[2], new double[] { 0, 0, 1, 0 });

    }

}