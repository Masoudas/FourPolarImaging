package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraConstellation;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class FoVCalculatorOneCameraTest {
    @Test
    public void calculate_BrasseletConstellation_ReturnsCorrectFoV() {
        long[] dim = { 1024, 512, 10 };
        IMetadata beadImageMetadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint = new ShapeFactory().point(new long[] { 510, 257 }, AxisOrder.XY); // A
                                                                                                         // not-perfect
                                                                                                         // intersection.
        OneCameraConstellation constellation = new OneCameraConstellation(OneCameraConstellation.Position.TopLeft,
                OneCameraConstellation.Position.TopRight, OneCameraConstellation.Position.BottomLeft,
                OneCameraConstellation.Position.BottomRight);

        IFieldOfView fov = new FoVCalculatorOneCamera(beadImageMetadata, intersectionPoint, constellation).calculate();

        long x_len = Math.min(dim[0] - intersectionPoint.point()[0], intersectionPoint.point()[0] - 1);
        long y_len = Math.min(dim[1] - intersectionPoint.point()[1], intersectionPoint.point()[1] - 1);

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { 1 + x_len , 1 + y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { dim[0] - x_len, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { dim[0] , 1 + y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { 1, dim[1] - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { 1 + x_len , dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - x_len, dim[1] - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] , dim[1] });

    }

    @Test
    public void calculate_ReverseBrasseletConstellation_ReturnsCorrectFoV() {
        long[] dim = { 1024, 512, 10 };
        IMetadata beadImageMetadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint = new ShapeFactory().point(new long[] { 510, 257 }, AxisOrder.XY); // A
                                                                                                         // not-perfect
                                                                                                         // intersection.
        OneCameraConstellation constellation = new OneCameraConstellation(OneCameraConstellation.Position.BottomLeft,
                OneCameraConstellation.Position.TopLeft, OneCameraConstellation.Position.BottomRight,
                OneCameraConstellation.Position.TopRight);

        IFieldOfView fov = new FoVCalculatorOneCamera(beadImageMetadata, intersectionPoint, constellation).calculate();

        long x_len = Math.min(dim[0] - intersectionPoint.point()[0], intersectionPoint.point()[0] - 1);
        long y_len = Math.min(dim[1] - intersectionPoint.point()[1], intersectionPoint.point()[1] - 1);

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { 1 + x_len , 1 + y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - x_len, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] , 1 + y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 1, dim[1] - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { 1 + x_len , dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { dim[0] - x_len, dim[1] - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { dim[0] , dim[1] });

    }

}