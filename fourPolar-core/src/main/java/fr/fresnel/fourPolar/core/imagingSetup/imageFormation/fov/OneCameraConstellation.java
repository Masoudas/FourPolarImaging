package fr.fresnel.fourPolar.core.imagingSetup.imageFormation.fov;

/**
 * Assigns the placement of polarization in the bead (or sample) image.
 */
public class OneCameraConstellation {
    public enum Position {
        TopLeft, TopRight, BottomLeft, BottomRight
    };

    public Position pol0;
    public Position pol45;
    public Position pol90;
    public Position pol135;

    public OneCameraConstellation(Position pol0, Position pol45, Position pol90, Position pol135) {
        this.pol0 = pol0;
        this.pol45 = pol45;
        this.pol90 = pol90;
        this.pol135 = pol135;
    }
}