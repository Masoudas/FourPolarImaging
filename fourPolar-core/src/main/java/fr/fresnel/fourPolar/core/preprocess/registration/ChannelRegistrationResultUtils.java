package fr.fresnel.fourPolar.core.preprocess.registration;

public class ChannelRegistrationResultUtils {
    private ChannelRegistrationResultUtils(){
        throw new AssertionError();
    }

    /**
     * Retruns true if every {@link RegistrationRule} has been successful.
     * @param result
     */
    public static boolean isEveryRegistrationSuccessful(IChannelRegistrationResult result) {
        boolean successful = true;
        for (RegistrationRule rule : RegistrationRule.values()) {
            successful &= result.registrationSuccessful(rule);
        }

        return successful;
    }
}