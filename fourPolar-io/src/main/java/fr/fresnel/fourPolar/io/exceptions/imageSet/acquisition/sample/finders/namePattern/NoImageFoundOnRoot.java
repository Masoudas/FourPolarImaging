package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.namePattern;

import java.io.IOException;

/**
 * Thrown when no images of the specified root is found on the root folder.
 */
public class NoImageFoundOnRoot extends IOException {
    private static final long serialVersionUID = 536871008L;

    public NoImageFoundOnRoot(String message) {
        super(message);
    }
    
}