package fr.fresnel.fourPolar.core.preprocess.registration;

/**
 * Determines the order for applying registration. The rule is that pol45, pol90
 * and pol135 are all registered to pol0.
 */
public enum ChannelRegistrationOrder {
    Pol45_to_Pol0, Pol90_to_Pol0, Pol135_to_Pol0,
}