package fr.fresnel.fourPolar.core.image.generic.pixel.types;

/**
 * This class models the RGB pixel values. Each one of R, G and B is stored as a
 * {@link uint16} class.
 */
public class RGB {
    private uint16 _r;
    private uint16 _g;
    private uint16 _b;

    public RGB(uint16 r, uint16 g, uint16 b) {
        this.set(r, g, b);
    }

    public void set(uint16 r, uint16 g, uint16 b) {
        this._r = r;
        this._g = g;
        this._b = b;
    }

    /**
     * returns the int equivalent of R.
     * 
     * @return
     */
    public int getR() {
        return _r.get();
    }

    /**
     * returns the int equivalent of G.
     * 
     * @return
     */
    public int getG() {
        return _g.get();
    }

    /**
     * returns the int equivalent of B.
     * 
     * @return
     */
    public int getB() {
        return _b.get();
    }

}