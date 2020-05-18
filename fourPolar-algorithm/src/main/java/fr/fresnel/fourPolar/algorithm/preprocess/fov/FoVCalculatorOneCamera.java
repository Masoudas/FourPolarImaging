package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraConstellation.Position;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Calculates the FoV for the one camera case, using the plane dimension of the
 * Captured image together with a {@link IPointShape} which indicates the
 * intersection point of all four polarizations in the image.
 */
public class FoVCalculatorOneCamera implements IFoVCalculator {
    final private long _xmax_beadImg;
    final private long _ymax_beadImg;

    final private long _x_intersection;
    final private long _y_intersection;

    /**
     * Because the image is not intersected perfectly, and we need to have equal
     * size pol images, we define length as minimum of intersected sub-images.
     */
    final private long _xlen_PolImg;
    final private long _ylen_PolImg;

    private final OneCameraConstellation _constellation;

    /**
     * Calculate FoV using the metadata of the bead image.
     * 
     * @param beadImg_pol0_45_90_135 is the metadata of the bead image for one
     *                               camera case.
     * @param intersectionPoint      is the intersection point of polarizations in
     *                               the bead image (starting from [1, 1, 1]).
     * 
     * @throws IllegalArgumentException if the axis order of the @param
     *                                  intersectionPoint is not XY. Also if
     *                                  intersection point is not in the bead image.
     */
    public FoVCalculatorOneCamera(IMetadata beadImg_pol0_45_90_135, IPointShape intersectionPoint,
            OneCameraConstellation constellation) {
        long[] beadImgDim = beadImg_pol0_45_90_135.getDim();
        long[] iPoint = intersectionPoint.point();

        if (beadImgDim[0] <= iPoint[0] || beadImgDim[1] <= iPoint[1]) {
            throw new IllegalArgumentException("Intersection point must be inside the image boundary.");
        }

        _xmax_beadImg = beadImgDim[0];
        _ymax_beadImg = beadImgDim[1];

        _x_intersection = iPoint[0];
        _y_intersection = iPoint[1];

        _xlen_PolImg = Math.min(_x_intersection - 1, _xmax_beadImg - _x_intersection);
        _ylen_PolImg = Math.min(_y_intersection - 1, _ymax_beadImg - _y_intersection);

        _constellation = constellation;
    }

    @Override
    public IFieldOfView calculate() {
        IBoxShape pol0 = _defineFoVAsBox(_constellation.pol0());
        IBoxShape pol45 = _defineFoVAsBox(_constellation.pol45());
        IBoxShape pol90 = _defineFoVAsBox(_constellation.pol90());
        IBoxShape pol135 = _defineFoVAsBox(_constellation.pol135());

        return new FieldOfView(pol0, pol45, pol90, pol135);
    }

    private IBoxShape _defineFoVAsBox(OneCameraConstellation.Position position) {
        long[] bottom = null;
        long[] top = null;
        if (position == Position.TopLeft) {
            bottom = new long[] { 1, 1 };
            top = new long[] { _xlen_PolImg, _ylen_PolImg };
        } else if (position == Position.TopRight) {
            bottom = new long[] { _xmax_beadImg - _xlen_PolImg + 1, 1 };
            top = new long[] { _xmax_beadImg, _ylen_PolImg };
        } else if (position == Position.BottomLeft) {
            bottom = new long[] { 1, _ymax_beadImg - _ylen_PolImg + 1 };
            top = new long[] { _xlen_PolImg, _ymax_beadImg };
        } else {
            bottom = new long[] { _xmax_beadImg - _xlen_PolImg + 1, _ymax_beadImg - _ylen_PolImg + 1 };
            top = new long[] { _xmax_beadImg, _ymax_beadImg };
        }
        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

}