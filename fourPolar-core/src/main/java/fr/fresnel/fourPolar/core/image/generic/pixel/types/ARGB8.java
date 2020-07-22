package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models the RGB pixel values. Each color has a maximum value of
 * 255.
 */
public class ARGB8 implements PixelType {
    /**
     * Maximum value allowed for each color.
     */
    public static int MAX_COLOR = 255;

    /**
     * Minimum value allowed for each color.
     */
    public static int MIN_COLOR = 0;

    /**
     * Maximum value allowed for alpha;
     */
    public static int MAX_ALPHA = 255;

    /**
     * Minimum value allowed for each color.
     */
    public static int MIN_ALPHA = 0;

    private int _r;
    private int _g;
    private int _b;
    private int _a;

    private final static ARGB8 _zero = new ARGB8(0, 0, 0);

    /**
     * Return a shared zero instance. Used for initializing types.
     * 
     * @return a shared instance with value zero.
     */
    public static ARGB8 zero() {
        return _zero;
    }

    /**
     * Constructs the type with specified values, setting alpha to {@link MIN_ALPHA}
     * (minimum transparency).
     * 
     * @param r is red.
     * @param g is green. 
     * @param b is blue.
     */
    public ARGB8(int r, int g, int b) {
        this.set(r, g, b, MIN_ALPHA);
    }

    public ARGB8(int r, int g, int b, int a){
        this.set(r, g, b, a);
    }

    public void set(int r, int g, int b, int a) {
        this._r = this._limitColorRange(r);
        this._g = this._limitColorRange(g);
        this._b = this._limitColorRange(b);
        this._a = this._limitAlphaRange(a);
    }

    public void set(ARGB8 rgb16) {
        this._r = this._limitColorRange(rgb16._r);
        this._g = this._limitColorRange(rgb16._g);
        this._b = this._limitColorRange(rgb16._b);
        this._a = this._limitAlphaRange(rgb16._a);
    }

    public void setAlpha(int alpha) {
        this._a = this._limitAlphaRange(alpha);
    }

    /**
     * Simply Add to the current pixel, and set the transparency to that of the new pixel.
     * @see {@link ColorBlender}. 
     */
    public void add(ARGB8 rgb16) {
        this.set(rgb16._r + this._r, rgb16._g + this._g, rgb16._b + this._b, rgb16._a);
    }

    /**
     * Limits the range of color to the range specified by {@code UINT16}
     * 
     * @param color
     */
    private int _limitColorRange(int color) {
        if (color < ARGB8.MIN_COLOR) {
            return MIN_COLOR;
        } else if (color > ARGB8.MAX_COLOR) {
            return ARGB8.MAX_COLOR;
        } else {
            return color;
        }
    }

    private int _limitAlphaRange(int alpha) {
        if (alpha < MIN_ALPHA) {
            return MIN_ALPHA;
        } else if (alpha > MAX_ALPHA) {
            return MAX_ALPHA;
        } else {
            return alpha;
        }

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

    public int getAlpha() {
        return _a;
    }

    @Override
    public PixelTypes getType() {
        return PixelTypes.ARGB_8;
    }

    @Override
    public PixelType copy() {
        return new ARGB8(getR(), getG(), getB(), getAlpha());
    }

}