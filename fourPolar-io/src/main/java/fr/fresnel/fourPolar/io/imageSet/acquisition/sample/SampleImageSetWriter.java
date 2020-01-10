package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.PathFactory;

/**
 * Used for writing the SampleImageSet to disk. The image files are written as
 * excel files in the path provided by the {@link PathFactory} class for each channel.
 */
public class SampleImageSetWriter {
    private File rootFolder;
    private String[] labels;
    SampleImageSet sampleSet;

    public SampleImageSetWriter(SampleImageSet sampleSet, File rootFolder) {
        this.sampleSet = sampleSet;

        Cameras camera = sampleSet.getImagingSetup().getCameras();
        this.labels = Cameras.getLabels(camera);
        this.rootFolder = rootFolder;

    }

    public void write() throws IOException, FileNotFoundException {
        for (int channel = 0; channel < this.sampleSet.getImagingSetup().getnChannel(); channel++) {
            this.writeChannel(channel);
        }
    }

    private void writeChannel(int channel) throws IOException, FileNotFoundException {
        File channelFile = this.getChannelFile(channel);
        channelFile.createNewFile();

        
        try (FileOutputStream fStream = new FileOutputStream(channelFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            this.writeTitleRow(sheet);

            int rowIndex = 1;
            Set<ICapturedImageFileSet> channelImages = this.sampleSet.getChannelImages(channel);
            for (ICapturedImageFileSet fileSet : channelImages) {
                this.writeImageSet(sheet, rowIndex, fileSet);
                rowIndex++;
            }

            workbook.write(fStream);
        }
    }

    private File getChannelFile(int channel) {
        String fileName = "Channel" + channel + ".xlsx";

        File sampleSetFolder = PathFactory.getFolder_SampleSet(this.rootFolder);
        return new File(sampleSetFolder, fileName);
    }

    private void writeTitleRow(Sheet sheet) {
        Row firstRow = sheet.createRow(0);

        int column = 0;
        firstRow.createCell(column).setCellValue("Set Name");
        for (++column; column < this.labels.length;) {
            firstRow.createCell(column).setCellValue(labels[column - 1]);
        }
    }

    private void writeImageSet(Sheet sheet, int rowIndex, ICapturedImageFileSet fileSet) {
        Row row = sheet.createRow(rowIndex);

        int column = 0;
        row.createCell(column).setCellValue(fileSet.getSetName());
        for (++column; column <= this.labels.length; ) {
            row.createCell(column).setCellValue(fileSet.getFile(labels[column - 1]).getAbsolutePath());
        }
    }
}