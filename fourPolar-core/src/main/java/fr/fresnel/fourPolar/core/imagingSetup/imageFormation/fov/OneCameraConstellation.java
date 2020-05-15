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
}