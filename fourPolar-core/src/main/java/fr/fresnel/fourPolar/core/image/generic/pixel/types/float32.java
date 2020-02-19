package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models a pixel of float type. It's in principle a wrapper for the
 * Float type of java. 
 */
public class float32 extends PixelType {
    /**
     * Min value of the {@code Float} type, given for convenience. 
     */
    public static float MIN_VAL = Float.MIN_VALUE;

    /**
     * Max value of the {@code Float} type, given for convenience. 
     */
    public static float MAX_VAL = Float.MAX_VALUE;

    private Float _pixel = 0f;

    public float32(Float value) {
        this.set(value);
    }

    public void set(Float value) {
        this._pixel = value;
    }

    public Float get() {
        return this._pixel;
    }

    /**
     * Sums the given pixel with this pixel. The rules are the same
     * as summation of normal floats.
     * 
     * @param pixel
     */
    public void sum(float32 pixel) {
        this.set(pixel.get() + this.get());
    }

    /**
     * Subtracts the given pixel value from this pixel.
     * 
     * @param pixel
     */
    public void subtract(uint16 pixel) {
        this.set(this.get() - pixel.get());
    }

    @Override
    public Type getType() {
        return Type.FLOAT32;
    }

}