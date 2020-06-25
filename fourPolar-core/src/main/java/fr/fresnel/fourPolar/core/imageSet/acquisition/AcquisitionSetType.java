package fr.fresnel.fourPolar.core.imageSet.acquisition;

/**
 * Determines the type of acquisition set and returns a string description for
 * each type.
 */
public enum AcquisitionSetType {
    Sample("Sample Set"), Registration("Registration Set");

    public final String description;

    AcquisitionSetType(String description) {
        this.description = description;
    }
}