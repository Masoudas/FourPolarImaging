package fr.fresnel.fourPolar.io.exceptions.image.captured;

import java.io.IOException;
import java.util.Arrays;

/**
 * Thrown in case a {@link ICapturedImage} can't be read from the disk.
 */
public class CapturedImageReadFailure extends IOException {

    private static final long serialVersionUID = 64523113120L;

    private int[] _channels;

    public CapturedImageReadFailure(int[] channels) {
        this._channels = channels;
    }

    @Override
    public String getMessage() {
        return "Can't read captured image for channel(s) " + Arrays.toString(this._channels)
                + " due to corrupt Metadata or other IO issues.";
    }

}