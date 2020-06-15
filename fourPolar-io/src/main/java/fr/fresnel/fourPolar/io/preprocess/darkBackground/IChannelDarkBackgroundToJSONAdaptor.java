package fr.fresnel.fourPolar.io.preprocess.darkBackground;

import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

public class IChannelDarkBackgroundToJSONAdaptor {
    @JsonProperty("Dark Background Level")
    private final TreeMap<Polarization, Double> _darkBackgrounds;

    public IChannelDarkBackgroundToJSONAdaptor(IChannelDarkBackground darkBackground) {
        this._darkBackgrounds = new TreeMap<>();

        this._setDarkBackgrounds(darkBackground);

    }

    private void _setDarkBackgrounds(IChannelDarkBackground darkBackground) {
        for (Polarization polarization : Polarization.values()) {
            this._darkBackgrounds.put(polarization, darkBackground.getBackgroundLevel(polarization));
        }
    }
}