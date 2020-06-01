package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

import java.util.TreeSet;

/**
 * Defines the placement of polarization in the bead (or sample) image for the
 * one camera case.
 */
public class OneCameraPolarizationConstellation {
    public enum Position {
        TopLeft, TopRight, BottomLeft, BottomRight
    };

    private final Position _pol0;
    private final Position _pol45;
    private final Position _pol90;
    private final Position _pol135;

    /**
     * 
     * @throws IllegalArgumentException if duplicate position is given for a
     *                                  polarization.
     */
    public OneCameraPolarizationConstellation(Position pol0, Position pol45, Position pol90, Position pol135) {
        this._checkNoDuplicate(pol0, pol45, pol90, pol135);

        this._pol0 = pol0;
        this._pol45 = pol45;
        this._pol90 = pol90;
        this._pol135 = pol135;

    }

    private void _checkNoDuplicate(Position pol0, Position pol45, Position pol90, Position pol135) {
        TreeSet<Position> set = new TreeSet<>();

        if (!set.add(pol0) || !set.add(pol45) || !set.add(pol90) || !set.add(pol135)) {
            throw new IllegalArgumentException("Duplicate position for polarizations is not allowed.");
        }
    }

    public Position pol0() {
        return _pol0;
    }

    public Position pol45() {
        return _pol45;
    }

    public Position pol90() {
        return _pol90;
    }

    public Position pol135() {
        return _pol135;
    }

}