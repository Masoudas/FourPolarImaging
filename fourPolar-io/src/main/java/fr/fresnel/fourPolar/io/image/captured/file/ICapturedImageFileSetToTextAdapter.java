package fr.fresnel.fourPolar.io.image.captured.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.fresnel.fourPolar.core.image.captured.ICapturedImage;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

class ICapturedImageFileSetToTextAdapter {
    private final Cameras _camera;

    public ICapturedImageFileSetToTextAdapter(Cameras camera) {
        _camera = camera;
    }

    /**
     * @return an iterator for string representation of each group of files that
     *         correspond to the four polarizations. Each iteration returns one
     *         group of such files.
     */
    public Iterator<String[]> toString(ICapturedImageFileSet capturedImageFileSet) {
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
        representer[repLineCtr++] = "Channels : " + Arrays.toString(channel);

        for (String label : Cameras.getLabels(_camera)) {
            representer[repLineCtr++] = label + ": " + capturedImageGroup.get(label).getAbsolutePath();
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