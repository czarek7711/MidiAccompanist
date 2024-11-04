package czys.midi.accompanist.player;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
class PianoEventsList extends ArrayList<PressableMidiMessageWithTick> {

    PianoEventsList(int size) {
        super(size);
    }

    PressableMidiMessageWithTick getLastNotPressedByTickLessThan(long tick) {
        List<PressableMidiMessageWithTick> shortMessageList = this.stream()
                .filter(messageWithTick -> messageWithTick.getTick() - 10 <= tick) // TODO expose the 10 ticks value in configuration
                .filter(messageWithTick -> !messageWithTick.isPressed())
                .sorted((a, b) -> Math.toIntExact(b.getTick() - a.getTick())) // TODO better way of sorting
                .toList();
        if (!shortMessageList.isEmpty()) {
            return shortMessageList.getFirst();
        } else {
            return null;
        }
    }

    PressableMidiMessageWithTick getFirstPressedByTickGreaterThan(long tick) {
        return this.stream()
                .filter(messageWithTick -> messageWithTick.getTick() > tick)
                .filter(PressableMidiMessageWithTick::isPressed)
                .findFirst()
                .orElse(null);
    }

    void markFirstFoundAsPressed(int note, long tick) {
        this.stream()
                .filter(isPressedPredicate(note, tick))
                .findFirst()
                .ifPresent(message -> message.setPressed(Boolean.TRUE));
    }

    private Predicate<PressableMidiMessageWithTick> isPressedPredicate(int note, long tick) {
        return message -> message.getMessage().getData1() == note &&
                !message.isPressed() &&
                (message.getTick() <= tick || message.getTick() - tick <= 480); // TODO use calculateDelta method along with the deltaInMilliseconds from config, not a hardcoded value
    }

    private double calculateDelta(float currentBPM) {
        final int ppq = 0;
        final int deltaInMilliseconds = 0;
        double microsecondsPerQuarterNote = 60_000_000 / currentBPM;
        double microsecondsPerTick = microsecondsPerQuarterNote / ppq;
        double millisecondsPerTick = microsecondsPerTick / 1000;
        return deltaInMilliseconds / millisecondsPerTick;
    }
}
