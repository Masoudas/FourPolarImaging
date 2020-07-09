package fr.fresnel.fourPolar.core.image.vector.filter;

import java.util.Iterator;
import java.util.Optional;

/**
 * As we know, each filter element in SVG consists of at least one sub-filter
 * (feBlend for example). This is an interface for accessing a composite of
 * several such sub-filters, where each sub-filter is defined using a
 * {@link Filter}. A filter composite has the following general element format
 * in SVG, <filter id= x= y= width= height=> which is modeled with this
 * interface. Moreover, each filter component can be accessed by the iterator.
 * It's the job of the user to ensure that this filter structure makes sense and
 * can be applied to the image.
 */
public interface FilterComposite {
    /**
     * @return the id name of this filter.
     */
    public String id();

    /**
     * @return the starting x-pose of where this filter is applied.
     */
    public Optional<String> xStart();

    /**
     * @return the starting y-pose of where this filter is applied.
     */
    public Optional<String> yStart();

    /**
     * @return the percentage of width to which this filter is applied.
     */
    public Optional<String> widthPercent();

    /**
     * @return the percentage of height to which this filter is applied.
     */
    public Optional<String> heightPercent();

    /**
     * @return the list of filters that are in this composite. There's always at
     *         least one filter in the composite.
     */
    public Iterator<Filter> filters();
}