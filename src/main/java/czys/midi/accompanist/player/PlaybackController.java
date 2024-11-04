package czys.midi.accompanist.player;

import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.Sequencer;

@Slf4j
class PlaybackController {

    private final Sequencer sequencer;
    private final PianoEventsList pianoMidiMessages;
    private final UnpressedNotesManager unpressedNotesManager;

    private PlaybackState playbackState;
    private float originalPauseTempo;
    private float originalForwardTempo;
    private float forwardTempo;
    private long tick;
    private long tickToForwardTo;

    PlaybackController(Sequencer sequencer, PianoEventsList pianoMidiMessages) {
        this.sequencer = sequencer;
        this.pianoMidiMessages = pianoMidiMessages;
        this.unpressedNotesManager = new UnpressedNotesManager();

        this.playbackState = PlaybackState.NORMAL;
        this.originalPauseTempo = 0;
        this.originalForwardTempo = 0;
        this.forwardTempo = 0;
        this.tick = -1;
        this.tickToForwardTo = -1;
    }

    public void handlePlaying() {
        while (sequencer.isRunning()) {
            if (sequencer.getTickPosition() > tick) {
                tick = sequencer.getTickPosition();
                log.trace(String.valueOf(tick));

                switch (playbackState) {
                    case FORWARDING:
                        handleForwarding();
                        break;
                    case NORMAL:
                        handleNormal();
                        break;
                    case PAUSED:
                        handlePaused();
                        break;
                }
            }
        }
    }

    private void handleForwarding() {
        if (tick >= tickToForwardTo) {
            sequencer.setTempoInBPM(originalForwardTempo);
            playbackState = PlaybackState.NORMAL;
        } else {
            sequencer.setTempoInBPM(forwardTempo);
        }
    }

    private void handleNormal() {
        unpressedNotesManager.updateUnpressedNotes(pianoMidiMessages, tick);

        if (!unpressedNotesManager.getUnpressedNotes().isEmpty()) {
            playbackState = PlaybackState.PAUSED;

            originalPauseTempo = sequencer.getTempoInBPM();
            sequencer.setTempoInBPM(0);
            log.debug("{} paused, {}, unpressed notes:", System.currentTimeMillis(), tick);
            unpressedNotesManager.logUnpressedNotes();
        } else {
            MidiMessageWithTick midiMessage = pianoMidiMessages.getFirstPressedByTickGreaterThan(tick);

            if (midiMessage != null) {
                playbackState = PlaybackState.FORWARDING;

                tickToForwardTo = midiMessage.getTick();
                originalForwardTempo = sequencer.getTempoInBPM();
                forwardTempo = originalForwardTempo * 2;
                sequencer.setTempoInBPM(forwardTempo);
                log.debug("{} forwarding at tempo: {} to tick: {}", System.currentTimeMillis(), (int) forwardTempo, tickToForwardTo);
            }
        }
    }

    private void handlePaused() {
        unpressedNotesManager.removeUnpressedNotes();
        if (unpressedNotesManager.getUnpressedNotes().isEmpty()) {
            playbackState = PlaybackState.NORMAL;

            sequencer.setTempoInBPM(originalPauseTempo);
            log.debug("{} unpaused, {}", System.currentTimeMillis(), tick);
        } else {
            MidiMessageWithTick midiMessage = pianoMidiMessages.getFirstPressedByTickGreaterThan(tick);

            if (midiMessage != null) {
                unpressedNotesManager.getAllNotes().forEach(note -> pianoMidiMessages.markFirstFoundAsPressed(note, tick));
                unpressedNotesManager.removeAllNotes();
                log.debug("{} Cleared all unpressed notes", System.currentTimeMillis());
                playbackState = PlaybackState.FORWARDING;

                tickToForwardTo = midiMessage.getTick();
                originalForwardTempo = originalPauseTempo;
                forwardTempo = originalForwardTempo * 2;
                sequencer.setTempoInBPM(forwardTempo);
                log.debug("{} forwarding at tempo: {} to tick: {}", System.currentTimeMillis(), (int) forwardTempo, tickToForwardTo);
            }
        }
    }
}

