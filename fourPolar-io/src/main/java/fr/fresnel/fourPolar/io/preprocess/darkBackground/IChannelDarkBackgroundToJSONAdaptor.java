package fr.fresnel.fourPolar.io.preprocess.darkBackground;

import java.util.TreeMap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.fresnel.fourPolar.core.physics.polarization.Polarization;
import fr.fresnel.fourPolar.core.preprocess.darkBackground.IChannelDarkBackground;

@JsonSerialize(using = IChannelDarkBackgroundJSONSerializer.class)
public class IChannelDarkBackgroundToJSONAdaptor {
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

    public Double getDarkBackgrounds(Polarization polarization) {
        return _darkBackgrounds.get(polarization);
    }
}