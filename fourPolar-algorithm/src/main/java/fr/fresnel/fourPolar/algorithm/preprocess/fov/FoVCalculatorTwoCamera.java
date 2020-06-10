package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.image.generic.metadata.MetadataUtil;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.TwoCameraPolarizationConstellation.Position;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Calculates the FoV for the two camera case, using the plane dimension of the
 * Captured images together with two {@link IPointShape} for each registration
 * image, which indicates the intersection point of the two polarizations in
 * each image.
 */
public class FoVCalculatorTwoCamera implements IFoVCalculator {
    final private long _x_min_two_images;
    final private long _y_min_two_images;

    final private long[] _intersection_pol0_90;
    final private long[] _intersection_pol45_135;

    /**
     * Because the image is not intersected perfectly, and we need to have equal
     * size pol images, we define length as minimum of intersected sub-images.
     */
    final private long _xlen_PolImg;
    final private long _ylen_PolImg;

    final private TwoCameraPolarizationConstellation _constellation;

    /**
     * Calculate FoV using the metadata of the registration images of each camera.
     * FoV is calcualted with two {@link IPointShape}, which indicates the
     * intersection point of two polarizations in each image. The maximum length
     * from intersection to each side of the image is considered as FoV.
     * 
     * @param registrationImg_pol0_90
     * @param intersection_pol0_90
     * @param registrationImg_pol45_135
     * @param intersection_pol45_135
     * @param constellation
     */
    public FoVCalculatorTwoCamera(IMetadata registrationImg_pol0_90, IPointShape intersection_pol0_90,
            IMetadata registrationImg_pol45_135, IPointShape intersection_pol45_135,
            TwoCameraPolarizationConstellation constellation) {
        FoVCalculatorUtil.checkIntersectionPointIs2D(intersection_pol0_90);
        FoVCalculatorUtil.checkIntersectionPointIs2D(intersection_pol45_135);

        // TODO Use Box to indicate region.
        this._checkIntersectionPointInside(registrationImg_pol0_90, intersection_pol0_90);
        this._checkIntersectionPointInside(registrationImg_pol45_135, intersection_pol45_135);

        this._x_min_two_images = this._getMinimumAlongDim(0, registrationImg_pol0_90, registrationImg_pol45_135);
        this._y_min_two_images = this._getMinimumAlongDim(1, registrationImg_pol0_90, registrationImg_pol45_135);

        this._intersection_pol0_90 = intersection_pol0_90.point();
        this._intersection_pol45_135 = intersection_pol45_135.point();

        this._xlen_PolImg = _calculate_xlen(registrationImg_pol0_90, registrationImg_pol45_135);
        this._ylen_PolImg = _calculate_ylen();

        this._constellation = constellation;
    }

    private void _checkIntersectionPointInside(IMetadata metadata, IPointShape intersection) {
        long[] dim = metadata.getDim();
        long[] iPoint = intersection.point();

        if (dim[0] <= iPoint[0] || dim[1] <= iPoint[1]) {
            throw new IllegalArgumentException("Intersection point must be inside the image boundary.");
        }

        if (iPoint[0] < 0 || iPoint[1] < 0) {
            throw new IllegalArgumentException("Intersection point must be greater than equal zero.");
        }

    }

    private long _getMinimumAlongDim(int d, IMetadata registrationImg_pol0_90, IMetadata registrationImg_pol45_135) {
        assert d >= 0 && d < 2 : "Dimension has to be 0 and 1";

        return Math.min(MetadataUtil.getImageLastPixel(registrationImg_pol0_90)[d],
                MetadataUtil.getImageLastPixel(registrationImg_pol45_135)[d]);
    }

    /**
     * x len is the minimum of the maximum length from the intersection of both
     * images (to accommodate for difference in image sizes).
     */
    private long _calculate_xlen(IMetadata registrationImg_pol0_90, IMetadata registrationImg_pol45_135) {
        long x_pol_0_90 = MetadataUtil.getImageLastPixel(registrationImg_pol0_90)[0];
        long x_pol_45_135 = MetadataUtil.getImageLastPixel(registrationImg_pol45_135)[0];
        return Math.min(Math.max(_intersection_pol0_90[0], x_pol_0_90 - _intersection_pol0_90[0]),
                Math.max(_intersection_pol45_135[0], x_pol_45_135 - _intersection_pol45_135[0]));
    }

    /**
     * y max is the minimum of the two images.
     */
    private long _calculate_ylen() {
        return _y_min_two_images;
    }

    @Override
    public IFieldOfView calculate() {
        IBoxShape pol0 = _defineFoVAsBox(_constellation.pol0(), _x_min_two_images);
        IBoxShape pol45 = _defineFoVAsBox(_constellation.pol45(), _x_min_two_images);
        IBoxShape pol90 = _defineFoVAsBox(_constellation.pol90(), _x_min_two_images);
        IBoxShape pol135 = _defineFoVAsBox(_constellation.pol135(), _x_min_two_images);

        return new FieldOfView(pol0, pol45, pol90, pol135);
    }

    private IBoxShape _defineFoVAsBox(Position position, long _xmax_registrationImg) {
        long[] bottom = null;
        long[] top = null;

        if (position == Position.Left) {
            bottom = new long[] { 0, 0 };
            top = new long[] { _xlen_PolImg, _ylen_PolImg };
        } else {
            bottom = new long[] { _xmax_registrationImg - _xlen_PolImg, 0 };
            top = new long[] { _xmax_registrationImg, _ylen_PolImg };

        }

        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

}