package czys.midi.accompanist.player;

import czys.midi.accompanist.configuration.ApplicationConfiguration;
import czys.midi.accompanist.configuration.MidiFileConfiguration;
import czys.midi.accompanist.exception.MidiAccompanistException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.*;
import java.io.IOException;

@Slf4j
public class MidiPlayer {

    @Getter
    private static final MidiPlayer instance = new MidiPlayer();

    private final MidiDeviceManager midiDeviceManager = new MidiDeviceManager();
    private final MidiSequenceManager midiSequenceManager = new MidiSequenceManager();
    private final MidiOutDeviceHandler midiOutDeviceHandler = new MidiOutDeviceHandler();

    private MidiPlayer() {}

    public void playFile(ApplicationConfiguration applicationConfig, MidiFileConfiguration midiFileConfig) {
        try (
                Sequencer sequencer = MidiSystem.getSequencer(false);
                MidiDevice midiInDevice = midiDeviceManager.findMidiDevice(applicationConfig.getMidiInDeviceName(), true);
                MidiDevice midiOutDevice = midiDeviceManager.findMidiDevice(applicationConfig.getMidiOutDeviceName(), false)
        ) {
            midiDeviceManager.openResources(midiInDevice, midiOutDevice, sequencer);
            midiDeviceManager.setUpMidiInDeviceTransmitting(midiInDevice, sequencer);

            Sequence sequence = midiSequenceManager.loadSequence(midiFileConfig.getFileLocation());
            sequencer.setSequence(sequence);

            PianoEventsList pianoMidiMessages = midiSequenceManager.preparePianoMidiMessages(midiFileConfig, sequence);

            midiOutDeviceHandler.handleIncomingMidiEvents(sequencer, pianoMidiMessages, midiOutDevice);

            sequencer.start();

            PlaybackController playbackController = new PlaybackController(sequencer, pianoMidiMessages);
            playbackController.handlePlaying();
        } catch (MidiUnavailableException | IOException | InvalidMidiDataException e) {
            throw new MidiAccompanistException(e);
        }
    }
}
