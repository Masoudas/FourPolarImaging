package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.fresnel.fourPolar.core.preprocess.registration.RegistrationRule;

public class RegistrationIssueRegistrationSetProcessFailureTest {
    @Test
    public void getMessage_Rule0OfC1AndRule3ofC3Failure_ReturnsCorrectMessage() {
        RegistrationIssueRegistrationSetProcessFailure failure = new RegistrationIssueRegistrationSetProcessFailure();

        RegistrationRule[] rules = RegistrationRule.values();

        failure.setRuleFailure(rules[0], 1);
        failure.setRuleFailure(rules[2], 3);

        assertTrue(failure.hasFailure());

        assertTrue(failure.getRuleFailure(rules[0]).size() == 1 && failure.getRuleFailure(rules[0]).get(0) == 1);

        assertTrue(failure.getRuleFailure(rules[2]).size() == 1 && failure.getRuleFailure(rules[2]).get(0) == 3);

        assertTrue(failure.getRuleFailure(rules[1]).size() == 0);

        System.out.println(failure.getMessage());
    }

    @Test
    public void hasFailure_NoFailure_ReturnsFalse() {
        RegistrationIssueRegistrationSetProcessFailure failure = new RegistrationIssueRegistrationSetProcessFailure();

        assertTrue(!failure.hasFailure());

    }

}