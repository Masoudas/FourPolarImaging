package fr.fresnel.fourPolar.core.imagingSetup.imageFormation;

public enum PolarizationConstellation {
    OneByOne, TwoByOne, TwoByTwo;

    /**
     * Determines how many images are present based on the constellation.
     * @param constellation
     * @return
     */
    public static int getNConstellationImages(PolarizationConstellation constellation) {
        if (constellation == OneByOne)
            return 4;
        else if (constellation == TwoByOne)
            return 2;
        else
            return 1;

    }
}