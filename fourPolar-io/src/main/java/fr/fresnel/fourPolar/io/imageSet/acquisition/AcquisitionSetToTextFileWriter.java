package fr.fresnel.fourPolar.io.imageSet.acquisition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.fresnel.fourPolar.core.PathFactoryOfProject;
import fr.fresnel.fourPolar.core.image.captured.file.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.AcquisitionSet;
import fr.fresnel.fourPolar.core.imagingSetup.IFourPolarImagingSetup;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.AcquisitionSetWriteError;
import fr.fresnel.fourPolar.io.image.captured.file.ICapturedImageFileSetTextAdapter;

/**
 * Using this class, we can write an {@link AcquisitionSet} to text as a set of
 * text files, each corresponding to one {@link ICapturedImageFileSet} in the
 * folder specified by {@link PathFactoryOfProject#getFolder_Params()}. The
 * content of each individual text file is as provided by
 * {@link ICapturedImageFileSetTextAdapter#toString()}, except that we apply
 * some cosmetic operations to make the text file appear nicer.
 */
public class AcquisitionSetToTextFileWriter {
    /**
     * Number of spaces we place before writing a line that corresponds to a file
     * name in the captured set.
     */
    public static final int SPACE_BEFORE_FILE_LINE = 10;

    /**
     * Determines how many lines we leave empty after having written a group of
     * files in the captured set.
     */
    public static final int NLINESPACE_AFTER_GROUP = 1;

    private final ICapturedImageFileSetTextAdapter _textAdapter;
    private final String _spaceBeforeFile;
    private final AcquisitionSet _acquisitionSet;

    public AcquisitionSetToTextFileWriter(AcquisitionSet acquisitionSet, IFourPolarImagingSetup setup) {
        _acquisitionSet = acquisitionSet;

        _textAdapter = new ICapturedImageFileSetTextAdapter(setup);
        _spaceBeforeFile = _createSpaceString();

    }

    private String _createSpaceString() {
        return Stream.generate(() -> " ").limit(SPACE_BEFORE_FILE_LINE).collect(Collectors.joining());
    }

    /**
     * Writes the acqusition set to disk.
     * 
     * @param root4PProject is the location of the 4Polar folder of the project
     *                      {@see PathFactoryOfProject}.
     * @throws AcquisitionSetWriteError is thrown if at least one captured image set
     *                                  can't be written to disk.
     */
    public void write() throws AcquisitionSetWriteError {
        File textFilesRoot = this._getTextFilesRootFolder();
        AcquisitionSetWriteError exception = new AcquisitionSetWriteError();

        for (Iterator<ICapturedImageFileSet> fileSetsItr = _acquisitionSet.getIterator(); fileSetsItr.hasNext();) {
            ICapturedImageFileSet fileSet = fileSetsItr.next();
            try {
                _writeCapturedImageSet(fileSet, textFilesRoot);
            } catch (IOException e) {
                exception.setFailedSets(fileSet.getSetName());
            }
        }

        _throwExceptionIfFailureOccured(exception);
    }

    private void _writeCapturedImageSet(ICapturedImageFileSet set, File textFilesRoot) throws IOException {
        try (BufferedWriter writer = _getSetFileWriter(set, textFilesRoot);) {
            for (Iterator<String[]> setAsStringItr = _getCapturedImageSetAsString(set); setAsStringItr.hasNext();) {
                String[] groupAsText = setAsStringItr.next();

                writer.write(groupAsText[0] + "\n"); // Write channel line;
                for (int fileInfo = 1; fileInfo <= groupAsText.length; fileInfo++) {
                    writer.write(_spaceBeforeFile + groupAsText[fileInfo] + "\n");
                }

                for (int linesAfterGroup = 0; linesAfterGroup < NLINESPACE_AFTER_GROUP; linesAfterGroup++) {
                    writer.write("\n");
                }
            }
        }
    }

    private BufferedWriter _getSetFileWriter(ICapturedImageFileSet set, File textFilesRoot) throws IOException {
        File setTextFile = _createCapturedSetTextFile(textFilesRoot, set.getSetName());
        return new BufferedWriter(new FileWriter(setTextFile));
    }

    private Iterator<String[]> _getCapturedImageSetAsString(ICapturedImageFileSet set) {
        return _textAdapter.toString(set);
    }

    private File _getTextFilesRootFolder() {
        File root4PProject = _acquisitionSet.rootFolder();
        File rootFolder = new File(PathFactoryOfProject.getFolder_Params(root4PProject),
                _acquisitionSet.setType().description);

        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        return rootFolder;
    }

    private File _createCapturedSetTextFile(File textFilesRoot, String setName) throws IOException {
        File setTextFile = new File(textFilesRoot, setName);

        if (setTextFile.exists()) {
            setTextFile.delete();
        }

        setTextFile.createNewFile();

        return setTextFile;
    }

    /**
     * Simply activates this exception if at least one failure has occured.
     * 
     * @throws AcquisitionSetWriteError
     */
    private void _throwExceptionIfFailureOccured(AcquisitionSetWriteError exception) throws AcquisitionSetWriteError {
        if (exception.hasFaileSets()) {
            throw exception;
        }
    }

}