package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import mpicbg.models.AffineModel2D;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

public class DescriptorBased2DResultConverterTest {
    @Test
    public void convertFailureCauseToString_DescriptorBasedFailureCauses_ReturnsCorrectFailureCauseString() {
        String noEnoughtFP = DescriptorBased2DResultConverter
                .convertFailureCauseToString(DescriptorBased2DResult.FailureCause.NOT_ENOUGH_FP);

        assertTrue(noEnoughtFP.equals(DescriptorBased2DResultConverter._NOT_ENOUGH_FP_DESCRIPTION));

        String noInlierAfterRansac = DescriptorBased2DResultConverter
                .convertFailureCauseToString(DescriptorBased2DResult.FailureCause.NO_INLIER_AFTER_RANSAC);

        assertTrue(noInlierAfterRansac.equals(DescriptorBased2DResultConverter._NO_TRANSFORMATION_DESCRIPTION));

        String noInvertibleTransform = DescriptorBased2DResultConverter
                .convertFailureCauseToString(DescriptorBased2DResult.FailureCause.NO_INVERTIBLE_TRANSFORMATION);

        assertTrue(noInvertibleTransform.equals(DescriptorBased2DResultConverter._NO_TRANSFORMATION_DESCRIPTION));

    }

    @Test
    public void convert_ThreeScenariosForEachPol_ReturnsCorrectResult() {
        int channel = 1;

        AffineModel2D pol45Affine = new AffineModel2D();
        pol45Affine.set(1, 0, 0, 1, 0, 0);
        DescriptorBased2DResult pol45 = new DescriptorBased2DResult();
        pol45.setIsSuccessful(true);    // Just to indicate success
        pol45.setAffineTransfrom(pol45Affine);
        pol45.setRegistrationError(0.9);

        AffineModel2D pol90Affine = new AffineModel2D();
        pol90Affine.set(2, 0, 0, 2, 0, 0);
        DescriptorBased2DResult pol90 = new DescriptorBased2DResult();
        pol90.setIsSuccessful(true);
        pol90.setAffineTransfrom(pol90Affine);
        pol90.setRegistrationError(0.1);

        AffineModel2D pol135Affine = new AffineModel2D();
        pol135Affine.set(3, 0, 0, 3, 0, 0);
        DescriptorBased2DResult pol135 = new DescriptorBased2DResult();
        pol135.setIsSuccessful(true);
        pol135.setAffineTransfrom(pol135Affine);
        pol135.setRegistrationError(0.9);

        DescriptorBased2DResultConverter converter = new DescriptorBased2DResultConverter(channel)
                .set(RegistrationRule.Pol45_to_Pol0, pol45).set(RegistrationRule.Pol90_to_Pol0, pol90)
                .set(RegistrationRule.Pol135_to_Pol0, pol135);

        IChannelRegistrationResult result = converter.convert();

        // Pol45
        assertTrue(result.getAffineTransform(RegistrationRule.Pol45_to_Pol0).get(0, 0) == 1);
        assertTrue(result.error(RegistrationRule.Pol45_to_Pol0) == 0.9);

        // Pol90
        assertTrue(result.getAffineTransform(RegistrationRule.Pol90_to_Pol0).get(0, 0) == 2);
        assertTrue(result.error(RegistrationRule.Pol90_to_Pol0) == 0.1);

        // Pol135
        assertTrue(result.getAffineTransform(RegistrationRule.Pol135_to_Pol0).get(0, 0) == 3);
        assertTrue(result.error(RegistrationRule.Pol135_to_Pol0) == 0.9);

    }

}