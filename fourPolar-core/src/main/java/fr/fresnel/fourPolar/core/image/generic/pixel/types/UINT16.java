package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models a pixel of unsigned short type, whose range is from 0 to
 * 2^16-1. Note that summation results in saturation rather than overflow,
 * meaning that if the summation results in a value greater than 2^16-1, it will
 * be rounded to 2^16-1,
 */
public class UINT16 implements RealType {
    public static int MIN_VAL = 0;
    public static int MAX_VAL = 65535;

    private int _value = 0;

    private final static UINT16 _zero = new UINT16(0);

    /**
     * Construct the type and set it to the specified value.
     * @param value
     */
    public UINT16(int value) {
        this.set(value);
    }

    public void set(int value) {
        if (value < 0) {
            this._value = 0;
        } else if (value > MAX_VAL) {
            this._value = MAX_VAL;
        } else {
            this._value = value;
        }
    }

    public int get() {
        return this._value;
    }

    /**
     * Adds the given pixel with this pixel. The result is rounded to
     * {@value MAX_VAL} if exceeds {@value MAX_VAL}.
     * 
     * @param pixel
     */
    public void add(UINT16 pixel) {
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
    public PixelTypes getType() {
        return PixelTypes.UINT_16;
    }

    /**
     * Return a shared zero instance. Used for initializing types.
     * 
     * @return a shared instance with value zero.
     */
    public static UINT16 zero() {
        return _zero;
    }

    @Override
    public PixelType copy() {
        return new UINT16(get());
    }

    @Override
    public double getRealValue() {
        return this._value;
    }

}