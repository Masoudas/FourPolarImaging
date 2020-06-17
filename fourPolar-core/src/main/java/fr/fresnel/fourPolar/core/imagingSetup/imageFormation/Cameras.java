package fr.fresnel.fourPolar.core.imagingSetup.imageFormation;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

public enum Cameras {
    One, Two, Four;

    // One camera case
    private static String[] _one = { "Pol0_45_90_135" };

    // Two camera case
    private static String[] _two = { "Pol0_90", "Pol45_135" };

    // Four camera case
    private static String[] _four = { "Pol0", "Pol45", "Pol90", "Pol135" };

    public static String[] getLabels(Cameras camera) {
        switch (camera) {
            case One:
                return _one;

            case Two:
                return _two;

            case Four:
                return _four;

            default:
                return null;
        }
    }

    public static int getNImages(Cameras camera) {
        return getLabels(camera).length;
    }

    /**
     * Returns which label contains the given polarization. The label may also
     * contain other polarizations.
     * 
     * @param camera       is the camera.
     * @param polarization is the polarization.
     * @return the label that contains the polarization.
     */
    public static String getLabelThatContainsPolarization(Cameras camera, Polarization polarization) {
        if (camera == Cameras.One) {
            return _getLabelThatContainsPolarization_OneCamera(polarization);
        } else if (camera == Cameras.Two) {
            return _getLabelThatContainsPolarization_TwoCamera(polarization);
        } else {
            return _getLabelThatContainsPolarization_FourCamera(polarization);
        }

    }

    private static String _getLabelThatContainsPolarization_OneCamera(Polarization polarization) {
        return _one[0];
    }

    private static String _getLabelThatContainsPolarization_TwoCamera(Polarization polarization) {
        if (polarization == Polarization.pol0 || polarization == Polarization.pol90) {
            return _two[0];
        } else {
            return _two[1];
        }
    }

    private static String _getLabelThatContainsPolarization_FourCamera(Polarization polarization) {
        if (polarization == Polarization.pol0) {
            return _four[0];
        } else if (polarization == Polarization.pol45) {
            return _four[1];
        } else if (polarization == Polarization.pol90) {
            return _four[2];
        } else {
            return _four[3];
        }
    }
}