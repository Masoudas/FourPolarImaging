package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess.registrationSet;

import fr.fresnel.fourPolar.core.imageSet.acquisition.registration.RegistrationImageSet;

/**
 * Thrown in case of failure while reading a {@link RegistrationImageSet} from
 * the disk.
 */
public class IOIssueRegistrationSetProcessFailure extends RegistrationSetProcessFailure {
    private static final long serialVersionUID = -384382905714233075L;
    private final String _message;

    public IOIssueRegistrationSetProcessFailure(String reason) {
        _message = reason;
    }


    @Override
    protected String _createMessage() {
        return this._message;
    }

}