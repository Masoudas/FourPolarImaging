package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

/**
 * A rectangle. Used for indicating the field of view of the bead image.
 */
public class Rectangle {
    private int[] _bottom = new int[2];
    private int _width;
    private int _height;

    public Rectangle(int x_bottom, int y_bottom, int width, int height) {
        this._bottom[0] = x_bottom;
        this._bottom[1] = y_bottom;

        this._height = height;
        this._width = width;
        
    }

    /**
     * @return the _bottom
     */
    public int[] get_bottom() {
        return _bottom;
    }

    /**
     * @return the _height
     */
    public int get_height() {
        return _height;
    }

    /**
     * @return the _width
     */
    public int get_width() {
        return _width;
    }

}