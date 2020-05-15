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
     * @param beadImageMetadata is the metadata of the bead image for one camera
     *                          case.
     * @param intersectionPoint is the intersection point of polarizations in the
     *                          bead image.
     * 
     * @throws IllegalArgumentException if the axis order of the @param
     *                                  intersectionPoint is not XY.
     */
    public FoVCalculatorOneCamera(IMetadata beadImageMetadata, IPointShape intersectionPoint,
            OneCameraConstellation constellation) {
        long[] beadImgDim = beadImageMetadata.getDim();
        long[] iPoint = intersectionPoint.point();

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
        IBoxShape pol0 = _defineFoVAsBox(_constellation.pol0);
        IBoxShape pol45 = _defineFoVAsBox(_constellation.pol45);
        IBoxShape pol90 = _defineFoVAsBox(_constellation.pol90);
        IBoxShape pol135 = _defineFoVAsBox(_constellation.pol135);

        return new FieldOfView(pol0, pol45, pol90, pol135);
    }

    private IBoxShape _defineFoVAsBox(OneCameraConstellation.Position position) {
        long[] bottom = _getBottom(position);
        long[] top = _getTop(bottom);
        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

    private long[] _getBottom(OneCameraConstellation.Position position) {
        if (position == Position.TopLeft) {
            return new long[] { 1, 1 };
        } else if (position == Position.TopRight) {
            return new long[] { _xmax_beadImg - _xlen_PolImg, 1 };
        } else if (position == Position.BottomLeft) {
            return new long[] { 1, _ymax_beadImg - _ylen_PolImg };
        } else {
            return new long[] { _xmax_beadImg - _xlen_PolImg, _ymax_beadImg - _ylen_PolImg };
        }
    }

    private long[] _getTop(long[] bottom) {
        return new long[] { bottom[0] + _xlen_PolImg, bottom[0] + _ylen_PolImg };
    }
}