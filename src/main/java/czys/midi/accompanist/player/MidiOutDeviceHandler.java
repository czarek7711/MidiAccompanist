package czys.midi.accompanist.player;

import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.*;

@Slf4j
class MidiOutDeviceHandler {

    void handleIncomingMidiEvents(Sequencer sequencer, PianoEventsList pianoMidiMessages, MidiDevice midiOutDevice) throws MidiUnavailableException {
        try (Receiver newMidiOutReceiver = new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                if (message instanceof ShortMessage shortMessage && shortMessage.getCommand() == ShortMessage.NOTE_ON) {
                    pianoMidiMessages.markFirstFoundAsPressed(shortMessage.getData1(), sequencer.getTickPosition());
                    log.debug("{} Note: {}, Tick: {}", System.currentTimeMillis(), shortMessage.getData1(), sequencer.getTickPosition());
                }
            }

            @Override
            public void close() {
                // Not used
            }
        }) {
            Transmitter midiOutDeviceTransmitter = midiOutDevice.getTransmitter();
            midiOutDeviceTransmitter.setReceiver(newMidiOutReceiver);
        }
    }
}
