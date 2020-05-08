package fr.fresnel.fourPolar.core.image.captured.file;

import java.io.File;
import java.util.Arrays;

/**
 * A concrete implementation of {@link ICapturedImageFile}.
 */
class CapturedImageFile implements ICapturedImageFile {
    final private int[] _channels;
    final private File _file;

    public CapturedImageFile(int[] channels, File file) {
        assert channels != null : "channels cannot be null";
        assert file != null : "file cannot be null";

        this._channels = channels;
        this._file = file;

        Arrays.sort(channels);
    }

    @Override
    public int[] channels() {
        return _channels;
    }

    @Override
    public File file() {
        return _file;
    }
    
}