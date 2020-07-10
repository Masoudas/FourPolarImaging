package fr.fresnel.fourPolar.core.image.vector.filter;

import java.util.Objects;
import java.util.Optional;

/**
 * Defines a blender filter that can be used for blending colors.
 */
public class BlenderFilter implements Filter {
    /**
     * Indicates possible blending modes.
     */
    public enum Mode {
        MULTIPLY {
            public String modeAsString() {
                return "multiply";
            }
        },
        Darken {
            public String modeAsString() {
                return "darken";
            }
        },
        Lighten {
            public String modeAsString() {
                return "lighten";
            }
        },
        COLOR_DODGE {
            public String modeAsString() {
                return "color-dodge";
            }
        },
        SCREEN {
            public String modeAsString() {
                return "screean";
            }
        },
        OVERLAY {
            public String modeAsString() {
                return "overlay";
            }
        },
        COLOR_BURN {
            public String modeAsString() {
                return "color-burn";
            }

        },
        HARD_LIGHT {
            public String modeAsString() {
                return "hard-light";
            }
        },
        SOFT_LIGHT {
            public String modeAsString() {
                return "soft-light";
            }
        },
        DIFFERENCE {
            @Override
            public String modeAsString() {
                return "difference";
            }

        },
        EXCLUSION {
            public String modeAsString() {
                return "exclusion";
            }
        },
        HUE {
            @Override
            public String modeAsString() {
                return "hue";
            }

        },
        SATURATION {
            @Override
            public String modeAsString() {
                return "saturation";
            }
        },
        COLOR {
            @Override
            public String modeAsString() {
                return "color";
            }

        },
        LUMINOSITY {
            @Override
            public String modeAsString() {
                return "luminosity";
            }
        };

        abstract public String modeAsString();
    }

    private final String _in;
    private final String _in2;
    private final String _mode;
    private final String _result;

    /**
     * Define a blender filter by defining two sources and a mode. Non of the
     * aforementioned parameters can be null. The resultName however can be set to
     * null.
     * 
     * @param in         is feblender in parameter.
     * @param mode       is feblender blending mode.
     * @param resultName is the result name tag of the feblender. It must be set to
     *                   null if no output tag is desired (and not empty string).
     */
    public BlenderFilter(IN in, Mode mode, String resultName) {
        Objects.requireNonNull(in, "in can't be null.");
        Objects.requireNonNull(mode, "mode can't be null.");

        _in = in.asString();
        _in2 = null;
        _mode = mode.modeAsString();
        _result = resultName;

    }

    /**
     * Define a blender filter by defining two sources and a mode. Non of the
     * aforementioned parameters can be null. The resultName however can be set to
     * null.
     * 
     * @param in         is feblender in parameter.
     * @param in2        is feblender in2 parameter.
     * @param mode       is feblender blending mode.
     * @param resultName is the result name tag of the feblender. It must be set to
     *                   null if no output tag is desired (and not empty string).
     */
    public BlenderFilter(IN in, IN in2, Mode mode, String resultName) {
        Objects.requireNonNull(in, "in can't be null.");
        Objects.requireNonNull(in2, "in2 can't be null.");
        Objects.requireNonNull(mode, "mode can't be null.");

        _in = in.asString();
        _in2 = in2.asString();
        _mode = mode.modeAsString();
        _result = resultName;

    }

    /**
     * @return the blending mode as string.
     */
    public String mode() {
        return _mode;
    }

    @Override
    public String in() {
        return _in;
    }

    @Override
    public Optional<String> in2() {
        return Optional.ofNullable(_in2);
    }

    @Override
    public Optional<String> resultName() {
        return Optional.ofNullable(_result);
    }
}