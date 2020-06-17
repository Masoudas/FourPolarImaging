package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class FoVCalculatorOneCameraTest {
    @Test
    public void calculate_FoVsInsideImageDim_ReturnsSameFoV() {
        IMetadata metadata = new Metadata.MetadataBuilder(new long[]{5, 5, 1, 2}).build();
        
        FoVCalculatorOneCamera calculator = new FoVCalculatorOneCamera(metadata);

        ShapeFactory shapeFactory = new ShapeFactory();
        IBoxShape pol0 = shapeFactory.closedBox(new long[]{0,0}, new long[]{1,1}, AxisOrder.XY);
        IBoxShape pol45 = shapeFactory.closedBox(new long[]{3,0}, new long[]{4,1}, AxisOrder.XY);
        IBoxShape pol90 = shapeFactory.closedBox(new long[]{0,3}, new long[]{1,4}, AxisOrder.XY);
        IBoxShape pol135 = shapeFactory.closedBox(new long[]{3,3}, new long[]{4,4}, AxisOrder.XY);

        IFieldOfView fov = calculator.calculate(pol0, pol45, pol90, pol135);

        assertFoVEqual(pol0, fov.getFoV(Polarization.pol0));
        assertFoVEqual(pol45, fov.getFoV(Polarization.pol45));
        assertFoVEqual(pol90, fov.getFoV(Polarization.pol90));
        assertFoVEqual(pol135, fov.getFoV(Polarization.pol135));
    }

    private void assertFoVEqual(IBoxShape fov, IBoxShape calculatedFoV) {
        assertArrayEquals(fov.min(), calculatedFoV.min());
        assertArrayEquals(fov.max(), calculatedFoV.max());
        
    }
    
}