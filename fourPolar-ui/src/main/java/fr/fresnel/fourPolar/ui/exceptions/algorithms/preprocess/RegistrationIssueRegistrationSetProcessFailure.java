package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

/**
 * Thrown in case of failure while registration a {@link RegistrationImageSet}.
 * This class can specify which {@link RegistrationRule} has failed for each
 * channel (if any).
 */
public class RegistrationIssueRegistrationSetProcessFailure extends RegistrationSetProcessFailure {
    /**
     *
     */
    private static final long serialVersionUID = -3746773829399830474L;
    private final Map<RegistrationRule, ArrayList<Integer>> _failedRegistrations;

    public RegistrationIssueRegistrationSetProcessFailure() {
        _failedRegistrations = _initializeFailedRegistrationMap();
    }

    private Map<RegistrationRule, ArrayList<Integer>> _initializeFailedRegistrationMap() {
        Map<RegistrationRule, ArrayList<Integer>> failedRegistrations = new EnumMap<>(RegistrationRule.class);

        for (RegistrationRule rule : RegistrationRule.values()) {
            failedRegistrations.put(rule, new ArrayList<Integer>());
        }

        return failedRegistrations;
    }

    public void setRuleFailure(RegistrationRule rule, int channel) {
        this._failedRegistrations.get(rule).add(channel);
    }

    @Override
    protected String _createMessage() {
        return "Registration failed for the following channels: " + this._failedRegistrations.toString();
    }

    public List<Integer> getRuleFailure(RegistrationRule rule) {
        return this._failedRegistrations.get(rule);
    }

    /**
     * Returns true if at least one rule has failed among all channels.
     * 
     * @return
     */
    public boolean hasFailure() {
        boolean hasFailure = false;
        for (int ruleIndex = 0; ruleIndex < RegistrationRule.values().length && !hasFailure; ruleIndex++) {
            hasFailure = this.getRuleFailure(RegistrationRule.values()[ruleIndex]).size() != 0;
        }

        return hasFailure;
    }

}