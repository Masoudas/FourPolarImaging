package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

/**
 * Defines the placement of polarization in the bead (or sample) image for the
 * two camera case.
 */
public class TwoCameraPolarizationConstellation {
    public enum Position {
        Left, Right
    };

    final private Position _pol0;
    final private Position _pol45;
    final private Position _pol90;
    final private Position _pol135;

    /**
     * 
     * @throws IllegalArgumentException position for pol0 and pol90, and pol45 and
     *                                  pol135 must be unequal.
     */
    public TwoCameraPolarizationConstellation(Position pol0, Position pol45, Position pol90, Position pol135) {
        if (pol0 == pol90 || pol45 == pol135) {
            throw new IllegalArgumentException("position for pol0 and pol90, and pol45 and pol135 must be unequal.");
        }

        _pol0 = pol0;
        _pol45 = pol45;
        _pol90 = pol90;
        _pol135 = pol135;
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