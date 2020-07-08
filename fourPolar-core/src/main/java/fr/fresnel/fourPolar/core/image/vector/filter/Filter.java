package fr.fresnel.fourPolar.core.image.vector.filter;

import java.util.Optional;

/**
 * An interface for defining SVG filters. Specific implementations of the filter
 * have their own methods for defining the attributes of the filter.
 */
public interface Filter {
    /**
     * Indicates a list of most famous in tags for filter.
     */
    public enum IN {
        SOURCE_GRAPHIC {
            public String asString() {
                return "SourceGraphic";
            }
        },
        BACKGROUND_IMAGE {
            public String asString() {
                return "BackgroundImage";
            }
        };

        abstract public String asString();
    }

    /**
     * @return the first source this filter will work on. This source is always non
     *         empty.
     */
    public String in();

    /**
     * @return the optional second source this filter would work on. This source can
     *         be empty.
     */
    public Optional<String> in2();

    /**
     * @return an optional indicating the result of the filter. The result can be
     *         empty.
     */
    public Optional<String> resultName();
}