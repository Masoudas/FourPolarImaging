package fr.fresnel.fourPolar.io.imageSet.acquisition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSetType;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.image.captured.file.CorruptCapturedImageSet;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetIOIssue;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetNotFound;
import fr.fresnel.fourPolar.io.image.captured.file.ICapturedImageFileSetTextAdapter;

/**
 * 
 * Reads an acquisition set that is written to the disk using
 * {@link AcquisitionSetToTextFileWriter}
 */
public class AcquisitionSetFromTextFileReader {
    private final int _nLinesForEachCapturedImageGroup;

    private final int _nSpaces_Before_Path_Line;

    private final ICapturedImageFileSetTextAdapter _adaptorToCapturedSet;

    public AcquisitionSetFromTextFileReader(IFourPolarImagingSetup setup) {
        this._nSpaces_Before_Path_Line = AcquisitionSetToTextFileWriter.SPACE_BEFORE_FILE_LINE;
        this._nLinesForEachCapturedImageGroup = _numLinesForEachCapturdImageGroup(setup.getCameras());
        this._adaptorToCapturedSet = new ICapturedImageFileSetTextAdapter(setup);
    }

    private int _numLinesForEachCapturdImageGroup(Cameras camera) {
        switch (camera) {
            case One:
                return AcquisitionSetToTextFileWriter.ONE_CAM_LINES_PER_GROUP;

            case Two:
                return AcquisitionSetToTextFileWriter.TWO_CAM_LINES_PER_GROUP;

            default:
                return AcquisitionSetToTextFileWriter.FOUR_CAM_LINES_PER_GROUP;
        }
    }

    /**
     * Populate an acquisition with the text files that are on the disk. The type of
     * acquisition set determines which folder should be searched.
     * 
     * @param setType        is an empty set to be populated by this reader.
     * @param acquisitionSet is the acquisition set to be populated. The type of the
     *                       set is used for reading the corresponding set from the
     *                       disk.
     * 
     * @throws AcquisitionSetNotFound   if the requested acquisition set does not
     *                                  exist on the disk.
     * @throws AcquisitionSetIOIssue    for any IO issues that may occur while
     *                                  reading the acquisition set (like issues
     *                                  with the text files, or that the captured
     *                                  images have been deleted. )
     * 
     * @throws IOException              if the requested acquisition set does not
     *                                  exist or is empy.
     * @throws IllegalArgumentException if the given acquisition set is not empty.
     * 
     */
    public void read(File root4PProject, AcquisitionSet acquisitionSet)
            throws AcquisitionSetNotFound, AcquisitionSetIOIssue {
        _checkAcquisitionSetIsEmpty(acquisitionSet);

        AcquisitionSetIOIssue exceptionIOIssue = new AcquisitionSetIOIssue(_getAcquisitionSetExceptionMessage());
        File[] capturedImageSets = _getCapturedImageSetsAsTextFilesOnRoot(root4PProject, acquisitionSet.setType());
        for (File capturedImageSet : capturedImageSets) {
            String setName = _getCapturedSetNameFromFileName(capturedImageSet);
            try {
                Iterator<String[]> capturedSetGroupsIterator = _groupCapturedImagesFromTextFileContent(
                        capturedImageSet);
                ICapturedImageFileSet fileSet = _createCapturedImageSet(setName, capturedSetGroupsIterator);

                acquisitionSet.addCapturedImageSet(fileSet);
            } catch (IOException | CorruptCapturedImageSet e) {
                exceptionIOIssue.addFailedSet(setName);
            }

        }

        _throwExceptionIfFailureOccured(exceptionIOIssue);

    }

    private ICapturedImageFileSet _createCapturedImageSet(String setName, Iterator<String[]> capturedSetGroupsIterator)
            throws CorruptCapturedImageSet {
        return this._adaptorToCapturedSet.fromString(capturedSetGroupsIterator, setName);
    }

    /**
     * Group the lines of the set text file together and return an iterator that can
     * be used for {@link ICapturedImageFileSetTextAdapter}
     * 
     * @throws IOException in case of IO issues reading the set file.
     */
    private Iterator<String[]> _groupCapturedImagesFromTextFileContent(File setFile) throws IOException {
        List<String> setFileContent = _readCapturedImageSet(setFile);
        ArrayList<String[]> capturedSetAsString = new ArrayList<>();

        String[] capturedImageGroup = new String[_nLinesForEachCapturedImageGroup];
        for (int index = 0; index < setFileContent.size(); index++) {
            if (index % _nLinesForEachCapturedImageGroup == 0) {
                capturedImageGroup = new String[_nLinesForEachCapturedImageGroup];
                capturedImageGroup[index] = setFileContent.get(index);
            } else if (index % _nLinesForEachCapturedImageGroup < _nLinesForEachCapturedImageGroup) {
                capturedImageGroup[index] = _removeSpacesFromFilePathLines(setFileContent.get(index));
            }
        }

        return capturedSetAsString.iterator();
    }

    private ArrayList<String> _readCapturedImageSet(File setFile) throws IOException {
        ArrayList<String> fileContent = new ArrayList<>();

        try (BufferedReader setReader = _getCapturedFileSetReader(setFile)) {
            String line;
            while ((line = setReader.readLine()) != null) {
                fileContent.add(line);
            }
        }

        return fileContent;
    }

    private String _removeSpacesFromFilePathLines(String lineContainingFilePath) {
        return lineContainingFilePath.substring(_nSpaces_Before_Path_Line + 1);
    }

    private BufferedReader _getCapturedFileSetReader(File setFile) {
        try {
            return new BufferedReader(new FileReader(setFile));
        } catch (FileNotFoundException e) {
            // This exception is never caught, because we first find the files
            // on the disk manually, and then try to read them.
            return null;
        }
    }

    /**
     * Looks for the text files that are on the root of acquisition set and then
     * returns them (without file extension), which are equivalent to captured image
     * set names.
     */
    private File[] _getCapturedImageSetsAsTextFilesOnRoot(File root4PProject, AcquisitionSetType setType)
            throws AcquisitionSetNotFound {
        File root = this._getAcquisitionSetRootFolder(root4PProject, setType);

        return root.listFiles(new TextFileFilter());
    }

    private File _getAcquisitionSetRootFolder(File root4PProject, AcquisitionSetType setType) {
        return AcquisitionSetToTextFileIOUtil._getTextFilesRootFolder(root4PProject, setType);
    }

    private String _getCapturedSetNameFromFileName(File capturedImageSet) {
        String fileName = capturedImageSet.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private void _checkAcquisitionSetIsEmpty(AcquisitionSet acquisitionSet) {
        if (acquisitionSet.setSize() != 0) {
            throw new IllegalArgumentException("AcquisitionSet must be empty to be populated by the reader.");
        }
    }

    /**
     * Simply activates this exception if at least one failure has occured.
     * 
     * @throws AcquisitionSetIOIssue
     */
    private void _throwExceptionIfFailureOccured(AcquisitionSetIOIssue exception) throws AcquisitionSetIOIssue {
        if (exception.hasFailedSets()) {
            throw exception;
        }
    }

    private String _getAcquisitionSetExceptionMessage() {
        return "At least one captured image set file can't be read from disk due to IO issues.";
    }
}