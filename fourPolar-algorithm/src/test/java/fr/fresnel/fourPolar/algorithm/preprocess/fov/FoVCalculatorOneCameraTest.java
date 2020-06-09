package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
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
        OneCameraPolarizationConstellation constellation = new OneCameraPolarizationConstellation(
                OneCameraPolarizationConstellation.Position.TopLeft,
                OneCameraPolarizationConstellation.Position.TopRight,
                OneCameraPolarizationConstellation.Position.BottomLeft,
                OneCameraPolarizationConstellation.Position.BottomRight);

        IFieldOfView fov = new FoVCalculatorOneCamera(beadImageMetadata, intersectionPoint, constellation).calculate();

        long x_len = Math.max(dim[0] - 1 - intersectionPoint.point()[0], intersectionPoint.point()[0]);
        long y_len = Math.max(dim[1] - 1 - intersectionPoint.point()[1], intersectionPoint.point()[1]);

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { x_len, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { dim[0] - 1, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { 0, dim[1] - 1 - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - 1 - x_len, dim[1] - 1 - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol45));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol90));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol135));
    }

    @Test
    public void calculate_ReverseBrasseletConstellation_ReturnsCorrectFoV() {
        long[] dim = { 1024, 512, 10 };
        IMetadata beadImageMetadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint = new ShapeFactory().point(new long[] { 510, 257 }, AxisOrder.XY); // A
                                                                                                         // not-perfect
                                                                                                         // intersection.
        OneCameraPolarizationConstellation constellation = new OneCameraPolarizationConstellation(
                OneCameraPolarizationConstellation.Position.BottomLeft,
                OneCameraPolarizationConstellation.Position.TopLeft,
                OneCameraPolarizationConstellation.Position.BottomRight,
                OneCameraPolarizationConstellation.Position.TopRight);

        IFieldOfView fov = new FoVCalculatorOneCamera(beadImageMetadata, intersectionPoint, constellation).calculate();

        long x_len = Math.max(dim[0] - 1 - intersectionPoint.point()[0], intersectionPoint.point()[0]);
        long y_len = Math.max(dim[1] - 1 - intersectionPoint.point()[1], intersectionPoint.point()[1]);

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { x_len, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] - 1, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 0, dim[1] - 1 - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { dim[0] - 1 - x_len, dim[1] - 1 - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol45));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol90));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol135));
    }

    @Test
    public void calculate_2By2Image_ReturnsCorrectFoV() {
        long[] dim = { 2, 2 };
        IMetadata beadImageMetadata = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XY).build();

        IPointShape intersectionPoint = new ShapeFactory().point(new long[] { 0, 0 }, AxisOrder.XY);
        OneCameraPolarizationConstellation constellation = new OneCameraPolarizationConstellation(
                OneCameraPolarizationConstellation.Position.TopLeft,
                OneCameraPolarizationConstellation.Position.TopRight,
                OneCameraPolarizationConstellation.Position.BottomLeft,
                OneCameraPolarizationConstellation.Position.BottomRight);

        IFieldOfView fov = new FoVCalculatorOneCamera(beadImageMetadata, intersectionPoint, constellation).calculate();

        long x_len = Math.max(dim[0] - 1 - intersectionPoint.point()[0], intersectionPoint.point()[0]);
        long y_len = Math.max(dim[1] - 1 - intersectionPoint.point()[1], intersectionPoint.point()[1]);

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { x_len, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] - 1, y_len });

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 0, dim[1] - 1 - y_len });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { dim[0] - x_len - 1, dim[1] - y_len - 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol45));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol90));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol135));

    }

    private void asserFovEqual(IBoxShape shape1, IBoxShape shape2) {
        assertArrayEquals(
                IntStream.range(0, shape1.min().length).mapToLong((i) -> shape1.max()[i] - shape1.min()[i]).toArray(),
                IntStream.range(0, shape2.min().length).mapToLong((i) -> shape2.max()[i] - shape2.min()[i]).toArray());

    }
}