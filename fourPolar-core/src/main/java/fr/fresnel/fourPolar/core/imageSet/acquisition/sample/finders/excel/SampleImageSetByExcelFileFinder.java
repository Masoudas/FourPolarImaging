package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;
import fr.fresnel.fourPolar.io.imageSet.acquisition.sample.TemplateExcelFileGenerator;

/**
 * This class is used for finding the images of the sample set from an excel
 * file, which is dedicated to one channel.
 */
public class SampleImageSetByExcelFileFinder {
    private IImageChecker imageChecker = null;
    private File rootFolder;

    public SampleImageSetByExcelFileFinder(IImageChecker imageChecker, File rootFolder) {
        this.imageChecker = imageChecker;
        this.rootFolder = rootFolder;

    }

    /**
     * Read the excel file provided and adds the images to the sample set.
     * <p>
     * The excel file must have the same format as provided by {@link TemplateExcelFileGenerator}
     * @param sampleImageSet : Sample image set to be filled.
     * @param channel : Channel number.
     * @param channelFile : The path to the corresponding excel file.
     * @throws IOException
     */
    public void findChannelImages(SampleImageSet sampleImageSet, int channel, File channelFile) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(channelFile))){
            Sheet sheet = workbook.getSheetAt(0);

            int titleRow = TemplateExcelFileGenerator.getTitleRow();
            int nColumns = sheet.getRow(titleRow).getLastCellNum();
            
            if (nColumns != 1 && nColumns !=2 && nColumns != 4)
                throw new IOException("The excel file does not have the required format.");
            
            for (int rowCtr = 4; rowCtr <= sheet.getLastRowNum(); rowCtr++) {
                Row row = sheet.getRow(rowCtr);    
                File[] files = createFiles(nColumns, row);
                
                if (this.imagesExistAndCompatible(files))
                {
                    CapturedImageFileSet fileSet = this.createFileSet(files);
                    sampleImageSet.addImage(channel, fileSet);
                }                
            }    
        } 
    }

    private File[ ] createFiles(int nColumns, Row row) {
        File[ ] files = new File[nColumns];
        for (int cellCtr = 0; cellCtr < nColumns; cellCtr++){
            Cell cell = row.getCell(cellCtr);
            files[cellCtr] = new File(this.rootFolder, cell.getStringCellValue());
        }

        return files;
    }

    private boolean imagesExistAndCompatible(File[] files) {
        for (File file : files){
            if (!file.exists() || !imageChecker.checkCompatible(file))
                return false;
        }
        return true;
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