package fr.fresnel.fourPolar.core.image.vector;

import java.util.Objects;

import fr.fresnel.fourPolar.core.image.generic.pixel.types.ARGB8;
import fr.fresnel.fourPolar.core.util.shape.IBoxShape;
import fr.fresnel.fourPolar.core.util.shape.ILineShape;
import fr.fresnel.fourPolar.core.util.shape.IPointShape;
import fr.fresnel.fourPolar.core.util.shape.IShape;

class Vector implements IVector {
    private IShape _shape;
    private ARGB8 _color;
    private ARGB8 _fillColor;
    private int _strokeWidth;

    /**
     * Private constructor for setting colors and stroke width. Sets fill color
     * equal to color, and stroke with to 1.
     */
    private Vector(ARGB8 color) {
        Objects.requireNonNull(color, "color can't be null");

        _color = color;
        _fillColor = color;
        _strokeWidth = 1;
    }

    /**
     * Constructs the vector with a point shape, setting its color equal to fill
     * color, and stroke width equal to 1.
     * 
     * @param pointShape is the point shape of the vector.
     * @param color      is the color of the vector
     */
    Vector(IPointShape pointShape, ARGB8 color) {
        this(color);
        
        Objects.requireNonNull(pointShape, "pointShape can't be null");
        _shape = pointShape;
    }

    /**
     * Constructs the vector with a line shape, setting its color equal to fill
     * color, and stroke width equal to 1.
     * 
     * @param lineShape is the point shape of the vector.
     * @param color     is the color of the vector
     */
    Vector(ILineShape lineShape, ARGB8 color) {
        this(color);

        Objects.requireNonNull(lineShape, "lineShape can't be null");
        _shape = lineShape;
    }

    /**
     * Constructs the vector with a box shape, setting its color equal to fill
     * color, and stroke width equal to 1.
     * 
     * @param boxShape is the point shape of the vector.
     * @param color    is the color of the vector
     */
    Vector(IBoxShape boxShape, ARGB8 color) {
        this(color);
        _shape = boxShape;
    }

    @Override
    public IShape shape() {
        return this._shape;
    }

    @Override
    public ARGB8 color() {
        return this._color;
    }

    @Override
    public ARGB8 fill() {
        return this._fillColor;
    }

    @Override
    public int strokeWidth() {
        return this._strokeWidth;
    }

    @Override
    public void setShape(IPointShape point) {
        Objects.requireNonNull(point, "point shape can't be null");

        this._shape = point;
    }

    @Override
    public void setShape(IBoxShape box) {
        Objects.requireNonNull(box, "box shape can't be null");

        this._shape = box;
    }

    @Override
    public void setShape(ILineShape line) {
        Objects.requireNonNull(line, "line shape can't be null");

        this._shape = line;
    }

    @Override
    public void setColor(ARGB8 color) {
        Objects.requireNonNull(color, "Color can't be null");

        this._color = color;
    }

    @Override
    public void setFill(ARGB8 color) {
        this._fillColor = color;
    }

    @Override
    public void setStrokeWidth(int width) {
        // TODO Auto-generated method stub

    }

}