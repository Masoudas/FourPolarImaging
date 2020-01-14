package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.FourPolarImagingSetup;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Using this class, we can read the SampleImageSet class from the disk. In
 * particular, the image files would be read from the sample folder in the
 * 0_params folder. See {@ SampleImageSetWriter}
 */
public class SampleImageSetReader {
    private FourPolarImagingSetup _imagingSetup;
    private SampleImageSet _sampleImageSet = null;
    private File _sampleSetFolder;

    /**
     * Using this class, we can read the SampleImageSet class from the disk. In
     * particular, the image files would be read from the sample folder in the
     * 0_params folder. See {@ SampleImageSetWriter}
     * 
     * @param imagingSetup
     * @param rootFolder
     */
    public SampleImageSetReader(FourPolarImagingSetup imagingSetup, File rootFolder) {
        this._imagingSetup = imagingSetup;
        this._sampleSetFolder = SampleImageSetWriter.getSampleSetFolder(rootFolder);
    }

    public SampleImageSet read() throws IOException {
        this._sampleImageSet = new SampleImageSet(this._imagingSetup);
        for (int channel = 1; channel <= this._imagingSetup.getnChannel(); channel++) {
            this.readChannel(channel);
        }

        return this._sampleImageSet;
    }

    /**
     * Reads each particular channel and puts it
     * 
     * @throws FileNotFoundException
     */
    private void readChannel(int channel) throws IOException {
        File channelFile = new File(this._sampleSetFolder, SampleImageSetWriter.getChannelFileName(channel));

        try (FileInputStream fStream = new FileInputStream(channelFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowCtr = 1; rowCtr <= sheet.getLastRowNum(); rowCtr++) {
                File[] files = this.createFiles(sheet.getRow(rowCtr));
                CapturedImageFileSet fileSet = this.createFileSet(files);
                this._sampleImageSet.addImage(channel, fileSet);
            }
            workbook.close();

        }
    }

    /**
     * Create the files from the path in the excel file.
     * 
     * @param row
     * @return
     * @throws IOException
     */
    private File[] createFiles(Row row) throws IOException {
        short nColumns = row.getLastCellNum();
        int nImages = Cameras.getNImages(this._imagingSetup.getCameras());

        if (nColumns != nImages) {
            int actualRow = row.getRowNum() + 1;
            throw new IOException(
                    "The excel file does not have " + nImages + " column(s) at the " + actualRow + "-th row");
        }

        File[] files = new File[nColumns];
        for (int cellCtr = 0; cellCtr < nColumns; cellCtr++) {
            Cell cell = row.getCell(cellCtr);
            files[cellCtr] = new File(cell.getStringCellValue());

            if (!files[cellCtr].exists()) {
                throw new IOException(files[cellCtr].getPath() + " does not exist on the root.");
            }

        }

        return files;
    }

    private CapturedImageFileSet createFileSet(File[] files) {
        if (files.length == 1)
            return new CapturedImageFileSet(files[0]);
        else if (files.length == 2)
            return new CapturedImageFileSet(files[0], files[1]);
        else
            return new CapturedImageFileSet(files[0], files[1], files[2], files[3]);
    }

}