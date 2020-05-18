package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraConstellation.Position;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Calculates the FoV for the two camera case, using the plane dimension of the
 * Captured images together with two {@link IPointShape} for each bead image,
 * which indicates the intersection point of the two polarizations in each
 * image.
 */
public class FoVCalculatorTwoCamera implements IFoVCalculator {
    final private long[] _dim_pol0_90;
    final private long[] _dim_pol45_135;

    final private long[] _intersection_pol0_90;
    final private long[] _intersection_pol45_135;

    /**
     * Because the image is not intersected perfectly, and we need to have equal
     * size pol images, we define length as minimum of intersected sub-images.
     */
    final private long _xlen_PolImg;
    final private long _ylen_PolImg;

    final private TwoCameraConstellation _constellation;

    public FoVCalculatorTwoCamera(IMetadata beadImg_pol0_90, IPointShape intersection_pol0_90,
            IMetadata beadImg_pol45_135, IPointShape intersection_pol45_135, TwoCameraConstellation constellation) {
        this._checkIntersectionPointInside(beadImg_pol0_90, intersection_pol0_90);
        this._checkIntersectionPointInside(beadImg_pol45_135, intersection_pol45_135);

        this._dim_pol0_90 = beadImg_pol0_90.getDim();
        this._dim_pol45_135 = beadImg_pol45_135.getDim();

        this._intersection_pol0_90 = intersection_pol0_90.point();
        this._intersection_pol45_135 = intersection_pol45_135.point();

        this._xlen_PolImg = _calculate_xlen();
        this._ylen_PolImg = _calculate_ylen();

        this._constellation = constellation;
    }

    private void _checkIntersectionPointInside(IMetadata metadata, IPointShape intersection) {
        long[] dim = metadata.getDim();
        long[] iPoint = intersection.point();

        if (dim[0] <= iPoint[0] || dim[1] <= iPoint[1]) {
            throw new IllegalArgumentException("Intersection point must be inside the image boundary.");
        }
    }

    /**
     * x min is minimum length from the intersection of both images.
     */
    private long _calculate_xlen() {
        long[] _xlen = { _intersection_pol0_90[0] - 1, _dim_pol0_90[0] - _intersection_pol0_90[0],
                _intersection_pol45_135[0] - 1, _dim_pol45_135[0] - _intersection_pol45_135[0] };
        return Arrays.stream(_xlen).summaryStatistics().getMin();
    }

    /**
     * y min is the minimum of the two images.
     */
    private long _calculate_ylen() {
        return Math.min(_dim_pol0_90[1], _dim_pol45_135[1]);
    }

    @Override
    public IFieldOfView calculate() {
        IBoxShape pol0 = _defineFoVAsBox(_constellation.pol0(), _dim_pol0_90[0]);
        IBoxShape pol45 = _defineFoVAsBox(_constellation.pol45(), _dim_pol45_135[0]);
        IBoxShape pol90 = _defineFoVAsBox(_constellation.pol90(), _dim_pol0_90[0]);
        IBoxShape pol135 = _defineFoVAsBox(_constellation.pol135(), _dim_pol45_135[0]);

        return new FieldOfView(pol0, pol45, pol90, pol135);
    }

    private IBoxShape _defineFoVAsBox(Position position, long _xmax_beadImg) {
        long[] bottom = null;
        long[] top = null;

        if (position == Position.Left) {
            bottom = new long[] { 1, 1 };
            top = new long[] { _xlen_PolImg, _ylen_PolImg};
        } else {
            bottom = new long[] { _xmax_beadImg - _xlen_PolImg + 1, 1 };
            top = new long[] { _xmax_beadImg, _ylen_PolImg};

        }

        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }



}