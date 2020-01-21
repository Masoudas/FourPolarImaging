package fr.fresnel.fourPolar.io.imageSet.acquisition.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imageSet.acquisition.ICapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.PathFactory;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.CorruptSampleSetExcel;

/**
 * Used for writing the SampleImageSet to disk. The image files are written as
 * excel files in the path provided by the {@link PathFactory} class for each
 * channel.
 */
public class SampleImageSetWriter {
    private File _sampleSetFolder;
    private String[] labels;
    private SampleImageSet sampleSet;

    /**
     * Returns the folder where the SampleImageSet would be written.
     * 
     * @param rootFolder
     * @return
     */
    public static File getSampleSetFolder(File rootFolder) {
        File zero_params_folder = PathFactory.getFolder_0_Params(rootFolder);
        return Paths.get(zero_params_folder.getAbsolutePath(), "Sample").toFile();
    }

    /**
     * Returns the name of the excel file corresponding to the channel.
     * 
     * @param channel
     * @return
     */
    public static String getChannelFileName(int channel) {
        return "Channel" + channel + ".xlsx";
    }

    /**
     * Used for writing the SampleImageSet to disk. The image files are written as
     * excel files in the path provided by the {@link PathFactory} class for each
     * channel.
     * 
     * @param sampleSet  : The sample set to be written.
     * @param rootFolder : The root folder of the four polar images. Note that this
     *                   file is written in the sampleSet subfolder of 0_Params
     *                   provided by {@link PathFactory}
     */
    public SampleImageSetWriter(SampleImageSet sampleSet, File rootFolder) {
        this.sampleSet = sampleSet;

        Cameras camera = sampleSet.getImagingSetup().getCameras();
        this.labels = Cameras.getLabels(camera);
        this._sampleSetFolder = this._createFolder_SampleSet(rootFolder);

    }

    /**
     * Write each channel images in the corresponding excel file.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void write() throws IOException, CorruptSampleSetExcel {
        for (int channel = 1; channel <= this.sampleSet.getImagingSetup().getnChannel(); channel++) {
            this.writeChannel(channel);
        }
    }

    private void writeChannel(int channel) throws IOException, CorruptSampleSetExcel {
        File channelFile = new File(this._sampleSetFolder, getChannelFileName(channel));
        try {
            channelFile.createNewFile();
        } catch (IOException e) {
            throw new IOException("Could not create the excel file for sample set.");
        }

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
            workbook.close();
        } catch (FileNotFoundException e) {
            throw new IOException("");
        } catch (IOException e) {
            throw new CorruptSampleSetExcel("");
        }

    }

    /**
     * Creates and returns the folder that would contain the sample set excel files.
     * 
     * @param rootFolder
     */
    private File _createFolder_SampleSet(File rootFolder) {
        File sampleSetFolder = getSampleSetFolder(rootFolder);
        sampleSetFolder.mkdirs();
        return sampleSetFolder;
    }

    /**
     * Write the title row, the first row of the excel file using the labels of
     * {@link Cameras}.
     * 
     * @param sheet
     */
    private void writeTitleRow(Sheet sheet) {
        Row firstRow = sheet.createRow(0);

        for (int column = 0; column < this.labels.length; column++) {
            firstRow.createCell(column).setCellValue(labels[column]);
        }
    }

    /**
     * Write given captured file set in the given row.
     * 
     * @param sheet
     * @param rowIndex
     * @param fileSet
     */
    private void writeImageSet(Sheet sheet, int rowIndex, ICapturedImageFileSet fileSet) {
        Row row = sheet.createRow(rowIndex);

        for (int column = 0; column < this.labels.length; column++) {
            row.createCell(column).setCellValue(fileSet.getFile(labels[column]).getAbsolutePath());
        }
    }
}