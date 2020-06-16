package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.algorithm.exceptions.preprocess.registration.ChannelRegistrationFailure;
import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;
import fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet.RegistrationIssueRegistrationSetProcessFailure;

public class RegistrationIssueRegistrationSetProcessFailureTest {
    @Test
    public void getMessage_Rule1OfC1AndRule3ofC3Failure_ReturnsCorrectMessage() {
        String failureReason1 = "dummyCause1";
        ChannelRegistrationFailure c1Failure = new ChannelRegistrationFailure.Builder()
                .addRuleFailure(RegistrationRule.values()[0], failureReason1).buildException();

        String failureReason2 = "dummyCause2";
        ChannelRegistrationFailure c3Failure = new ChannelRegistrationFailure.Builder()
                .addRuleFailure(RegistrationRule.values()[2], failureReason2).buildException();

        RegistrationIssueRegistrationSetProcessFailure.Builder failureBuilder = new RegistrationIssueRegistrationSetProcessFailure.Builder();

        RegistrationIssueRegistrationSetProcessFailure exception = failureBuilder.setRuleFailure(c1Failure, 1)
                .setRuleFailure(c3Failure, 3).buildException();

        assertArrayEquals(exception.getFailedChannels(), new int[] { 1, 3 });
        assertArrayEquals(exception.getChannelFailedRules(1), new RegistrationRule[] { RegistrationRule.values()[0] });
        assertArrayEquals(exception.getChannelFailedRules(3), new RegistrationRule[] { RegistrationRule.values()[2] });

        assertTrue(exception.getFailureReason(1, RegistrationRule.values()[0]).equals(failureReason1));
        assertTrue(exception.getFailureReason(3, RegistrationRule.values()[2]).equals(failureReason2));

    }

    @Test
    public void buildException_WithNoException_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistrationIssueRegistrationSetProcessFailure.Builder().buildException();
        });

    }
}