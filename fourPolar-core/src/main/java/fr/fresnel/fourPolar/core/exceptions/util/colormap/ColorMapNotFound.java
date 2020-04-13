package fr.fresnel.fourPolar.core.exceptions.util.colormap;

public class ColorMapNotFound extends Exception {
    private static final long serialVersionUID = 432823218309128301L;

    public ColorMapNotFound(){
        super("The specified colormap does not exist.", null, true, false);
    }

}