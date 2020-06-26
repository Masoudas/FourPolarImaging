package fr.fresnel.fourPolar.io.image.captured.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Provides an string representation of an {@link ICapturedImageFileSet} that
 * can be used to write this set to disk.
 * 
 * The convention used is that each group of {@link ICapturedImageFile} that
 * correspond to the four polarizations are converted into the following
 * strings:
 * <ul>
 * <ul>
 * {@link Cameras#One} case:
 * <li>String 0 : "Channels : [c1, c2, ...]", that indicates the channels the
 * files correspond to.</li>
 * <li>String 1 : "Pol0_45_90_135 : file.xxx", the file that corresponds to
 * pol0, 45, 90 and 135.</li>
 * </ul>
 * 
 * <ul>
 * {@link Cameras#Two} case:
 * <li>String 0 : "Channels : [c1, c2, ...]", that indicates the channels the
 * files correspond to.</li>
 * <li>String 1 : "Pol0_90 : file.xxx", the file that corresponds to pol0 and
 * 90.</li>
 * <li>String 2 : "Pol45_135 : file.xxx", the file that corresponds to pol45 and
 * 135.</li>
 * </ul>
 * 
 * <ul>
 * {@link Cameras#Four} case:
 * <li>String 0 : "Channels : [c1, c2, ...]", that indicates the channels the
 * files correspond to.</li>
 * <li>String 1 : "Pol0 : file.xxx", the file that corresponds to pol0.</li>
 * <li>String 2 : "Pol45 : file.xxx", the file that corresponds to pol45.</li>
 * <li>String 3 : "Pol90 : file.xxx", the file that corresponds to pol90.</li>
 * <li>String 4 : "Pol135 : file.xxx", the file that corresponds to pol135.</li>
 * </ul>
 * </ul>
 * 
 * Note that for {@link Cameras#One}, all pol filess would be equal, and for
 * {@link Cameras#Two}, pol0 is equal to pol90 and pol45 is equal to pol135.
 */
public class ICapturedImageFileSetToTextAdapter {
    /**
     * The index where first channel number resides.
     */
    public static final int _CHANNEL_NO_START = 11;

    /**
     * The index where pol0_45_90_135 resides.
     */
    public static final int _POL0_45_90_135_START = 16;

    /**
     * The index where pol0_90 resides.
     */
    public static final int _POL0_90_START = 9;

    /**
     * The index where pol45_135 resides.
     */
    public static final int _POL45_135_START = 11;

    /**
     * The index where pol0 resides.
     */
    public static final int _POL0_START = 6;

    /**
     * The index where pol45 resides.
     */
    public static final int _POL45_START = 7;

    /**
     * The index where pol90 resides.
     */
    public static final int _POL90_START = 7;

    /**
     * The index where pol135 resides.
     */
    public static final int _POL135_START = 8;

    private final Cameras _camera;

    public ICapturedImageFileSetToTextAdapter(IFourPolarImagingSetup setup) {
        Objects.requireNonNull(setup, "setup can't be null");
        _camera = setup.getCameras();
    }

    /**
     * @return an iterator for string representation of each group of files that
     *         correspond to the four polarizations. Each iteration returns one
     *         group of such files.
     */
    public Iterator<String[]> toString(ICapturedImageFileSet capturedImageFileSet) {
        Objects.requireNonNull(capturedImageFileSet, "capturedImageFileSet can't be null");

        ArrayList<String[]> groupRepresenters = new ArrayList<>();

        Map<int[], Map<String, File>> capturedImageGroups = _groupCapturedImageGroups(capturedImageFileSet);
        for (Map.Entry<int[], Map<String, File>> capturedGroup : capturedImageGroups.entrySet()) {
            String[] groupAsString = _getStringRepresentationOfCapturedImageGroup(capturedGroup.getValue(),
                    capturedGroup.getKey());

            groupRepresenters.add(groupAsString);
        }

        return groupRepresenters.iterator();
    }

    private Map<int[], Map<String, File>> _groupCapturedImageGroups(ICapturedImageFileSet capturedSet) {
        Map<int[], Map<String, File>> capturedImageGroups = new HashMap<>();
        for (int groupNo = 0; groupNo < this._nFileGroups(capturedSet); groupNo++) {
            Map<String, File> groupFiles = _getGroupFiles(capturedSet, groupNo);
            int[] channels = _getGroupChannels(capturedSet, groupNo);

            capturedImageGroups.put(channels, groupFiles);
        }

        return capturedImageGroups;
    }

    private Map<String, File> _getGroupFiles(ICapturedImageFileSet capturedSet, int groupNo) {
        HashMap<String, File> groupFiles = new HashMap<>();
        for (String label : Cameras.getLabels(_camera)) {
            groupFiles.put(label, capturedSet.getFile(label)[groupNo].file());
        }
        return groupFiles;
    }

    private int[] _getGroupChannels(ICapturedImageFileSet capturedSet, int groupNo) {
        String label0 = Cameras.getLabels(_camera)[0];
        return capturedSet.getFile(label0)[groupNo].channels();
    }

    private String[] _getStringRepresentationOfCapturedImageGroup(Map<String, File> capturedImageGroup, int[] channel) {
        String[] representer = new String[_nStringPerGroup()];

        int repLineCtr = 0;
        representer[repLineCtr++] = "Channels: " + Arrays.toString(channel);

        for (String label : Cameras.getLabels(_camera)) {
            representer[repLineCtr++] = label + ": " + capturedImageGroup.get(label).getPath();
        }

        return representer;
    }

    /**
     * @return the number of strings required to represent each group.
     */
    private int _nStringPerGroup() {
        return _nCameraLabels() + 1;
    }

    /**
     * Using the first label of this camera, determine how many groups of four
     * polarization are present in this set. This would correspond to the length of
     * {@link ICapturedImage} for this label.
     */
    private int _nFileGroups(ICapturedImageFileSet capturedSet) {
        return capturedSet.getFile(Cameras.getLabels(_camera)[0]).length;

    }

    private int _nCameraLabels() {
        return Cameras.getLabels(_camera).length;
    }

}