package fr.fresnel.fourPolar.algorithm.preprocess.registration.descriptorBased;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.preprocess.registration.IChannelRegistrationResult;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationOrder;
import ij.ImagePlus;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.ARGBType;
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

        long[] resultImageDim = { 2, 2 };
        ImagePlus img = ImageJFunctions.wrap(new CellImgFactory<ARGBType>(new ARGBType()).create(resultImageDim), "");
        DescriptorBased2DResult pol135 = new DescriptorBased2DResult();
        pol135.setIsSuccessful(true);
        pol135.setResultingCompositeImage(img);
        pol135.setPercentInliers(0.90);
        pol135.setRegistrationError(0.9);

        DescriptorBased2DResultConverter converter = new DescriptorBased2DResultConverter(channel);

        converter.set(RegistrationOrder.Pol45_to_Pol0, pol45);
        converter.set(RegistrationOrder.Pol90_to_Pol0, pol90);
        converter.set(RegistrationOrder.Pol135_to_Pol0, pol135);

        IChannelRegistrationResult result = converter.convert();

        assertTrue(result.getDescription(RegistrationOrder.Pol45_to_Pol0)
                .equals(DescriptorBasedChannelRegistrationResult._NOT_ENOUGH_FP_DESCRIPTION));
        assertTrue(result.getDescription(RegistrationOrder.Pol90_to_Pol0)
                .equals(DescriptorBasedChannelRegistrationResult._NO_TRANSFORMATION_DESCRIPTION));
        assertTrue(result.registrationSuccessful(RegistrationOrder.Pol135_to_Pol0));

        assertTrue(result.error(RegistrationOrder.Pol135_to_Pol0) == 0.9);

    }

}