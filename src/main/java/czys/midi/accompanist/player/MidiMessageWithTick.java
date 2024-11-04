package czys.midi.accompanist.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.sound.midi.ShortMessage;

@Getter
@AllArgsConstructor
class MidiMessageWithTick implements Comparable<MidiMessageWithTick> {

    private ShortMessage message;
    private long tick;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MidiMessageWithTick other) {
            return this.message.getData1() == other.message.getData1() &&
                    this.tick == other.tick;
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int compareTo(MidiMessageWithTick o) {
        return Long.compare(this.tick, o.getTick());
    }
}
