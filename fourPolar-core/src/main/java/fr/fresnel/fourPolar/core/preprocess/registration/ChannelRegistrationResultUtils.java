package fr.fresnel.fourPolar.core.preprocess.registration;

import java.util.ArrayList;

public class ChannelRegistrationResultUtils {
    private ChannelRegistrationResultUtils() {
        throw new AssertionError();
    }

    /**
     * Retruns true if every {@link RegistrationRule} has been successful.
     * 
     * @param result
     */
    public static boolean isEveryRegistrationSuccessful(IChannelRegistrationResult result) {
        boolean successful = true;
        for (RegistrationRule rule : RegistrationRule.values()) {
            successful &= result.registrationSuccessful(rule);
        }

        return successful;
    }

    /**
     * @return which rules have failed for this channel.
     */
    public static RegistrationRule[] getFailedRegistrations(IChannelRegistrationResult result) {
        ArrayList<RegistrationRule> failedRules = new ArrayList<>();

        for (RegistrationRule rule : RegistrationRule.values()) {
            if (!result.registrationSuccessful(rule)) {
                failedRules.add(rule);
            }
        }
        return failedRules.toArray(new RegistrationRule[0]);
    }
}