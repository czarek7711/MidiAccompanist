package czys.midi.accompanist.player;

import czys.midi.accompanist.configuration.MidiFileConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

@Slf4j
class MidiSequenceManager {

    public Sequence loadSequence(String fileLocation) throws InvalidMidiDataException, IOException {
        return MidiSystem.getSequence(new File(fileLocation));
    }

    public PianoEventsList preparePianoMidiMessages(MidiFileConfiguration midiFileConfig, Sequence sequence) {
        Track pianoTrack = sequence.getTracks()[midiFileConfig.getPianoTrack()];

        PianoEventsList pianoMidiMessages = new PianoEventsList(pianoTrack.size());

        for (int i = 0; i < pianoTrack.size(); i++) {
            if (pianoTrack.get(i).getMessage() instanceof ShortMessage message && message.getCommand() == ShortMessage.NOTE_ON) {
                pianoMidiMessages.add(new PressableMidiMessageWithTick(message, pianoTrack.get(i).getTick()));
            }
        }

        logPianoMidiMessages(pianoMidiMessages);
        return pianoMidiMessages;
    }

    private void logPianoMidiMessages(PianoEventsList pianoMidiMessages) {
        for (MidiMessageWithTick messageWithTick : pianoMidiMessages) {
            log.trace("Command: {}, Channel: {}, Data 1: {}, Data 2: {}, Tick:  {}", messageWithTick.getMessage().getCommand(), messageWithTick.getMessage().getChannel(), messageWithTick.getMessage().getData1(), messageWithTick.getMessage().getData2(), messageWithTick.getTick());
        }
    }
}
