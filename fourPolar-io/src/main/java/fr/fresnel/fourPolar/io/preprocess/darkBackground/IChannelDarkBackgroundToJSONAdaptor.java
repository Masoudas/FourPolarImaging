package fr.fresnel.fourPolar.io.preprocess.darkBackground;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;


public class IChannelDarkBackgroundToJSONAdaptor {
    @JsonProperty("Polarization 0")
    private final double _darkBackground_pol0;
    
    @JsonProperty("Polarization 45")
    private final double _darkBackground_pol45;
    
    @JsonProperty("Polarization 90")
    private final double _darkBackground_pol90;
    
    @JsonProperty("Polarization 135")
    private final double _darkBackground_pol135;


    public IChannelDarkBackgroundToJSONAdaptor(IChannelDarkBackground darkBackground) {
        this._darkBackground_pol0 = this._getDarkBackground(darkBackground, Polarization.pol0);
        this._darkBackground_pol45 = this._getDarkBackground(darkBackground, Polarization.pol45);
        this._darkBackground_pol90 = this._getDarkBackground(darkBackground, Polarization.pol90);
        this._darkBackground_pol135 = this._getDarkBackground(darkBackground, Polarization.pol135);
    }

    private double _getDarkBackground(IChannelDarkBackground darkBackground, Polarization polarization) {
        return darkBackground.getBackgroundLevel(polarization);
    }
}