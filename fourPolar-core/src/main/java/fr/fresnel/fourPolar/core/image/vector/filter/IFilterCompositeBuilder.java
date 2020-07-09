package fr.fresnel.fourPolar.core.image.vector.filter;

import java.util.List;

/**
 * A private interface for accessing the parameters of the
 */
abstract class IFilterCompositeBuilder {
    /**
     * @return -1 if no start, else the positive value.
     */
    abstract int xStart();

    /**
     * @return -1 if no start, else the positive value.
     */
    abstract int yStart();

    /**
     * @return -1 if no percent, else the positive value.
     */
    abstract int widthPercent();

    /**
     * @return -1 if no percent, else the positive value.
     */
    abstract int heightPercent();

    abstract String id();

    abstract List<Filter> filters();
}