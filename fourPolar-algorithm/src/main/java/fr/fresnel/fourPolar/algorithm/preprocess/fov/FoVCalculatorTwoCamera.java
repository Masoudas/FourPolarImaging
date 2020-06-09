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
    final private long[] _laxtPixel_pol0_90;
    final private long[] _lixtPixel_pol45_135;

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
        // TODO Use Box to indicate region.
        this._checkIntersectionPointInside(registrationImg_pol0_90, intersection_pol0_90);
        this._checkIntersectionPointInside(registrationImg_pol45_135, intersection_pol45_135);

        this._laxtPixel_pol0_90 = MetadataUtil.getImageLastPixel(registrationImg_pol0_90);
        this._lixtPixel_pol45_135 = MetadataUtil.getImageLastPixel(registrationImg_pol45_135);

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

        if (iPoint[0] <= 0 || iPoint[1] <= 0) {
            throw new IllegalArgumentException("Intersection point must be greater than equal zero.");
        }

    }

    /**
     * x len is maximum length from the intersection of both images.
     */
    private long _calculate_xlen() {
        long[] _xlen = { _intersection_pol0_90[0], _laxtPixel_pol0_90[0] - _intersection_pol0_90[0],
                _intersection_pol45_135[0], _lixtPixel_pol45_135[0] - _intersection_pol45_135[0] };
        return Arrays.stream(_xlen).summaryStatistics().getMax();
    }

    /**
     * y max is the minimum of the two images.
     */
    private long _calculate_ylen() {
        return Math.max(_laxtPixel_pol0_90[1], _lixtPixel_pol45_135[1]);
    }

    @Override
    public IFieldOfView calculate() {
        IBoxShape pol0 = _defineFoVAsBox(_constellation.pol0(), _laxtPixel_pol0_90[0]);
        IBoxShape pol45 = _defineFoVAsBox(_constellation.pol45(), _lixtPixel_pol45_135[0]);
        IBoxShape pol90 = _defineFoVAsBox(_constellation.pol90(), _laxtPixel_pol0_90[0]);
        IBoxShape pol135 = _defineFoVAsBox(_constellation.pol135(), _lixtPixel_pol45_135[0]);

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