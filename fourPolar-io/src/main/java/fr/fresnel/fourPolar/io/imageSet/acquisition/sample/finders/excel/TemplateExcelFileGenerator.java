package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import java.io.File;
import java.io.FileNotFoundException;
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
    private int _nCommentColumns;

    /**
     * Default constructor. Generate a template excel file, for defining the sample
     * set images. The second page is the example page.
     * 
     * @param camera is the number of cameras
     * @param root4PProject is the location of the 4Polar folder of the project {@see PathFactoryOfProject}.
     */
    public TemplateExcelFileGenerator(Cameras camera, File root4PProject) {
        this.camera = camera;
        this.folder = this._getFolder(root4PProject);
        this._nCommentColumns = 8;

    }

    /**
     * Create a template for full channel image files (i.e, the case where a
     * {@link ICapturedImageFile} contains all channel.)
     * 
     * @return
     * @throws IOException
     */
    public boolean create() throws IOException {
        String[] comments_examplePage = _generateExamplePageComments();
        String[] comments_filePage = _generateFilePageComments();
        File outputFile = this.getFileName();

        return _create(outputFile, comments_examplePage, comments_filePage);
    }

    /**
     * Create a template for single channel image files (i.e, the case where a
     * {@link ICapturedImageFile} contains only one channel.)
     * 
     * @param channel is the desired channel.
     * @return true if file was created.
     * @throws IOException in case of low-level IO issues.
     */
    public boolean create(int channel) throws IOException {
        String[] comments_examplePage = _generateExamplePageComments(channel);
        String[] comments_filePage = _generateFilePageComments();
        File outputFile = this.getFileName(channel);

        return _create(outputFile, comments_examplePage, comments_filePage);
    }

    private boolean _create(File outputFile, String[] comments_examplePage, String[] comments_filePage)
            throws IOException, FileNotFoundException {
        outputFile.delete();
        if (!outputFile.createNewFile())
            return false;

        try (FileOutputStream stream = new FileOutputStream(outputFile)) {
            XSSFWorkbook workBook = new XSSFWorkbook();
            Sheet sheet1 = workBook.createSheet("FileList");
            this.writeComments(sheet1, comments_filePage);
            this.writeRow(sheet1, Cameras.getLabels(camera), comments_filePage.length + 1);

            Sheet sheet2 = workBook.createSheet("ExamplePage");
            this.writeComments(sheet2, comments_examplePage);
            this.writeRow(sheet2, Cameras.getLabels(camera), comments_examplePage.length + 1);
            this.writeRow(sheet2, this._getExampleFileNames(), comments_examplePage.length + 2);

            workBook.write(stream);
            workBook.close();
            return true;
        }
    }

    /**
     * Creates and returns the folder that would contain the template excel files.
     * 
     * @param root4PProject
     * @return
     */
    private File _getFolder(File root4PProject) {
        return new File(root4PProject.getAbsolutePath());
    }

    private File getFileName(int channel) {
        String fileName = "SampleImages_Channel" + channel + ".xlsx";
        return new File(this.folder, fileName);
    }

    private File getFileName() {
        String fileName = "SampleImages.xlsx";
        return new File(this.folder, fileName);
    }

    private String[] _generateExamplePageComments(int channel) {
        String[] comments_examplePage = new String[3];
        comments_examplePage[0] = "This page serves as an example of how to fill this excel file.";
        comments_examplePage[1] = "Put the COMPLETE path to the images of channel " + channel + " in each row.";
        comments_examplePage[2] = "Make sure that the title row is always present before all file names, otherwise the files would not be detected.";

        return comments_examplePage;
    }

    private String[] _generateFilePageComments() {
        String[] comments_filePage = new String[1];
        comments_filePage[0] = "Refer to the next sheet for instructions on how to fill this excel file";

        return comments_filePage;
    }

    private String[] _generateExamplePageComments() {
        String[] comments_examplePage = new String[3];
        comments_examplePage[0] = "This page serves as an example of how to fill this excel file.";
        comments_examplePage[1] = "Put the COMPLETE path to the images (each of which should have all channels) in each row.";
        comments_examplePage[2] = "Make sure that the title row is always present before all file names, otherwise the files would not be detected.";

        return comments_examplePage;
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
            String[] fileName = { "C:\\rootFolder\\Img_Pol0.tif", "C:\\rootFolder\\Img_Pol45.tif",
                    "C:\\rootFolder\\Img_Pol90.tif", "C:\\rootFolder\\Img_Pol135.tif" };
            return fileName;
        }
    }

}