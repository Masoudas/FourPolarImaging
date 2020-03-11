package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models a pixel of float type. It's in principle a wrapper for the
 * Float type of java. 
 */
public class Float32 extends PixelType {
    /**
     * Min value of the {@code Float} type, given for convenience. 
     */
    public static float MIN_VAL = Float.MIN_VALUE;

    /**
     * Max value of the {@code Float} type, given for convenience. 
     */
    public static float MAX_VAL = Float.MAX_VALUE;

    private float _pixel = 0f;

    /**
     * Creates the type, and sets the value to zero. 
     */
    public Float32() {
        this.set(0f);
    }

    /**
     * Construct with the specified value
     * @param value is the desired value.
     */
    public Float32(float value) {
        this.set(value);
    }

    public void set(float value) {
        this._pixel = value;
    }

    public float get() {
        return this._pixel;
    }

    /**
     * Adds the given pixel with this pixel. The rules are the same
     * as summation of normal floats.
     * 
     * @param pixel
     */
    public void add(Float32 pixel) {
        this.set(pixel.get() + this.get());
    }

    /**
     * Subtracts the given pixel value from this pixel.
     * 
     * @param pixel
     */
    public void subtract(UINT16 pixel) {
        this.set(this.get() - pixel.get());
    }

    @Override
    public Type getType() {
        return Type.FLOAT_32;
    }

}