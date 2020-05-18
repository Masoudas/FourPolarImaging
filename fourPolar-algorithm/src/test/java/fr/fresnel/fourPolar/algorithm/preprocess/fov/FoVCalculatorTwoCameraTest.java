package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.Metadata;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraConstellation.Position;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
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

        TwoCameraConstellation twoCameraConstellation = new TwoCameraConstellation(Position.Left, Position.Left,
                Position.Right, Position.Right);
        IFieldOfView fov = new FoVCalculatorTwoCamera(metadata_0_90, intersectionPoint_0_90, metadata_45_135,
                intersectionPoint_45_135, twoCameraConstellation).calculate();

        long x_len = Arrays
                .stream(new long[] { intersectionPoint_0_90.point()[0] - 1, dim[0] - intersectionPoint_0_90.point()[0],
                        intersectionPoint_45_135.point()[0] - 1, dim[0] - intersectionPoint_45_135.point()[0] })
                .summaryStatistics().getMin();

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { x_len, dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { dim[0] - x_len + 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { dim[0], dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { x_len, dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { dim[0] - x_len + 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { dim[0], dim[1] });
    }

    @Test
    public void calculate_0And45RightConstellation_ReturnsCorrectFoV() {
        long[] dim = { 512, 512, 10 };
        IMetadata metadata_0_90 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();
        IMetadata metadata_45_135 = new Metadata.MetadataBuilder(dim).axisOrder(AxisOrder.XYC).build();

        IPointShape intersectionPoint_0_90 = new ShapeFactory().point(new long[] { 250, 250 }, AxisOrder.XY);
        IPointShape intersectionPoint_45_135 = new ShapeFactory().point(new long[] { 270, 255 }, AxisOrder.XY);

        TwoCameraConstellation twoCameraConstellation = new TwoCameraConstellation(Position.Right, Position.Right,
                Position.Left, Position.Left);
        IFieldOfView fov = new FoVCalculatorTwoCamera(metadata_0_90, intersectionPoint_0_90, metadata_45_135,
                intersectionPoint_45_135, twoCameraConstellation).calculate();

        long x_len = Arrays
                .stream(new long[] { intersectionPoint_0_90.point()[0] - 1, dim[0] - intersectionPoint_0_90.point()[0],
                        intersectionPoint_45_135.point()[0] - 1, dim[0] - intersectionPoint_45_135.point()[0] })
                .summaryStatistics().getMin();

        assertArrayEquals(fov.getFoV(Polarization.pol90).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol90).max(), new long[] { x_len, dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol0).min(), new long[] { dim[0] - x_len + 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol0).max(), new long[] { dim[0], dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol135).min(), new long[] { 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol135).max(), new long[] { x_len, dim[1] });

        assertArrayEquals(fov.getFoV(Polarization.pol45).min(), new long[] { dim[0] - x_len + 1, 1 });
        assertArrayEquals(fov.getFoV(Polarization.pol45).max(), new long[] { dim[0], dim[1] });
    }

}