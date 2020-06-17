package fr.fresnel.fourPolar.core.util.shape;

/**
 * An interface for a box shape. Box is represented using minimum and maximum
 * point.
 */
public interface IBoxShape extends IShape {
    /**
     * Return box length on the given dimension.
     * 
     * @param boxShape is the desired box.
     * @param dim is the dimension from zero.
     * 
     * @throws IllegalArgumentException if dimension does not exist.
     * @return
     */
    public static long len(IBoxShape boxShape, int dim) {
        if (dim < 0 || dim > boxShape.min().length){
            throw new IllegalArgumentException("The given dimension does not exist");
        }
        return boxShape.max()[dim] - boxShape.min()[dim];
    }

    public long[] min();

    public long[] max();
}