# MidiAccompanist #

Allows to play the piano (or any other MIDI compatible instrument) with a MIDI accompaniment that adjusts its tempo in real-time to match your playing.

Requires preparing MIDI and configuration files, all described below.

Written in Java because of its built-in MIDI support.

In its current state it should work more or less with majority properly configured MIDI files, however the configuration process isn't very user friendly.

## How it works - Preconfiguration ##

Application requires the MIDI file to be multi-track, with one track dedicated to piano events and others to accompaniment.

The piano track should contain notes you're expected to play.

During playback application compares your playing with the piano track. If you're behind it slows down, if you're ahead or accidentaly skip a note it speeds up to match your position.

The piano track doesn't need to include all notes you're going to play. Sections where left hand plays the accompaniment exclusively are probably fine with only those notes included. This leaves more freedom for the right hand. Figuring out which notes to include is a matter of experimenting. Debug logging may be used for especially tricky cases.

MIDI files can be edited in a MIDI file editor of your choice. You probably need to use the editor to find the piano track index as well.

The piano needs to be connected to PC and have its drivers installed, so it shows up in MIDI devices (in Windows you can find your MIDI devices in Device Manager -> Software devices).

*src/main/resources* includes example MIDI and configuration files.

## Configuration ##

Two configuration files need to be provided as arguments when running the application:

1. Application config, with properties:
  * midiOutDeviceName - Name of your piano MIDI Device connected to PC.
  * midiInDeviceName - Name of the MIDI Device used for MIDI playback. Windows uses *Microsoft GS Wavetable Synth* by default, but you can use applications like *loopMIDI* to have the playback routed through a DAW for example.

2. MIDI file config, per each file, with properties:
  * fileLocation - Location of the MIDI file.
  * pianoTrack - Index of the track with piano events.
  * deltaInMilliseconds - Currently not used, but should be used in the process of recognizing events as pressed.

## Bulding and running ##

1. Install Java 21 and one of the latest Maven 3.X.X releases.
2. Build the application using Maven by running the following command in the directory with pom.xml file:
```
mvn clean package
```
3. Run built jar-with-dependencies jar file using Java. Example command, assuming configuration files are in the same location as the jar file:

```
java -jar MidiAccompanist-0.1.0-jar-with-dependencies.jar applicationConfig.yaml midiFileConfig.yaml
```
Tested only on a Windows PC.

## TODO ##
* Refactoring
* Unit testing
* Taking care of *TODO* comments
* Better logging, validation, error handling and debugging options
* Algorithm improvements
