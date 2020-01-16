package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

/**
 * A rectangle. Used for indicating the field of view of the bead image.
 */
public class Rectangle {
    private int _xtop;
    private int _ytop;
    private int _width;
    private int _height;

    public Rectangle(int xtop, int ytop, int width, int height) {
        this._xtop = xtop;
        this._ytop = ytop;

        this._height = height;
        this._width = width;
        
    }

    /**
     * @return the xTop
     */
    public int getxTop() {
        return this._xtop;
    }

    /**
     * @return the xTop
     */
    public int getyTop() {
        return this._ytop;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return this._height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return this._width;
    }

}