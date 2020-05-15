package fr.fresnel.fourPolar.algorithm.preprocess.fov;

import java.util.Arrays;

import fr.fresnel.fourPolar.core.image.generic.IMetadata;
import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.FieldOfView;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov.IFieldOfView;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ShapeFactory;

/**
 * Calculates the FoV for the four camera case, using the plane dimension of the
 * Captured images. It essentially returns minimum of x and y axis among all
 * images.
 */

public class FoVCalculatorFourCamera implements IFoVCalculator {
    private final long _x_len_min;
    private final long _y_len_min;

    public FoVCalculatorFourCamera(IMetadata beadImg_pol0, IMetadata beadImg_pol45, IMetadata beadImg_pol90,
            IMetadata beadImg_pol135) {
        this._x_len_min = Arrays.stream(new long[] { beadImg_pol0.getDim()[0], beadImg_pol45.getDim()[0],
                beadImg_pol90.getDim()[0], beadImg_pol135.getDim()[0] }).summaryStatistics().getMin();
        this._y_len_min = Arrays.stream(new long[] { beadImg_pol0.getDim()[1], beadImg_pol45.getDim()[1],
                beadImg_pol90.getDim()[1], beadImg_pol135.getDim()[1] }).summaryStatistics().getMin();

    }

    @Override
    public IFieldOfView calculate() {
        IBoxShape pol0 = _defineFoVAsBox();
        IBoxShape pol45 = _defineFoVAsBox();
        IBoxShape pol90 = _defineFoVAsBox();
        IBoxShape pol135 = _defineFoVAsBox();

        return new FieldOfView(pol0, pol45, pol90, pol135);
    }

    private IBoxShape _defineFoVAsBox() {
        long[] bottom = { 1, 1 };
        long[] top = { _x_len_min, _y_len_min };
        return new ShapeFactory().closedBox(bottom, top, AxisOrder.XY);
    }

}