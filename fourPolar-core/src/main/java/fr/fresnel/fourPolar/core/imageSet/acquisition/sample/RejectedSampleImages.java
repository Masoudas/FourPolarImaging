package fr.fresnel.fourPolar.core.imageSet.acquisition.sample;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A container that holds the rejected captured images of a channel.
 */
public class RejectedSampleImages {
    private Hashtable<File, String> _rejectedFiles = new Hashtable<File, String>();
    
    /**
     * A container that holds the rejected captured images of a channel.
     */
    public RejectedSampleImages(){

    };

    /**
     * Add a captured images together with a message as to why it was rejected.
     * @param image
     * @param message
     */
    public void addImage(File image, String message){
        _rejectedFiles.put(image, message);
    }

    /**
     * Allows several files to be added with the same message.
     * @param message
     * @param images
     */
    public void addImage(String message, File...images) {
        for (File image : images) {
            this.addImage(image, message);
        }
    }

    /**
     * Get all the images as an enumeration.
     * @return
     */
    public Enumeration<File> getFiles(){
        return this._rejectedFiles.keys();
    }

    /**
     * Get the reason why this file was rejected.
     * @param file
     * @return
     */
    public String getMessage(File file){
        return this._rejectedFiles.get(file);
    }

    
}