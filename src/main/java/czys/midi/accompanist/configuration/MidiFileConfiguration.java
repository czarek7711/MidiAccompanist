package czys.midi.accompanist.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidiFileConfiguration {

    private String fileLocation;
    private int pianoTrack;
    private int deltaInMilliseconds; // TODO use it
}