package czys.midi.accompanist.player;

import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.ShortMessage;

@Getter
@Setter
class PressableMidiMessageWithTick extends MidiMessageWithTick {

    private boolean pressed = false;

    public PressableMidiMessageWithTick(ShortMessage message, long tick) {
        super(message, tick);
    }
}
