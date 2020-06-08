package fr.fresnel.fourPolar.ui.exceptions.algorithms.preprocess;

import java.util.Arrays;

/**
 * Thrown in case of failure while reading a {@link RegistrationImageSet} from
 * the disk.
 */
public class IOIssueRegistrationSetProcessFailure extends RegistrationSetProcessFailure {
    private static final long serialVersionUID = -384382905714233075L;
    private final int[] _channel;
    private final String _message;

    public static IOIssueRegistrationSetProcessFailure failedReadingChannel(int[] channel) {
        return new IOIssueRegistrationSetProcessFailure(channel);
    }

    public static IOIssueRegistrationSetProcessFailure failedWithOtherIssues(String reason) {
        return new IOIssueRegistrationSetProcessFailure(reason);
    }

    private IOIssueRegistrationSetProcessFailure(String reason) {
        _channel = new int[0];
        _message = reason;
    }

    private IOIssueRegistrationSetProcessFailure(int[] channel) {
        _channel = channel;
        _message = "Can't load captured image of channel " + Arrays.toString(channel);
    }

    @Override
    protected String _createMessage() {
        return this._message;
    }

    public int getChannel() {
        return this._channel;
    }

}