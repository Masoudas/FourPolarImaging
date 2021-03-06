package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration.ChannelRegistrationFailure;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.physics.channel.ChannelUtils;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Thrown in case of failure while registration a {@link RegistrationImageSet}.
 * This class can specify which {@link RegistrationRule} has failed for each
 * channel (if any).
 */
public class RegistrationIssueRegistrationSetProcessFailure extends RegistrationSetProcessFailure {
    private static final long serialVersionUID = -3746773829399830474L;
    private final Map<Integer, RegistrationRule[]> _failedRegistrations;
    private final Map<String, String> _reasons;

    public static class Builder {
        private final Map<Integer, RegistrationRule[]> _failures = new HashMap<>();
        private final Map<String, String> _reasons = new HashMap<>();

        public Builder setRuleFailure(ChannelRegistrationFailure failureException, int channel) {
            Objects.requireNonNull(failureException, "failureException can't be null");
            ChannelUtils.checkChannelNumberIsNonZero(channel);

            this._setChannelFailureRules(failureException, channel);
            this._setReasons(failureException, channel);
            return this;
        }

        private void _setChannelFailureRules(ChannelRegistrationFailure failureException, int channel) {
            this._failures.put(channel, failureException.getFailedRules());
        }

        public RegistrationIssueRegistrationSetProcessFailure buildException() {
            _checkAtLeastOneFailureExists();
            return new RegistrationIssueRegistrationSetProcessFailure(this);
        }

        private void _setReasons(ChannelRegistrationFailure failureException, int channel) {
            for (RegistrationRule rule : failureException.getFailedRules()) {
                this._reasons.put(rule.name() + channel, failureException.getFailureReason(rule));
            }
        }

        private void _checkAtLeastOneFailureExists() {
            if (!this._hasFailure()) {
                throw new IllegalArgumentException("Can't create exception with no failure cases.");
            }
        }

        /**
         * Returns true if at least one channel has failed.
         * 
         * @return
         */
        private boolean _hasFailure() {
            return !this._failures.isEmpty();
        }

    }

    private RegistrationIssueRegistrationSetProcessFailure(Builder builder) {
        _failedRegistrations = builder._failures;
        _reasons = builder._reasons;
    }

    @Override
    protected String _createMessage() {
        return "Registration failed for the following channels: " + this._failedRegistrations.toString();
    }

    public int[] getFailedChannels() {
        return this._failedRegistrations.keySet().stream().mapToInt((t) -> t).toArray();
    }

    public RegistrationRule[] getChannelFailedRules(int channel) {
        return this._failedRegistrations.get(channel);
    }

    public String getFailureReason(int channel, RegistrationRule rule) {
        return this._reasons.get(rule.name() + channel);
        
    }
}