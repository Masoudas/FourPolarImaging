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
    private Cameras camera;
    private File folder;
    private String[] comments_examplePage = null;
    private String[] comments_filePage = null;
    private int _nCommentColumns;

    /**
     * Default constructor. Generate a template excel file, for defining the sample
     * set images. The second page is the example page.
     * 
     * @param camera
     * @param rootFolder
     */
    public TemplateExcelFileGenerator(Cameras camera, File rootFolder) {
        this.camera = camera;
        this.folder = this._getFolder(rootFolder);
        this._nCommentColumns = 8;

    }

    public boolean createChannelFile(int channel) throws IOException {
        _generateComments(channel);
        File outputFile = this.getFileName(channel);

        outputFile.delete();
        if (!outputFile.createNewFile())
            return false;

        try (FileOutputStream stream = new FileOutputStream(outputFile)) {
            XSSFWorkbook workBook = new XSSFWorkbook();
            Sheet sheet1 = workBook.createSheet("FileList");
            this.writeComments(sheet1, this.comments_filePage);
            this.writeRow(sheet1, Cameras.getLabels(camera), this.comments_filePage.length + 1);

            Sheet sheet2 = workBook.createSheet("ExamplePage");
            this.writeComments(sheet2, this.comments_examplePage);
            this.writeRow(sheet2, Cameras.getLabels(camera), this.comments_examplePage.length + 1);
            this.writeRow(sheet2, this._getExampleFileNames(), this.comments_examplePage.length + 2);

            workBook.write(stream);
            workBook.close();
            return true;
        }
    }

    /**
     * Creates and returns the folder that would contain the template excel files.
     * 
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

    private void _generateComments(int channel) {
        comments_examplePage = new String[3];
        comments_examplePage[0] = "This page serves as an example of how to fill this excel file.";
        comments_examplePage[1] = "Put the COMPLETE path to the images of channel " + channel + " in each row.";
        comments_examplePage[2] = "Ensure that the title row is always present before all file names, otherwise the files would not be detected.";

        comments_filePage = new String[1];
        comments_filePage[0] = "Refer to the next sheet on instructions for filling this excel file";
    }

    /**
     * Writes comments to the excel file, and specify how many columns they should
     * occupy. Each comment would be written in one row.
     * 
     * @param sheet
     * @param comments
     */
    private void writeComments(Sheet sheet, String[] comments) {
        for (int ctr = 0; ctr < comments.length; ctr++) {
            Row row = sheet.createRow(ctr);
            for (int cellctr = 0; cellctr < this._nCommentColumns; cellctr++)
                row.createCell(cellctr);

            row.getCell(0).setCellValue(comments[ctr]);
            sheet.addMergedRegion(new CellRangeAddress(ctr, ctr, 0, this._nCommentColumns - 1));
        }
    }

    /**
     * Writes an array of strings into a row
     * 
     * @param sheet
     * @param labels
     */
    private void writeRow(Sheet sheet, String[] labels, int rownum) {
        Row row = sheet.createRow(rownum);

        for (int i = 0; i < labels.length; i++) {
            row.createCell(i).setCellValue(labels[i]);
            sheet.autoSizeColumn(i);
        }

    }

    /**
     * Returns example file names for each camera case.
     * 
     * @return
     */
    private String[] _getExampleFileNames() {
        if (Cameras.One == this.camera) {
            String[] fileName = { "C:\\rootFolder\\Img_Pol0_45_90_135.tif" };
            return fileName;
        } else if (Cameras.Two == this.camera) {
            String[] fileName = { "C:\\rootFolder\\Img_Pol0_90.tif", "C:\\rootFolder\\Img_Pol45_135.tif" };
            return fileName;
        } else {
            String[] fileName = { "C:\\rootFolder\\Img_Pol0.tif", "C:\\rootFolder\\Img_Pol45.tif", "C:\\rootFolder\\Img_Pol90.tif",
                    "C:\\rootFolder\\Img_Pol135.tif" };
            return fileName;
        }
    }

}