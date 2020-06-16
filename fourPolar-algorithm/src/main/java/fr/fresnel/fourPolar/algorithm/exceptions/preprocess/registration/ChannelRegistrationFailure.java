package fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration;

import java.util.HashMap;
import java.util.Objects;

import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Thrown in case the registration algorithm fails to register at least one
 * {@link RegistrationRule} for a channel.
 * <p>
 * NOTE: By registration failure we imply failing to get an affine transform.
 * Note however if registration is for example weak (has large error), we still
 * say it has been successful.
 */
public class ChannelRegistrationFailure extends Exception {
    public static class Builder {
        private final HashMap<RegistrationRule, String> _buildFailures = new HashMap<>();

        /**
         * Register a failure reason.
         */
        public Builder addRuleFailure(RegistrationRule rule, String failureCause) {
            Objects.requireNonNull(rule, "rule cannot be null");
            Objects.requireNonNull(failureCause, "failureCause cannot be null");

            this._buildFailures.put(rule, failureCause);

            return this;
        }

        public ChannelRegistrationFailure buildException() {
            return new ChannelRegistrationFailure(this);
        }
    }

    private final HashMap<RegistrationRule, String> _failures;

    private static final long serialVersionUID = 7499711200932393875L;

    /**
     * Create an exception, by registration a message indicating the failure reason.
     * 
     * @param failureReason is the description of why registration failed.
     */
    private ChannelRegistrationFailure(Builder builder) {
        this._failures = builder._buildFailures;
    }

    public RegistrationRule[] getFailedRules() {
        return _failures.keySet().toArray(new RegistrationRule[0]);
    }

    public String getFailureReason(RegistrationRule rule) {
        return _failures.get(rule);
    }

    @Override
    public String getMessage() {
        return "Registration was unsuccessful for the following: " + _failures;
    }
}