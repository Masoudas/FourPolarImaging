package fr.fresnel.fourPolar.core.preprocess.registration;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;

/**
 * Determines the rule for applying registration. The rule is that pol45, pol90
 * and pol135 are all registered to pol0.
 */
public enum RegistrationRule {
    Pol45_to_Pol0("Pol45 to Pol0") {
        public Polarization getBaseImagePolarization() {
            return Polarization.pol0;
        }

        public Polarization getToRegisterImagePolarization() {
            return Polarization.pol45;
        }
    },

    Pol90_to_Pol0("Pol90 to Pol0") {
        public Polarization getBaseImagePolarization() {
            return Polarization.pol0;
        }

        public Polarization getToRegisterImagePolarization() {
            return Polarization.pol90;
        }
    },

    Pol135_to_Pol0("Pol135 to Pol0") {
        public Polarization getBaseImagePolarization() {
            return Polarization.pol0;
        }

        public Polarization getToRegisterImagePolarization() {
            return Polarization.pol135;
        }
    };

    public final String description;

    RegistrationRule(String description) {
        this.description = description;
    }

    /**
     * Returns the polarization that would be the base of registration for this
     * rule.
     */
    abstract public Polarization getBaseImagePolarization();

    /**
     * Returns the polarization to be registeredd for this rule.
     */
    abstract public Polarization getToRegisterImagePolarization();
}