package czys.midi.accompanist.player;

import czys.midi.accompanist.exception.ConfigurationException;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.Objects;

class MidiDeviceManager {

    public MidiDevice findMidiDevice(String midiDeviceName, boolean isMidiInDevice) {
        return Arrays.stream(MidiSystem.getMidiDeviceInfo())
                .filter(info -> Objects.equals(midiDeviceName, info.getName()))
                .map(this::getMidiDevice)
                .filter(midiDevice -> isMidiInDevice ? midiDevice.getMaxReceivers() != 0 : midiDevice.getMaxTransmitters() != 0)
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("Can't find Midi Device with name " + midiDeviceName));
    }

    public void openResources(MidiDevice midiInDevice, MidiDevice midiOutDevice, Sequencer sequencer) throws MidiUnavailableException {
        midiInDevice.open();
        midiOutDevice.open();
        sequencer.open();
    }

    public void setUpMidiInDeviceTransmitting(MidiDevice midiInDevice, Sequencer sequencer) throws MidiUnavailableException {
        Receiver receiver = midiInDevice.getReceiver();
        Transmitter transmitter = sequencer.getTransmitter();
        transmitter.setReceiver(receiver);
    }

    private MidiDevice getMidiDevice(MidiDevice.Info info) {
        try {
            return MidiSystem.getMidiDevice(info);
        } catch (MidiUnavailableException e) {
            throw new ConfigurationException(e);
        }
    }
}
