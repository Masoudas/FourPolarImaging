package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.shape.IBoxShape;

public class FoVCalculatorTest {
    @Test
    public void calculate_OneCameraFoVsInsideImageDim_ReturnsSameFoV() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();

        long[] minPoint_pol0 = new long[] { 0, 0 };
        long[] maxPoint_pol0 = new long[] { 1, 1 };

        long[] minPoint_pol45 = new long[] { 3, 0 };
        long[] maxPoint_pol45 = new long[] { 4, 1 };

        long[] minPoint_pol90 = new long[] { 0, 3 };
        long[] maxPoint_pol90 = new long[] { 1, 4 };

        long[] minPoint_pol135 = new long[] { 3, 3 };
        long[] maxPoint_pol135 = new long[] { 4, 4 };

        IFoVCalculator calculator = FoVCalculator.oneCamera(metadata);

        calculator.setMin(minPoint_pol0[0], minPoint_pol0[1], Polarization.pol0);
        calculator.setMax(maxPoint_pol0[0], maxPoint_pol0[1], Polarization.pol0);

        calculator.setMin(minPoint_pol45[0], minPoint_pol45[1], Polarization.pol45);
        calculator.setMax(maxPoint_pol45[0], maxPoint_pol45[1], Polarization.pol45);

        calculator.setMin(minPoint_pol90[0], minPoint_pol90[1], Polarization.pol90);
        calculator.setMax(maxPoint_pol90[0], maxPoint_pol90[1], Polarization.pol90);

        calculator.setMin(minPoint_pol135[0], minPoint_pol135[1], Polarization.pol135);
        calculator.setMax(maxPoint_pol135[0], maxPoint_pol135[1], Polarization.pol135);

        IFieldOfView fov = calculator.calculate();
        assertFoVEqual(fov.getFoV(Polarization.pol0), minPoint_pol0, maxPoint_pol0);
        assertFoVEqual(fov.getFoV(Polarization.pol45), minPoint_pol45, maxPoint_pol45);
        assertFoVEqual(fov.getFoV(Polarization.pol90), minPoint_pol90, maxPoint_pol90);
        assertFoVEqual(fov.getFoV(Polarization.pol135), minPoint_pol135, maxPoint_pol135);

    }

    @Test
    public void calculate_TwoCameraFoVsInsideImageDim_ReturnsSameFoV() {
        IMetadata metadata1 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();
        IMetadata metadata2 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();

        long[] minPoint_pol0 = new long[] { 0, 0 };
        long[] maxPoint_pol0 = new long[] { 1, 4 };

        long[] minPoint_pol45 = new long[] { 3, 0 };
        long[] maxPoint_pol45 = new long[] { 4, 4 };

        long[] minPoint_pol90 = new long[] { 0, 0 };
        long[] maxPoint_pol90 = new long[] { 1, 4 };

        long[] minPoint_pol135 = new long[] { 3, 0 };
        long[] maxPoint_pol135 = new long[] { 4, 4 };

        IFoVCalculator calculator = FoVCalculator.twoCamera(metadata1, metadata2);

        calculator.setMin(minPoint_pol0[0], minPoint_pol0[1], Polarization.pol0);
        calculator.setMax(maxPoint_pol0[0], maxPoint_pol0[1], Polarization.pol0);

        calculator.setMin(minPoint_pol45[0], minPoint_pol45[1], Polarization.pol45);
        calculator.setMax(maxPoint_pol45[0], maxPoint_pol45[1], Polarization.pol45);

        calculator.setMin(minPoint_pol90[0], minPoint_pol90[1], Polarization.pol90);
        calculator.setMax(maxPoint_pol90[0], maxPoint_pol90[1], Polarization.pol90);

        calculator.setMin(minPoint_pol135[0], minPoint_pol135[1], Polarization.pol135);
        calculator.setMax(maxPoint_pol135[0], maxPoint_pol135[1], Polarization.pol135);

        IFieldOfView fov = calculator.calculate();
        assertFoVEqual(fov.getFoV(Polarization.pol0), minPoint_pol0, maxPoint_pol0);
        assertFoVEqual(fov.getFoV(Polarization.pol45), minPoint_pol45, maxPoint_pol45);
        assertFoVEqual(fov.getFoV(Polarization.pol90), minPoint_pol90, maxPoint_pol90);
        assertFoVEqual(fov.getFoV(Polarization.pol135), minPoint_pol135, maxPoint_pol135);

    }

    @Test
    public void calculate_FourCameraFoVsInsideImageDim_ReturnsSameFoV() {
        IMetadata metadata1 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();
        IMetadata metadata2 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();
        IMetadata metadata3 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();
        IMetadata metadata4 = new Metadata.MetadataBuilder(new long[] { 5, 5, 1, 2 }).build();

        long[] minPoint_pol0 = new long[] { 0, 0 };
        long[] maxPoint_pol0 = new long[] { 4, 4 };

        long[] minPoint_pol45 = new long[] { 0, 0 };
        long[] maxPoint_pol45 = new long[] { 4, 4 };

        long[] minPoint_pol90 = new long[] { 0, 0 };
        long[] maxPoint_pol90 = new long[] { 4, 4 };

        long[] minPoint_pol135 = new long[] { 0, 0 };
        long[] maxPoint_pol135 = new long[] { 4, 4 };

        IFoVCalculator calculator = FoVCalculator.fourCamera(metadata1, metadata2, metadata3, metadata4);

        calculator.setMin(minPoint_pol0[0], minPoint_pol0[1], Polarization.pol0);
        calculator.setMax(maxPoint_pol0[0], maxPoint_pol0[1], Polarization.pol0);

        calculator.setMin(minPoint_pol45[0], minPoint_pol45[1], Polarization.pol45);
        calculator.setMax(maxPoint_pol45[0], maxPoint_pol45[1], Polarization.pol45);

        calculator.setMin(minPoint_pol90[0], minPoint_pol90[1], Polarization.pol90);
        calculator.setMax(maxPoint_pol90[0], maxPoint_pol90[1], Polarization.pol90);

        calculator.setMin(minPoint_pol135[0], minPoint_pol135[1], Polarization.pol135);
        calculator.setMax(maxPoint_pol135[0], maxPoint_pol135[1], Polarization.pol135);

        IFieldOfView fov = calculator.calculate();
        assertFoVEqual(fov.getFoV(Polarization.pol0), minPoint_pol0, maxPoint_pol0);
        assertFoVEqual(fov.getFoV(Polarization.pol45), minPoint_pol45, maxPoint_pol45);
        assertFoVEqual(fov.getFoV(Polarization.pol90), minPoint_pol90, maxPoint_pol90);
        assertFoVEqual(fov.getFoV(Polarization.pol135), minPoint_pol135, maxPoint_pol135);

    }

    private void assertFoVEqual(IBoxShape fov, long[] min, long[] max) {
        assertArrayEquals(fov.min(), min);
        assertArrayEquals(fov.max(), max);

    }

}