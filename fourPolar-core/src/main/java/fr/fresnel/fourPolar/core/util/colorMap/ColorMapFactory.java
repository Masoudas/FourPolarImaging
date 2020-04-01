package fr.fresnel.fourPolar.core.util.colorMap;

/**
 * Creates a colormap from the available list of colormaps.
 */
public class ColorMapFactory {
    public static final String IMAGEJ_ICA = "WCIF/ICA.lut";
    public static final String IMAGEJ_HILO = "WCIF/HiLo.lut";
    public static final String IMAGEJ_YELLOW = "Yellow.lut";
    public static final String IMAGEJ_RED_GREEN = "Red-Green.lut";
    public static final String IMAGEJ_ICA3 = "WCIF/ICA3.lut";
    public static final String IMAGEJ_3_3_2_RGB = "3-3-2 RGB.lut";
    public static final String IMAGEJ_ROYAL = "NCSA PalEdit/royal.lut";
    public static final String IMAGEJ_MAGENTA_HOT = "WCIF/Magenta Hot.lut";
    public static final String IMAGEJ_CYAN = "Cyan.lut";
    public static final String IMAGEJ_SEPIA = "NCSA PalEdit/sepia.lut";
    public static final String IMAGEJ_GRAYS = "Grays.lut";
    public static final String IMAGEJ_RAINBOW = "WCIF/Rainbow RGB.lut";
    public static final String IMAGEJ_CYAN_HOT = "WCIF/Cyan Hot.lut";
    public static final String IMAGEJ_GREEN = "Green.lut";
    public static final String IMAGEJ_GEM = "NCSA PalEdit/gem.lut";
    public static final String IMAGEJ_EDGES = "NCSA PalEdit/edges.lut";
    public static final String IMAGEJ_RED_HOT = "WCIF/Red Hot.lut";
    public static final String IMAGEJ_FIRE = "Fire.lut";
    public static final String IMAGEJ_THAL = "NCSA PalEdit/thal.lut";
    public static final String IMAGEJ_BRGBCMYW = "NCSA PalEdit/brgbcmyw.lut";
    public static final String IMAGEJ_SMART = "NCSA PalEdit/smart.lut";
    public static final String IMAGEJ_5_RAMPS = "NCSA PalEdit/5_ramps.lut";
    public static final String IMAGEJ_BLUE = "Blue.lut";
    public static final String IMAGEJ_MAGENTA = "Magenta.lut";
    public static final String IMAGEJ_SPECTRUM = "Spectrum.lut";
    public static final String IMAGEJ_THALLIUM = "NCSA PalEdit/thallium.lut";
    public static final String IMAGEJ_PHASE = "NCSA PalEdit/phase.lut";
    public static final String IMAGEJ_ICE = "Ice.lut";
    public static final String IMAGEJ_16_COLORS = "NCSA PalEdit/16_colors.lut";
    public static final String IMAGEJ_COOL = "NCSA PalEdit/cool.lut";
    public static final String IMAGEJ_ICA2 = "WCIF/ICA2.lut";
    public static final String IMAGEJ_BLUE_ORANGE_ICB = "NCSA PalEdit/blue_orange_icb.lut";
    public static final String IMAGEJ_ORANGE_HOT = "WCIF/Orange Hot.lut";
    public static final String IMAGEJ_UNIONJACK = "NCSA PalEdit/unionjack.lut";
    public static final String IMAGEJ_GREEN_FIRE_BLUE = "WCIF/Green Fire Blue.lut";
    public static final String IMAGEJ_RED = "Red.lut";
    public static final String IMAGEJ_6_SHADES = "NCSA PalEdit/6_shades.lut";
    public static final String IMAGEJ_YELLOW_HOT = "WCIF/Yellow Hot.lut";

    public static ColorMap create(String colorMap) {
        ColorMap cMap = null;
        switch (colorMap) {
            case IMAGEJ_ICA:
                cMap = new ImageJColorMap(IMAGEJ_ICA);
                break;

            case IMAGEJ_HILO:
                cMap = new ImageJColorMap(IMAGEJ_HILO);
                break;

            case (IMAGEJ_YELLOW):
                cMap = new ImageJColorMap(IMAGEJ_YELLOW);
                break;

            case (IMAGEJ_RED_GREEN):
                cMap = new ImageJColorMap(IMAGEJ_RED_GREEN);
                break;

            case (IMAGEJ_ICA3):
                cMap = new ImageJColorMap(IMAGEJ_ICA3);
                break;

            case (IMAGEJ_3_3_2_RGB):
                cMap = new ImageJColorMap(IMAGEJ_3_3_2_RGB);
                break;

            case (IMAGEJ_ROYAL):
                cMap = new ImageJColorMap(IMAGEJ_ROYAL);
                break;

            case (IMAGEJ_MAGENTA_HOT):
                cMap = new ImageJColorMap(IMAGEJ_MAGENTA_HOT);
                break;

            case (IMAGEJ_CYAN):
                cMap = new ImageJColorMap(IMAGEJ_CYAN);
                break;

            case (IMAGEJ_SEPIA):
                cMap = new ImageJColorMap(IMAGEJ_SEPIA);
                break;

            case (IMAGEJ_GRAYS):
                cMap = new ImageJColorMap(IMAGEJ_GRAYS);
                break;

            case (IMAGEJ_RAINBOW):
                cMap = new ImageJColorMap(IMAGEJ_RAINBOW);
                break;

            case (IMAGEJ_CYAN_HOT):
                cMap = new ImageJColorMap(IMAGEJ_CYAN_HOT);
                break;

            case (IMAGEJ_GREEN):
                cMap = new ImageJColorMap(IMAGEJ_GREEN);
                break;

            case (IMAGEJ_GEM):
                cMap = new ImageJColorMap(IMAGEJ_GEM);
                break;

            case (IMAGEJ_EDGES):
                cMap = new ImageJColorMap(IMAGEJ_EDGES);
                break;

            case (IMAGEJ_RED_HOT):
                cMap = new ImageJColorMap(IMAGEJ_RED_HOT);
                break;

            case (IMAGEJ_FIRE):
                cMap = new ImageJColorMap(IMAGEJ_FIRE);
                break;

            case (IMAGEJ_THAL):
                cMap = new ImageJColorMap(IMAGEJ_THAL);
                break;

            case (IMAGEJ_BRGBCMYW):
                cMap = new ImageJColorMap(IMAGEJ_BRGBCMYW);
                break;

            case (IMAGEJ_SMART):
                cMap = new ImageJColorMap(IMAGEJ_SMART);
                break;

            case (IMAGEJ_5_RAMPS):
                cMap = new ImageJColorMap(IMAGEJ_5_RAMPS);
                break;

            case (IMAGEJ_BLUE):
                cMap = new ImageJColorMap(IMAGEJ_BLUE);
                break;

            case (IMAGEJ_MAGENTA):
                cMap = new ImageJColorMap(IMAGEJ_MAGENTA);
                break;

            case (IMAGEJ_SPECTRUM):
                cMap = new ImageJColorMap(IMAGEJ_SPECTRUM);
                break;

            case (IMAGEJ_THALLIUM):
                cMap = new ImageJColorMap(IMAGEJ_THALLIUM);
                break;

            case (IMAGEJ_PHASE):
                cMap = new ImageJColorMap(IMAGEJ_PHASE);
                break;

            case (IMAGEJ_ICE):
                cMap = new ImageJColorMap(IMAGEJ_ICE);
                break;

            case (IMAGEJ_16_COLORS):
                cMap = new ImageJColorMap(IMAGEJ_16_COLORS);
                break;

            case (IMAGEJ_COOL):
                cMap = new ImageJColorMap(IMAGEJ_COOL);
                break;

            case (IMAGEJ_ICA2):
                cMap = new ImageJColorMap(IMAGEJ_ICA2);
                break;

            case (IMAGEJ_BLUE_ORANGE_ICB):
                cMap = new ImageJColorMap(IMAGEJ_BLUE_ORANGE_ICB);
                break;

            case (IMAGEJ_ORANGE_HOT):
                cMap = new ImageJColorMap(IMAGEJ_ORANGE_HOT);
                break;

            case (IMAGEJ_UNIONJACK):
                cMap = new ImageJColorMap(IMAGEJ_UNIONJACK);
                break;

            case (IMAGEJ_GREEN_FIRE_BLUE):
                cMap = new ImageJColorMap(IMAGEJ_GREEN_FIRE_BLUE);
                break;

            case (IMAGEJ_RED):
                cMap = new ImageJColorMap(IMAGEJ_RED);
                break;

            case (IMAGEJ_6_SHADES):
                cMap = new ImageJColorMap(IMAGEJ_6_SHADES);
                break;

            case (IMAGEJ_YELLOW_HOT):
                cMap = new ImageJColorMap(IMAGEJ_YELLOW_HOT);
                break;

            default:
                break;
        }

        return cMap;
    }
}