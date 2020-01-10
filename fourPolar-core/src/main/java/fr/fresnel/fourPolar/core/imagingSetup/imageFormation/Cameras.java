package fr.fresnel.fourPolar.core.imagingSetup.imageFormation;

public enum Cameras {
    One, Two, Four;

    public static String[] getLabels(Cameras camera) {
       String[] labels = null;

        if (camera == One)
        {
            labels = new String[1];
            labels[0] = "Pol0_45_90_135";
        }
        else if (camera == Two)
        {
            labels = new String[2];
            labels[0] = "Pol0_90";
            labels[1] = "Pol45_135";
        }
        else if (camera == Four)
        {
            labels = new String[4];
            labels[0] = "Pol0";
            labels[1] = "Pol45";
            labels[2] = "Pol90";
            labels[3] ="Pol135";
        }
            
        return labels;
    }

}