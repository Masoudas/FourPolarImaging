package fr.fresnel.fourPolar.io.preprocess;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class RegistrationSetProcessResultJSONSerializer
        extends StdSerializer<RegistrationSetProcessResultToJSONAdaptor> {
    /**
     *
     */
    private static final long serialVersionUID = 3196084304356664812L;

    public RegistrationSetProcessResultJSONSerializer() {
        this(null);
    }

    public RegistrationSetProcessResultJSONSerializer(Class<RegistrationSetProcessResultToJSONAdaptor> t) {
        super(t);
    }

    @Override
    public void serialize(RegistrationSetProcessResultToJSONAdaptor value, JsonGenerator gen,
            SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        
        for (Iterator<String> labelItr = value.getChannelLabels(); labelItr.hasNext();) {
            String channelLabel = labelItr.next();
            gen.writeObjectField(channelLabel, value.getChannelResultAdaptor(channelLabel));
        }

        gen.writeEndObject();

    }

}