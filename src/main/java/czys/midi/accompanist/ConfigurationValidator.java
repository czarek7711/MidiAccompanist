package czys.midi.accompanist;

import czys.midi.accompanist.exception.ConfigurationException;
import org.apache.commons.lang3.ArrayUtils;

class ConfigurationValidator {

    private ConfigurationValidator() {
        throw new UnsupportedOperationException();
    }

    static void validate(String... args) {
        if (ArrayUtils.isEmpty(args)) {
            throw new ConfigurationException("No configuration provided");
        }
        if (args.length < 2) {
            throw new ConfigurationException("Insufficient configuration provided. Make sure both application and midi file configs are available");
        }
    }
}
