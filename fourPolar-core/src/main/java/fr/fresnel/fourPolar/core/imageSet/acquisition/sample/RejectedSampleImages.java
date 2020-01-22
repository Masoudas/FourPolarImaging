package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A container that holds the rejected captured images of a channel.
 */
public class RejectedSampleImages {
    private Hashtable<File, String> _rejectedFiles = new Hashtable<File, String>();
    
    public void addImage(File image, String message){
        _rejectedFiles.put(image, message);
    }

    public Enumeration<File> getFiles(){
        return this._rejectedFiles.keys();
    }

    public String getMessage(File file){
        return this._rejectedFiles.get(file);
    }

    
}