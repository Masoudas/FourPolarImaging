package fr.fresnel.fourPolar.core.image;

import java.util.function.Supplier;

/**
 * An interface for supplying new instances of image plane. The {@link #get()}
 * method of supplier must return a new instance of image plane, that is
 * consistent with the demanded metadata.
 * 
 * @param <T> is the image plane type.
 */
public interface ImagePlaneSupplier<T> extends Supplier<T> {

}