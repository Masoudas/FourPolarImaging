package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.OneCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Calculates the FoV for the one camera case, using the plane dimension of the
 * Captured image.
 */
public class FoVCalculatorByIntersectionPointOneCamera implements IFoVCalculatorByIntersectionPoint {
    final private long _xmax_registrationImg;
    final private long _ymax_registrationImg;

    final private long _x_intersection;
    final private long _y_intersection;

    /**
     * Because the image is not intersected perfectly, and we need to have equal
     * size pol images, we define length as minimum of intersected sub-images.
     */
    final private long _xlen_PolImg;
    final private long _ylen_PolImg;

    private final OneCameraPolarizationConstellation _constellation;

    /**
     * Calculate FoV using the metadata of the registration image. FoV is calcualted
     * with a {@link IPointShape} which indicates the intersection point of all four
     * polarizations in the image. The maximum length from intersection to each side
     * of the image is considered as FoV.
     * 
     * @param registrationImg_pol0_45_90_135 is the metadata of the registration
     *                                       image for one camera case.
     * @param intersectionPoint              is the intersection point of
     *                                       polarizations in the registration image
     *                                       (starting from [1, 1, 1]).
     * 
     * @throws IllegalArgumentException if the axis order of the @param
     *                                  intersectionPoint is not XY. Also if
     *                                  intersection point is not in the
     *                                  registration image.
     */
    public FoVCalculatorByIntersectionPointOneCamera(IMetadata registrationImg_pol0_45_90_135, IPointShape intersectionPoint,
            OneCameraPolarizationConstellation constellation) {
        FoVCalculatorUtil.checkIntersectionPointIs2D(intersectionPoint);
        long[] registrationImgDim = MetadataUtil.getImageLastPixel(registrationImg_pol0_45_90_135);
        long[] iPoint = intersectionPoint.point();

        if (registrationImgDim[0] <= iPoint[0] || registrationImgDim[1] <= iPoint[1]) {
            throw new IllegalArgumentException("Intersection point must be inside the image boundary.");
        }

        if (iPoint[0] < 0 || iPoint[1] < 0) {
            throw new IllegalArgumentException("Intersection point must be greater than equal zero.");
        }

        _xmax_registrationImg = registrationImgDim[0];
        _ymax_registrationImg = registrationImgDim[1];

        _x_intersection = iPoint[0];
        _y_intersection = iPoint[1];

        _xlen_PolImg = Math.max(_x_intersection, _xmax_registrationImg - _x_intersection);
        _ylen_PolImg = Math.max(_y_intersection, _ymax_registrationImg - _y_intersection);

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

    private IBoxShape _defineFoVAsBox(OneCameraPolarizationConstellation.Position position) {
        long[] bottom = null;
        long[] top = null;
        if (position == Position.TopLeft) {
            bottom = new long[] { 0, 0 };
            top = new long[] { _xlen_PolImg, _ylen_PolImg };
        } else if (position == Position.TopRight) {
            bottom = new long[] { _xmax_registrationImg - _xlen_PolImg, 0 };
            top = new long[] { _xmax_registrationImg, _ylen_PolImg };
        } else if (position == Position.BottomLeft) {
            bottom = new long[] { 0, _ymax_registrationImg - _ylen_PolImg };
            top = new long[] { _xlen_PolImg, _ymax_registrationImg };
        } else {
            bottom = new long[] { _xmax_registrationImg - _xlen_PolImg, _ymax_registrationImg - _ylen_PolImg };
            top = new long[] { _xmax_registrationImg, _ymax_registrationImg };
        }
        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

}