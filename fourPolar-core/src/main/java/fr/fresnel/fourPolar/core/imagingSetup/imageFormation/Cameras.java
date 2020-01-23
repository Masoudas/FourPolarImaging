package fr.fresnel.fourPolar.core.imagingSetup.imageFormation;

public enum Cameras {
    One, Two, Four;

    // One camera case
    private static String[] _one = {"Pol0_45_90_135"};

    // Two camera case
    private static String[] _two = {"Pol0_90", "Pol45_135"};

    // Four camera case
    private static String[] _four = {"Pol0", "Pol45", "Pol90", "Pol135"};

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

}