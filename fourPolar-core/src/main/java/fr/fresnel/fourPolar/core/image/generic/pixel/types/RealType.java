package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * An interface that models real types (in R), as opposed to for example
 * RGB type.
 */
public interface RealType extends PixelType {
    /**
     * Returns the real value associated with the type as double.
     */
    public double getRealValue();

}