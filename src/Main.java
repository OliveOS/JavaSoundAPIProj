/**
 * @author oliverwooding
 */

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
        playFile("src/midfiles/giantsteps.mid");
        //vbhiasfvhasi
    }

    /**
     *
     * @param pathName the path to the MIDI file relative to the src folder
     */
    public static void playFile(String pathName){
        try {
            Sequencer sequencer = MidiSystem.getSequencer(); // retrieve default midi playback device

            if (sequencer==null) {
                System.err.println("Sequencer device not supported"); //print to error channel
                return;
            }

            sequencer.open();

            // create sequence from file with valid midi date
            //file must be specified with full file path
            Sequence sequence = MidiSystem.getSequence(new File(pathName));
            sequencer.setSequence(sequence); // load it into sequencer
            sequencer.start();  // start the playback

        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace(); //print instruction stack to error channel
        }
    }

    /**
     *
     * @param sequencer any available sequencer
     * @param pathName the path to the MIDI file relative to the src folder
     */
    public static void playFile(Sequencer sequencer, String pathName){
        try {
            //verify sequencer status
            controlledMidiOpen(sequencer);

            // generate and assign sequence to specified sequencer
            Sequence sequence = MidiSystem.getSequence(new File(pathName));
            sequencer.setSequence(sequence); // load it into sequencer
            sequencer.start();  // start the playback

        } catch (InvalidMidiDataException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param name the name of the device to find
     * @return device index in MidiSystem.getMidiDeviceInfo(), -1 if device does not exist
     */
    public static int findDevice(String name){
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();

        for(int i = 0; i < devices.length; i++){
            if(devices[i].getName().equals(name)){
                return i;
            }
        }

        return -1;
    }

    /**
     *
     * @return array of all available devices + prints failed attempts to console
     */
    public static MidiDevice[] getAvailableDevices(){
        ArrayList<MidiDevice> devices = new ArrayList<>();
        MidiDevice midiDev;

        for(MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()){
            try{
                midiDev = MidiSystem.getMidiDevice(info);
                devices.add(midiDev);
            }catch (MidiUnavailableException e){
                System.out.println("MidiUnavailableException: " + info.getName());
            }
        }

        return devices.toArray(new MidiDevice[devices.size()]);
    }

    /**
     *
     * @param device the MidiDevice to be opened
     */
    public static void controlledMidiOpen(MidiDevice device){
        if(!device.isOpen()){
            try{
                device.open();
            }catch(MidiUnavailableException e){
            }
        }
    }
}