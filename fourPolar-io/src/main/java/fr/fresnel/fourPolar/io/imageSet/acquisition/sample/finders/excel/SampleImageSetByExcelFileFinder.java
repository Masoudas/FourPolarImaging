package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.ExcelIncorrentRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.MissingExcelTitleRow;
import fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel.TemplateSampleSetExcelNotFound;

/**
 * This class is used for finding the images of the sample set from an excel
 * file. The class first checks that each row has the same number of files as
 * the number of {@link Cameras}, and then returns each row. We can use the
 * {@link SampleImageSet} to build the set separately.
 * 
 * Note that this class is agnostic of how many channels are present in each
 * file (could be one or all).
 */
public class SampleImageSetByExcelFileFinder {
    final private Cameras _cameras;

    public SampleImageSetByExcelFileFinder(Cameras _cameras) {
        this._cameras = _cameras;
    }

    /**
     * Reads the excel file, and returns an iterator that iterators over each row of
     * the file.
     * <p>
     * The excel file must have the same format as provided by
     * {@link TemplateExcelFileGenerator}, otherwise an exception is raised.
     * 
     * @param channelFile is the path to the corresponding excel file.
     * @param cameras     is the number of cameras in the setup.
     * 
     * @throws IncorrectSampleSetExcelFormat  if the format of the file set (number
     *                                        of columns or title row) does not
     *                                        correspond to the generated template.
     * @throws TemplateSampleSetExcelNotFound is the excel file is not found on
     *                                        disk.
     * @return an iterator that iterato
     */
    public Iterator<File[]> read(File channelFile)
            throws TemplateSampleSetExcelNotFound, MissingExcelTitleRow, ExcelIncorrentRow {
        SampleImageSetExcelFileRowIterator itrCreator = new SampleImageSetExcelFileRowIterator();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(channelFile))) {
            Sheet sheet = workbook.getSheetAt(0);

            int titleRow = this._findTitleRow(sheet, this._cameras);
            int nImages = Cameras.getNImages(this._cameras);

            for (int rowCtr = titleRow + 1; rowCtr <= sheet.getLastRowNum(); rowCtr++) {
                Row row = sheet.getRow(rowCtr);
                File[] files = createFiles(nImages, row);
                itrCreator.add(files);
                
            }

        } catch (IOException e) {
            throw new TemplateSampleSetExcelNotFound("The template file does not exist or corrupted.");
        }

        return itrCreator.iterator();
    }

    /**
     * Create the files based on the strings in the row.
     * 
     * @param nColumns
     * @param row
     * @return
     * @throws IOException
     */
    private File[] createFiles(int nImages, Row row) throws ExcelIncorrentRow {
        short nColumns = row.getLastCellNum();
        if (nColumns != nImages) {
            int actualRow = row.getRowNum() + 1;
            throw new ExcelIncorrentRow(
                    "The excel file does not have " + nImages + " column(s) at the " + actualRow + "-th row");
        }

        File[] files = new File[nColumns];
        for (int cellCtr = 0; cellCtr < nColumns; cellCtr++) {
            Cell cell = row.getCell(cellCtr);
            files[cellCtr] = new File(cell.getStringCellValue());
        }

        return files;
    }

    private int _findTitleRow(Sheet sheet, Cameras camera) throws MissingExcelTitleRow {
        String[] titleRow = Cameras.getLabels(camera);
        int result = -1;
        for (int rowNum = 0; rowNum < sheet.getLastRowNum() & result < 0; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null && this._isTitleRow(camera, titleRow, row)) {
                result = rowNum;
            }
        }

        if (result < 0) {
            throw new MissingExcelTitleRow("The title row was not found in the excel file.");
        }

        return result;
    }

    /**
     * Checks whether the given row is the title row.
     * 
     * @param camera
     * @param titleRow
     * @param row
     * @return
     */
    private boolean _isTitleRow(Cameras camera, String[] titleRow, Row row) {
        boolean result = true;
        for (int columnNum = 0; columnNum < Cameras.getNImages(camera) && result == true; columnNum++) {
            String cellText = row.getCell(columnNum).getStringCellValue();
            if (!titleRow[columnNum].equals(cellText)) {
                result = false;
            }
        }

        return result;
    }

}