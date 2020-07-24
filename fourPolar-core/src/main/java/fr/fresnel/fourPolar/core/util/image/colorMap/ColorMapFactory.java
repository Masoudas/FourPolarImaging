package fr.fresnel.fourPolar.core.util.image.colorMap;

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
    public static final String DISK_STRONG_RED = "strong_red.lut";
    public static final String IMAGEJ_6_SHADES = "NCSA PalEdit/6_shades.lut";
    public static final String IMAGEJ_YELLOW_HOT = "WCIF/Yellow Hot.lut";
    public static final String IMAGEJ_JET = "jet.lut";

    public static ColorMap create(String colorMap) {
        ColorMap cMap = null;
        switch (colorMap) {
            case IMAGEJ_ICA:
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ICA);
                break;

            case IMAGEJ_HILO:
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_HILO);
                break;

            case (IMAGEJ_YELLOW):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_YELLOW);
                break;

            case (IMAGEJ_RED_GREEN):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_RED_GREEN);
                break;

            case (IMAGEJ_ICA3):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ICA3);
                break;

            case (IMAGEJ_3_3_2_RGB):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_3_3_2_RGB);
                break;

            case (IMAGEJ_ROYAL):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ROYAL);
                break;

            case (IMAGEJ_MAGENTA_HOT):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_MAGENTA_HOT);
                break;

            case (IMAGEJ_CYAN):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_CYAN);
                break;

            case (IMAGEJ_SEPIA):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_SEPIA);
                break;

            case (IMAGEJ_GRAYS):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_GRAYS);
                break;

            case (IMAGEJ_RAINBOW):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_RAINBOW);
                break;

            case (IMAGEJ_CYAN_HOT):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_CYAN_HOT);
                break;

            case (IMAGEJ_GREEN):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_GREEN);
                break;

            case (IMAGEJ_GEM):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_GEM);
                break;

            case (IMAGEJ_EDGES):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_EDGES);
                break;

            case (IMAGEJ_RED_HOT):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_RED_HOT);
                break;

            case (IMAGEJ_FIRE):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_FIRE);
                break;

            case (IMAGEJ_THAL):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_THAL);
                break;

            case (IMAGEJ_BRGBCMYW):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_BRGBCMYW);
                break;

            case (IMAGEJ_SMART):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_SMART);
                break;

            case (IMAGEJ_5_RAMPS):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_5_RAMPS);
                break;

            case (IMAGEJ_BLUE):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_BLUE);
                break;

            case (IMAGEJ_MAGENTA):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_MAGENTA);
                break;

            case (IMAGEJ_SPECTRUM):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_SPECTRUM);
                break;

            case (IMAGEJ_THALLIUM):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_THALLIUM);
                break;

            case (IMAGEJ_PHASE):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_PHASE);
                break;

            case (IMAGEJ_ICE):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ICE);
                break;

            case (IMAGEJ_16_COLORS):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_16_COLORS);
                break;

            case (IMAGEJ_COOL):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_COOL);
                break;

            case (IMAGEJ_ICA2):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ICA2);
                break;

            case (IMAGEJ_BLUE_ORANGE_ICB):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_BLUE_ORANGE_ICB);
                break;

            case (IMAGEJ_ORANGE_HOT):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_ORANGE_HOT);
                break;

            case (IMAGEJ_UNIONJACK):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_UNIONJACK);
                break;

            case (IMAGEJ_GREEN_FIRE_BLUE):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_GREEN_FIRE_BLUE);
                break;

            case (IMAGEJ_RED):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_RED);
                break;

            case (IMAGEJ_6_SHADES):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_6_SHADES);
                break;

            case (IMAGEJ_YELLOW_HOT):
                cMap = ImageJColorMap.getDefaultColorMaps(IMAGEJ_YELLOW_HOT);
                break;

            case (IMAGEJ_JET):
                cMap = ImageJColorMap.getDiskColorMap(IMAGEJ_JET);
                break;

            case (DISK_STRONG_RED):
                cMap = ImageJColorMap.getDiskColorMap(DISK_STRONG_RED);
                break;

            default:
                break;
        }

        return cMap;
    }
}