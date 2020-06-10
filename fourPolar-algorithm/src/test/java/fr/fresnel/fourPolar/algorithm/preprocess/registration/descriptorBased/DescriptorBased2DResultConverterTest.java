package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import registration.descriptorBased.result.DescriptorBased2DResult;
import registration.descriptorBased.result.DescriptorBased2DResult.FailureCause;

public class DescriptorBased2DResultConverterTest {
    @Test
    public void convert_ThreeScenariosForEachPol_ReturnsCorrectResult() {
        int channel = 1;

        DescriptorBased2DResult pol45 = new DescriptorBased2DResult();
        pol45.setFailureDescription(FailureCause.NOT_ENOUGH_FP);
        pol45.setIsSuccessful(false);

        DescriptorBased2DResult pol90 = new DescriptorBased2DResult();
        pol90.setFailureDescription(FailureCause.NO_INLIER_AFTER_RANSAC);
        pol90.setIsSuccessful(false);

        DescriptorBased2DResult pol135 = new DescriptorBased2DResult();
        pol135.setIsSuccessful(true);
        pol135.setPercentInliers(0.90);
        pol135.setRegistrationError(0.9);

        DescriptorBased2DResultConverter converter = new DescriptorBased2DResultConverter(channel);

        converter.set(RegistrationRule.Pol45_to_Pol0, pol45);
        converter.set(RegistrationRule.Pol90_to_Pol0, pol90);
        converter.set(RegistrationRule.Pol135_to_Pol0, pol135);

        IChannelRegistrationResult result = converter.convert();

        assertTrue(result.getFailureDescription(RegistrationRule.Pol45_to_Pol0).get()
                .equals(DescriptorBasedChannelRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION));
        assertTrue(result.getFailureDescription(RegistrationRule.Pol90_to_Pol0).get()
                .equals(DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION));
        assertTrue(result.registrationSuccessful(RegistrationRule.Pol135_to_Pol0));

        assertTrue(result.error(RegistrationRule.Pol135_to_Pol0) == 0.9);

    }

}