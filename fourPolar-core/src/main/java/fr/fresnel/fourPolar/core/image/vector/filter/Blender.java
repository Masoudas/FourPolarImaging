package fr.fresnel.fourPolar.core.image.vector.filter;

/**
 * Defines a blender filter that can be used for blending colors.
 */
public class Blender implements Filter {
    /**
     * Indicates the element that should be used as the source of blending
     * operation.
     */
    public enum IN {
        SOURCE_GRAPHIC {
            public String value() {
                return "SourceGraphic";
            }
        },
        BACKGROUND_IMAGE {
            public String value() {
                return "BackgroundImage";
            }
        },
        NO_SOURCE {
            public String value() {
                return null;
            }
        };

        abstract public String value();
    }

    /**
     * Indicates possible blending modes.
     */
    public enum Mode {
        MULTIPLY {
            public String value() {
                return "multiply";
            }
        },
        Darken {
            public String value() {
                return "darken";
            }
        },
        Lighten {
            public String value() {
                return "lighten";
            }
        },
        COLOR_DODGE {
            public String value() {
                return "color-dodge";
            }
        },
        SCREEN {
            public String value() {
                return "screean";
            }
        },
        OVERLAY {
            public String value() {
                return "overlay";
            }
        },
        COLOR_BURN {
            public String value() {
                return "color-burn";
            }

        },
        HARD_LIGHT {
            public String value() {
                return "hard-light";
            }
        },
        SOFT_LIGHT {
            public String value() {
                return "soft-light";
            }
        },
        DIFFERENCE {
            @Override
            public String value() {
                return "difference";
            }

        },
        EXCLUSION {
            public String value() {
                return "exclusion";
            }
        },
        HUE {
            @Override
            public String value() {
                return "hue";
            }

        },
        SATURATION {
            @Override
            public String value() {
                return "saturation";
            }
        },
        COLOR {
            @Override
            public String value() {
                return "color";
            }

        },
        LUMINOSITY {
            @Override
            public String value() {
                return "luminosity";
            }
        };

        abstract public String value();
    }

    private final IN _in;
    private final IN _in2;
    private final Mode _mode;

    /**
     * Define a blender filter by defining two sources and a mode. First source
     * can't be {@link IN#NO_SOURCE}. If second source is {@link IN#NO_SOURCE}, it
     * would imply that no source is added.
     * 
     * @param in is blender in parameter.
     * @param in2 is blender in2 parameter.
     * @param mode is the blending mode.
     */
    public Blender(IN in, IN in2, Mode mode) {
        _checkInIsNotNoSource(in);

        _in = in;
        _in2 = in2;
        _mode = mode;

    }

    public void _checkInIsNotNoSource(IN in) {
        if (in == IN.NO_SOURCE){
            throw new IllegalArgumentException("in can't be no source.");
        }
        
    }

    public IN in() {
        return _in;
    }

    public IN in2() {
        return _in2;
    }

    public Mode mode() {
        return _mode;
    }
}