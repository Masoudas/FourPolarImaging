package fr.fresnel.fourPolar.algorithm.preprocess.darkBackground.estimator.percentile;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PercentileDarkBackgroundUtilTest {
    @Test
    public void computePercentile_returnsCorrectPercentile() {
        assertTrue(PercentileDarkBackgroundUtil.computePercentile(new double[]{1, 2, 3, 4}, 25) == 2);
    }
}