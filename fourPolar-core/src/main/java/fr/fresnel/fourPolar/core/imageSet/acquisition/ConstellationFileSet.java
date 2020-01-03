package fr.fresnel.fourPolar.core.imageSet.acquisition;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.PolarizationConstellation;

import java.io.File;

/**
 * A file container, which holds the images provided in the raw constellation format.
 */

public class ConstellationFileSet implements IConstellationFileSet {
    private String nameExtract = "";
    private Hashtable<String, File> fileSet = new Hashtable<String, File>();

    public ConstellationFileSet(File pol0_45_90_135){
        fileSet.put("Pol0_45_90_135", pol0_45_90_135);

        nameExtract = pol0_45_90_135.getName().substring(0, pol0_45_90_135.getName().indexOf('.'));
    }
    
    public ConstellationFileSet(File pol0_90, File pol45_135){
        fileSet.put("Pol0_90", pol0_90);
        fileSet.put("Pol45_135", pol45_135);
        
        nameExtract = pol0_90.getName().substring(0, pol0_90.getName().indexOf('.'));
    }

    public ConstellationFileSet(File pol0, File pol45, File pol90, File pol135){
        fileSet.put("Pol0", pol0);
        fileSet.put("Pol45", pol45);
        fileSet.put("Pol90", pol90);
        fileSet.put("Pol135", pol135);

        nameExtract = pol0.getName().substring(0, pol0.getName().lastIndexOf('.'));
    }

    @Override
    public boolean equals(Object obj) {
        IConstellationFileSet fileSet = (IConstellationFileSet)obj;

        if (fileSet.getNameExtract().equals(this.nameExtract))
            return true;
        
        return false;
    }

    @Override
    public Set<String> getLabels() {
        return fileSet.keySet();
    }

    @Override
    public File getFile(String label) {
        return fileSet.get(label);
    }

    @Override
    public String getNameExtract() {
        return nameExtract;
    }
    
}