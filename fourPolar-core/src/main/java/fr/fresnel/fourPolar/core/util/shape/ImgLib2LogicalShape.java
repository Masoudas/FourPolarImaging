package fr.fresnel.fourPolar.core.util.shape;

import fr.fresnel.fourPolar.core.image.generic.axis.AxisOrder;
import net.imglib2.roi.RealMaskRealInterval;

/**
 * A logical shape, which would be generated as a result of anding two shapes
 * for example.
 */
public class ImgLib2LogicalShape extends ImgLib2Shape {
    public static IShape createAndedShape(ImgLib2Shape shape1, ImgLib2Shape shape2) {
        if (shape1.axisOrder() != shape2.axisOrder()) {
            throw new IllegalArgumentException("The two shapes are not defined over the same axis.");
        }

        if (!(shape1 instanceof ImgLib2Shape) || !(shape2 instanceof ImgLib2Shape)) {
            throw new IllegalArgumentException("The shapes are not instances of ImbLib2Shape.");
        }

        ImgLib2Shape shape1Ref = (ImgLib2Shape) shape1;
        ImgLib2Shape shape2Ref = (ImgLib2Shape) shape2;

        RealMaskRealInterval andedShape = shape1Ref.getImgLib2Shape().and(shape2Ref.getImgLib2Shape());

        return new ImgLib2LogicalShape(andedShape, shape1.axisOrder(), shape1.shapeDim());
    }

    private ImgLib2LogicalShape(RealMaskRealInterval shape, AxisOrder axisOrder, int shapeDim) {
        super(shape, axisOrder, shapeDim);
    }

}