package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models the RGB pixel values. Each one of R, G and B is stored as a
 * {@link UINT16} class.
 */
public class RGB16 implements PixelType {
    private int _r;
    private int _g;
    private int _b;

    private final static RGB16 _zero = new RGB16(0, 0, 0);

    /**
     * Constructs the type with specified values.
     * 
     * @param r
     * @param g
     * @param b
     */
    public RGB16(int r, int g, int b) {
        this.set(r, g, b);
    }

    public void set(int r, int g, int b) {
        this._r = this._limitColorRange(r);
        this._g = this._limitColorRange(g);
        this._b = this._limitColorRange(b);
    }

    /**
     * Limits the range of color to the range specified by {@code UINT16}
     * 
     * @param color
     * @return
     */
    private int _limitColorRange(int color) {
        int compressedColor = 0;
        if (color < UINT16.MIN_VAL) {
            compressedColor = 0;
        } else if (color > UINT16.MAX_VAL) {
            compressedColor = UINT16.MAX_VAL;
        } else {
            compressedColor = color;
        }

        return compressedColor;

    }

    /**
     * returns the int equivalent of R.
     * 
     * @return
     */
    public int getR() {
        return _r;
    }

    /**
     * returns the int equivalent of G.
     * 
     * @return
     */
    public int getG() {
        return _g;
    }

    /**
     * returns the int equivalent of B.
     * 
     * @return
     */
    public int getB() {
        return _b;
    }

    @Override
    public Type getType() {
        return Type.RGB_16;
    }

    /**
     * Return a shared zero instance. Used for initializing types.
     * 
     * @return a shared instance with value zero.
     */
    public static RGB16 zero() {
        return _zero;
    }

    @Override
    public PixelType copy() {
        return new RGB16(getR(), getG(), getB());
    }

}