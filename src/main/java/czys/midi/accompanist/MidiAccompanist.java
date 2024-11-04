package czys.midi.accompanist;

import czys.midi.accompanist.configuration.ApplicationConfiguration;
import czys.midi.accompanist.configuration.MidiFileConfiguration;
import czys.midi.accompanist.exception.MidiAccompanistException;
import czys.midi.accompanist.player.MidiPlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;

public class MidiAccompanist {

    public static void main(String[] args) {
        ConfigurationValidator.validate(args);

        Yaml yaml = new Yaml();

        try (InputStream applicationConfigInputStream = new FileInputStream(args[0]); InputStream midiFileConfigInputStream = new FileInputStream(args[1])) {
            MidiPlayer.getInstance().playFile(
                    yaml.loadAs(applicationConfigInputStream, ApplicationConfiguration.class),
                    yaml.loadAs(midiFileConfigInputStream, MidiFileConfiguration.class)
            );
        } catch (Exception e) {
            throw new MidiAccompanistException(e);
        }
    }
}
