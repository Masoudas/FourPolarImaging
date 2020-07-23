package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import java.util.HashMap;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Creates a fov calculator for the given number of cameras
 */
public class FoVCalculator implements IFoVCalculator {
    /**
     * Create a calculator for {@link Cameras#One}.
     * 
     * @param pol0_45_90_135 is the metadata of an image captured by this camera
     *                       (preferably a registration image).
     * @return field of view.
     */
    public static IFoVCalculator oneCamera(IMetadata pol0_45_90_135) {
        return new FoVCalculator(pol0_45_90_135, pol0_45_90_135, pol0_45_90_135, pol0_45_90_135);
    }

    /**
     * Create a calculator for {@link Cameras#Two}.
     * 
     * @param pol0_90   is the metadata of an image captured by the camera capturing
     *                  pol0 and pol90 (preferably a registration image).
     * @param pol45_135 is the metadata of an image captured by the camera capturing
     *                  pol45 and pol135 (preferably a registration image).
     * 
     * @return field of view.
     */
    public static IFoVCalculator twoCamera(IMetadata pol0_90, IMetadata pol45_135) {
        return new FoVCalculator(pol0_90, pol45_135, pol0_90, pol45_135);
    }

    /**
     * Create a calculator for {@link Cameras#Four}.
     * 
     * @param pol0   is the metadata of an image captured by the camera capturing
     *               pol0 (preferably a registration image).
     * @param pol45  is the metadata of an image captured by the camera capturing
     *               pol45 (preferably a registration image).
     * @param pol90  is the metadata of an image captured by the camera capturing
     *               pol90 (preferably a registration image).
     * @param pol135 is the metadata of an image captured by the camera capturing
     *               pol135 (preferably a registration image).
     * 
     * @return field of view.
     */
    public static IFoVCalculator fourCamera(IMetadata pol0, IMetadata pol45, IMetadata pol90, IMetadata pol135) {
        return new FoVCalculator(pol0, pol45, pol90, pol135);
    }

    private final HashMap<Polarization, IMetadata> _metadatas = new HashMap<>();
    private final HashMap<Polarization, long[]> _minPoints;
    private final HashMap<Polarization, long[]> _maxPoints;

    /**
     * Create the calculator, using the metadata associated with the image that
     * contains the polarization. Note that these metadata can be repetitive, for
     * example for the {@link Cameras#One} case.
     */
    private FoVCalculator(IMetadata pol0, IMetadata pol45, IMetadata pol90, IMetadata pol135) {
        this._metadatas.put(Polarization.pol0, pol0);
        this._metadatas.put(Polarization.pol45, pol45);
        this._metadatas.put(Polarization.pol90, pol90);
        this._metadatas.put(Polarization.pol135, pol135);

        _minPoints = new HashMap<>();
        _maxPoints = new HashMap<>();
    }

    @Override
    public void setMin(long x, long y, Polarization pol) {
        _checkPointIsInImageBoundary(this._metadatas.get(pol), x, y);
        _minPoints.put(pol, new long[] { x, y });
    }

    @Override
    public void setMax(long x, long y, Polarization pol) {
        _checkPointIsInImageBoundary(this._metadatas.get(pol), x, y);
        _maxPoints.put(pol, new long[] { x, y });
    }

    @Override
    public IFieldOfView calculate() {
        this._checkMinAndMaxAllPolarizationsProvided();

        HashMap<Polarization, IBoxShape> fovBoxes = _createFovBoxes();

        return new FieldOfView(fovBoxes.get(Polarization.pol0), fovBoxes.get(Polarization.pol45),
                fovBoxes.get(Polarization.pol90), fovBoxes.get(Polarization.pol135));
    }

    /**
     * Create a box from min to max for every polarization.
     */
    private HashMap<Polarization, IBoxShape> _createFovBoxes() {
        HashMap<Polarization, IBoxShape> fovBoxes = new HashMap<>();
        for (Polarization polarization : Polarization.values()) {
            long[] pol_fov_min = _minPoints.get(polarization);
            long[] pol_fov_max = _maxPoints.get(polarization);

            IBoxShape pol_fovBox = ShapeFactory.closedBox(pol_fov_min, pol_fov_max, AxisOrder.XY);
            fovBoxes.put(polarization, pol_fovBox);
        }

        return fovBoxes;
    }

    private void _checkMinAndMaxAllPolarizationsProvided() {
        int numPolarizations = Polarization.values().length;
        if (this._minPoints.size() != numPolarizations || this._maxPoints.size() != numPolarizations) {
            throw new IllegalStateException(
                    "min and max point of all polarizations have not been provided for FoV calclation.");
        }

    }

    /**
     * Checks whether this 2D point is inside the image boundary.
     */
    private void _checkPointIsInImageBoundary(IMetadata imageMetadata, long x, long y) {
        long[] imgDim = imageMetadata.getDim();

        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Point must be positive.");
        }

        if (x > imgDim[0] || y > imgDim[1]) {
            throw new IllegalArgumentException("Point is outside image boundary.");
        }
    }

    @Override
    public long[] getMaxPoint(Polarization polarization) {
        return _maxPoints.get(polarization).clone();
    }

    @Override
    public long[] getMinPoint(Polarization polarization) {
        return _minPoints.get(polarization).clone();
    }

}