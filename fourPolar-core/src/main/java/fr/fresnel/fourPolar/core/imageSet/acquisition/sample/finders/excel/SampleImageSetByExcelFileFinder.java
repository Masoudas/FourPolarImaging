package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.fresnel.fourPolar.core.imageSet.acquisition.CapturedImageFileSet;
import fr.fresnel.fourPolar.core.imageSet.acquisition.sample.SampleImageSet;
import fr.fresnel.fourPolar.io.image.IImageChecker;

/**
 * This class is used for finding the images of the sample set from an excel
 * file, which is dedicated to one channel.
 */
public class SampleImageSetByExcelFileFinder {
    private IImageChecker imageChecker = null;

    public SampleImageSetByExcelFileFinder(IImageChecker imageChecker, File rootFolder) {
        this.imageChecker = imageChecker;
    }

    public void findChannelImages(SampleImageSet sampleImageSet, int channel, File channelFile)
            throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(channelFile);

        // raise exception for number of columns
        int nColumns;
        File[ ] files = new File[nColumns];
        
        for (String str : row) {
            for (int ctr = 0; ctr < nColumns; ctr++){
                files[ctr] = new File(pathname);
            }
            
            if (this.imagesExistAndCompatible(files))
            {
                CapturedImageFileSet fileSet = this.createFileSet(files);
                sampleImageSet.addImage(channel, fileSet);
            }                
        }
    }

    private boolean imagesExistAndCompatible(File[] files) {
        for (File file : files){
            if (file.exists() && imageChecker.checkCompatible(file))
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