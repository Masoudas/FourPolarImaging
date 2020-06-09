package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

/**
 * A logical shape, which would be generated as a result of anding two shapes
 * for example.
 */
public class ImgLib2LogicalShape extends ImgLib2Shape {
    public static IShape create(int shapeDim, RealMaskRealInterval shape, AxisOrder axisOrder) {
        return new ImgLib2LogicalShape(shapeDim, shape, axisOrder);
    }

    private ImgLib2LogicalShape(int shapeDim, RealMaskRealInterval shape, AxisOrder axisOrder) {
        super(shapeDim, shape, axisOrder);
    }

}