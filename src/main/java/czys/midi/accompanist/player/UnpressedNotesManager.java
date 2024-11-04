package czys.midi.accompanist.player;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.ShortMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
class UnpressedNotesManager {

    private final Set<PressableMidiMessageWithTick> unpressedNotes;

    UnpressedNotesManager() {
        this.unpressedNotes = new HashSet<>();
    }

    List<Integer> getAllNotes() {
        return unpressedNotes.stream()
                .map(MidiMessageWithTick::getMessage)
                .map(ShortMessage::getData1)
                .toList();
    }

    public void updateUnpressedNotes(PianoEventsList pianoMidiMessages, long tick) {
        PressableMidiMessageWithTick midiMessage = pianoMidiMessages.getLastNotPressedByTickLessThan(tick);
        if (midiMessage != null) {
            unpressedNotes.add(midiMessage);
        }
    }

    public void removeUnpressedNotes() {
        unpressedNotes.removeIf(PressableMidiMessageWithTick::isPressed);
    }

    public void removeAllNotes() {
        unpressedNotes.clear();
    }

    public void logUnpressedNotes() {
        for (Integer note : unpressedNotes.stream()
                .map(MidiMessageWithTick::getMessage)
                .map(ShortMessage::getData1)
                .collect(Collectors.toSet())) {
            log.debug(String.valueOf(note));
        }
    }
}
