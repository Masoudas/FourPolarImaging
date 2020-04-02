package fr.fresnel.fourPolar.algorithm.visualization.figures.stickFigure.stick;

import fr.fresnel.fourPolar.algorithm.exceptions.visualization.figures.stickFigure.AngleStickUndefined;
import fr.fresnel.fourPolar.core.image.generic.pixel.types.RGB16;
import fr.fresnel.fourPolar.core.physics.dipole.IOrientationVector;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationAngle;
import fr.fresnel.fourPolar.core.physics.dipole.OrientationVector;
import fr.fresnel.fourPolar.core.util.DPoint;
import fr.fresnel.fourPolar.core.util.Point;
import fr.fresnel.fourPolar.core.util.colorMap.ColorMap;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStick;
import fr.fresnel.fourPolar.core.visualization.figures.stickFigure.stick.IAngleStickIterator;
import ij.gui.Line;

/**
 * A concrete implementation of {@link IAngleStick}. Note that the sticks can
 * have negative coordinates in this implementation.
 */
public class AngleStickGenerator {
    private final ColorMap _colorMap;

    /**
     * Define a colormap, to choose the stick colors from.
     */
    public AngleStickGenerator(ColorMap colorMap) {
        this._colorMap = colorMap;
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * is again the rho angle.
     * 
     * @param vec            is the orientation vector.
     * @param dipolePosition is the corresponding dipole discrete position.
     * @param length         is the desired length of the dipole, and greater than equal 1.
     * @param thickness      is the desired thickness of the dipole, and greater than equal 1.
     * @return
     */
    public IAngleStick getRhoStick(IOrientationVector vec, DPoint dipolePosition, int length, int thickness)
            throws AngleStickUndefined {
        if (Float.isNaN(vec.getAngle(OrientationAngle.rho)) || Float.isNaN(vec.getAngle(OrientationAngle.rho))) {
            throw new AngleStickUndefined("Cannot define stick for nan angles.");
        }
        float slope = vec.getAngle(OrientationAngle.rho);

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Rho, slope);
        IAngleStickIterator iterator = formIterator(slope, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, vec.getAngle(OrientationAngle.rho), length, thickness, color, iterator);
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * the delta angle.
     * 
     * @param vec            is the orientation vector.
     * @param dipolePosition is the corresponding dipole discrete position.
     * @param length         is the desired length of the dipole, and greater than equal 1.
     * @param thickness      is the desired thickness of the dipole, and greater than equal 1.     * @return
     */
    public IAngleStick getDeltaStick(IOrientationVector vec, DPoint dipolePosition, int length, int thickness)
            throws AngleStickUndefined {
        if (Float.isNaN(vec.getAngle(OrientationAngle.rho)) || Float.isNaN(vec.getAngle(OrientationAngle.rho))) {
            throw new AngleStickUndefined("Cannot define stick for nan angles.");
        }

        float slope = vec.getAngle(OrientationAngle.rho);

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Delta, vec.getAngle(OrientationAngle.delta));
        IAngleStickIterator iterator = formIterator(slope, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, vec.getAngle(OrientationAngle.rho), length, thickness, color, iterator);
    }

    /**
     * Generates an {@link IAngleStick}, where the slope is the rho angle, and color
     * is the eta angle.
     * 
     * @param vec            is the orientation vector.
     * @param dipolePosition is the corresponding dipole discrete position.
     * @param length         is the desired length of the dipole, and greater than equal 1.
     * @param thickness      is the desired thickness of the dipole, and greater than equal 1.
     * @return
     */
    public IAngleStick getEtaStick(IOrientationVector vec, DPoint dipolePosition, int length, int thickness)
            throws AngleStickUndefined {
        if (Float.isNaN(vec.getAngle(OrientationAngle.rho)) || Float.isNaN(vec.getAngle(OrientationAngle.rho))) {
            throw new AngleStickUndefined("Cannot define stick for nan angles.");
        }

        float slope = vec.getAngle(OrientationAngle.rho);

        RGB16 color = _colorMap.getColor(0, OrientationVector.MAX_Eta, vec.getAngle(OrientationAngle.eta));
        IAngleStickIterator iterator = formIterator(slope, dipolePosition, length, thickness);

        return new AngleStick(dipolePosition, slope, length, thickness, color, iterator);
    }

    /**
     * To form an iterator that iterates over the pixels corresponding to the stick,
     * define a negative line, one that starts from the end point to the position,
     * and one that starts from position to the other line. This would ensure that
     * the stick always passes through the dipole!
     * 
     * @param slopeAngle     is the slope of the dipole in particular direction.
     * @param dipolePosition is the pixel position of the dipole.
     * @param length         is the desired length of the stick.
     * @param thickness      is the desired thickness of the stick.
     * @return iterator that iterates over the region corresponding to this stick,
     *         in pixel coordinates.
     */
    public IAngleStickIterator formIterator(float slopeAngle, DPoint dipolePosition, int length, int thickness) {
        Point[] endPoints = _getEndPoints(dipolePosition, slopeAngle, length);
        Line negativeLine = new Line(endPoints[0].x, endPoints[0].y, dipolePosition.x, dipolePosition.y);
        negativeLine.setStrokeWidth(thickness);

        Line positiveLine = new Line(dipolePosition.x, dipolePosition.y, endPoints[1].x, endPoints[1].y);
        positiveLine.setStrokeWidth(thickness);

        return new AngleStickIterator(negativeLine.iterator(), positiveLine.iterator());
    }

    /**
     * Calculate the end points of the stick, assuming the length of the stick and
     * knowing the angle of the stick.
     * 
     * @return start_point and end_point.
     */
    private Point[] _getEndPoints(DPoint dipolePosition, float slopeAngle, int length) {
        double xStart = dipolePosition.x - Math.cos(slopeAngle) * ((length - 1) / 2);
        double yStart = dipolePosition.y - Math.sin(slopeAngle) * ((length - 1) / 2);

        double xEnd = dipolePosition.x + Math.cos(slopeAngle) * (length / 2);
        double yEnd = dipolePosition.y + Math.sin(slopeAngle) * (length / 2);

        return new Point[] { new Point(xStart, yStart), new Point(xEnd, yEnd) };
    }

}