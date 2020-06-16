package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

public class FoVCalculatorTwoCameraTest {
    @Test
    public void calculate_0And45LeftConstellation_ReturnsCorrectFoV() {
        long[] dim = { 512, 512, 10 };
        IMetadata metadata_0_90 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();
        IMetadata metadata_45_135 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint_0_90 = new ShapeFactory().point(new long[] { 250, 250 }, AxisOrder.XY);
        IPointShape intersectionPoint_45_135 = new ShapeFactory().point(new long[] { 270, 255 }, AxisOrder.XY);

        TwoCameraPolarizationConstellation twoCameraConstellation = new TwoCameraPolarizationConstellation(
                Position.Left, Position.Left, Position.Right, Position.Right);
        IFieldOfView fov = new FoVCalculatorTwoCamera(metadata_0_90, intersectionPoint_0_90, metadata_45_135,
                intersectionPoint_45_135, twoCameraConstellation).calculate();

        long x_len = Math.min(
                Math.max(intersectionPoint_0_90.point()[0], dim[0] - 1 - intersectionPoint_0_90.point()[0]),
                Math.max(intersectionPoint_45_135.point()[0], dim[0] - 1 - intersectionPoint_45_135.point()[0]));

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol45));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol90));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol135));
    }

    @Test
    public void calculate_ThreeByThreeImage_ReturnsCorrectFoV() {
        long[] dim = { 3, 3 };
        IMetadata metadata_0_90 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XY).build();
        IMetadata metadata_45_135 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XY).build();

        IPointShape intersectionPoint_0_90 = new ShapeFactory().point(new long[] { 1, 1 }, AxisOrder.XY);
        IPointShape intersectionPoint_45_135 = new ShapeFactory().point(new long[] { 1, 1 }, AxisOrder.XY);

        TwoCameraPolarizationConstellation twoCameraConstellation = new TwoCameraPolarizationConstellation(
                Position.Left, Position.Left, Position.Right, Position.Right);
        IFieldOfView fov = new FoVCalculatorTwoCamera(metadata_0_90, intersectionPoint_0_90, metadata_45_135,
                intersectionPoint_45_135, twoCameraConstellation).calculate();

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { 1, 2 });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { 1, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { 2, 2 });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { 1, 2 });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { 1, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { 2, 2 });

        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol45));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol90));
        this.asserFovEqual(fov.getFoV(Polarization.pol0), fov.getFoV(Polarization.pol135));

    }

    @Test
    public void calculate_0And45RightConstellation_ReturnsCorrectFoV() {
        long[] dim = { 512, 512, 10 };
        IMetadata metadata_0_90 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();
        IMetadata metadata_45_135 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint_0_90 = new ShapeFactory().point(new long[] { 250, 250 }, AxisOrder.XY);
        IPointShape intersectionPoint_45_135 = new ShapeFactory().point(new long[] { 270, 255 }, AxisOrder.XY);

        TwoCameraPolarizationConstellation twoCameraConstellation = new TwoCameraPolarizationConstellation(
                Position.Right, Position.Right, Position.Left, Position.Left);
        IFieldOfView fov = new FoVCalculatorTwoCamera(metadata_0_90, intersectionPoint_0_90, metadata_45_135,
                intersectionPoint_45_135, twoCameraConstellation).calculate();

        long x_len = Math.min(
                Math.max(intersectionPoint_0_90.point()[0], dim[0] - 1 - intersectionPoint_0_90.point()[0]),
                Math.max(intersectionPoint_45_135.point()[0], dim[0] - 1 - intersectionPoint_45_135.point()[0]));

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { dim[0] - 1, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { 0, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { x_len, dim[1] - 1 });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { dim[0] - 1 - x_len, 0 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { dim[0] - 1, dim[1] - 1 });

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