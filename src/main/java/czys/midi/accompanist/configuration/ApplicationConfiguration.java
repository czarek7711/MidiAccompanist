package czys.midi.accompanist.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationConfiguration {

    private String midiOutDeviceName;
    private String midiInDeviceName;
}