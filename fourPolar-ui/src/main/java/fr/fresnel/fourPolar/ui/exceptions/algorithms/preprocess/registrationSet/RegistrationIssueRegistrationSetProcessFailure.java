package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet;

import java.util.HashMap;
import java.util.Map;

import fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration.ChannelRegistrationFailure;
import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Thrown in case of failure while registration a {@link RegistrationImageSet}.
 * This class can specify which {@link RegistrationRule} has failed for each
 * channel (if any).
 */
public class RegistrationIssueRegistrationSetProcessFailure extends RegistrationSetProcessFailure {
    private static final long serialVersionUID = -3746773829399830474L;
    private final Map<Integer, RegistrationRule[]> _failedRegistrations;

    public static class Builder {
        private final Map<Integer, RegistrationRule[]> _failures = new HashMap<>();

        public void setRuleFailure(ChannelRegistrationFailure failureException, int channel) {
            this._failures.put(channel, failureException.getFailedRules());
        }

        public RegistrationIssueRegistrationSetProcessFailure buildException() {
            _checkAtLeastOneFailureExists();
            return new RegistrationIssueRegistrationSetProcessFailure(this);
        }

        private void _checkAtLeastOneFailureExists() {
            if (this._failures.isEmpty()){
                throw new IllegalArgumentException("Can't create exception with no failure cases.");
            }
        }
    }

    private RegistrationIssueRegistrationSetProcessFailure(Builder builder) {
        _failedRegistrations = builder._failures;
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

    /**
     * Returns true if at least one channel has failed.
     * 
     * @return
     */
    public boolean hasFailure() {
        return !this._failedRegistrations.isEmpty();
    }

}