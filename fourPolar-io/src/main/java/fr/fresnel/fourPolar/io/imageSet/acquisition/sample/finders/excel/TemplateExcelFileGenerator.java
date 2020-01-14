package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Using this class, we can generate a template excel file, for defining the
 * sample set images.
 */
public class TemplateExcelFileGenerator {
    private static int titleRow = 3; // This is the actuall title row in excel file, minus 1.

    private Cameras camera;
    private File folder;
    private String[] comments = null;

    /**
     * Default constructor. Generate a template excel file, for defining the
     * sample set images.
     * @param camera
     * @param rootFolder
     */
    public TemplateExcelFileGenerator(Cameras camera, File rootFolder) {
        this.camera = camera;
        this.folder = this._getFolder(rootFolder);

        comments = new String[titleRow - 1];    // Don't touch this. Change the titleRow!
        comments[0] = "Put the COMPLETE path to the images of the channel under the corresponding column.";
        comments[1] = "The files in each row must correspond to different polarizations of same sample.";

    }

    /**
     * To create a template file from scratch, where each string would be written in
     * four cells of the first rows.
     * 
     * @param camera
     */
    public TemplateExcelFileGenerator(Cameras camera, File rootFolder, String... comments) {
        this(camera, rootFolder);
        titleRow = comments.length;

        this.comments = comments;
    }

    public boolean createChannelFile(int channel) throws IOException{
        File outputFile = this.getFileName(channel);
        
        if (!outputFile.createNewFile())
            return false;

        try (FileOutputStream stream = new FileOutputStream(outputFile))
        {
            XSSFWorkbook workBook = new XSSFWorkbook();
            Sheet sheet = workBook.createSheet();

            this.writeComments(sheet);
            this.writeTitleRow(sheet);

            workBook.write(stream);
            workBook.close();
            return true;
        }
    }

    /**
     * Creates and returns the folder that would contain the template excel files.
     * @param rootFolder
     * @return
     */
    private File _getFolder(File rootFolder) {
        return new File(rootFolder.getAbsolutePath());
    }

    private File getFileName(int channel) {
        String fileName = "SampleImages_Channel" + channel + ".xlsx";
        return new File(this.folder, fileName);
    }

    /**
     * Writes comments to the excel file.
     * 
     * @param sheet
     */
    private void writeComments(Sheet sheet) {
        for (int ctr = 0; ctr < comments.length; ctr++) {
            Row row = sheet.createRow(ctr);
            for (int cellctr = 0; cellctr < 6; cellctr++) row.createCell(cellctr);
            
            row.getCell(0).setCellValue(comments[ctr]);
            sheet.addMergedRegion(new CellRangeAddress(ctr, ctr, 0, 5));
        }
    }

    private void writeTitleRow(Sheet sheet) {
        String[] labels = Cameras.getLabels(camera);
        Row row = sheet.createRow(getTitleRowIndex());

        for (int i = 0; i < labels.length; i++) {
            row.createCell(i).setCellValue(labels[i]);
            sheet.autoSizeColumn(i);
        }

    }

    /**
     * Returns the row in the excel files that defines the labels.
     * 
     * @return row number
     */
    public static int getTitleRowIndex() {
        return titleRow;
    }

}