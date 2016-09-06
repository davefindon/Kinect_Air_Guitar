import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;


public class MidiInterface {
	static Receiver myReceiver = null;
	static int previousInstrument = -1;
	static int previousNote = -1;
	
	
	
	public static Info[] getDeviceList(){
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		return infos;
	}
	public static void connectToDevice(Object device) throws MidiUnavailableException{
		MidiDevice myDevice = MidiSystem.getMidiDevice((MidiDevice.Info) device);
		myDevice.open();
		myReceiver = myDevice.getReceiver();
		System.out.println("Connected to "+myDevice.toString());
	}
	public static Receiver getMyReciever(){
		return myReceiver;
	}
	public static void onNote(int instrument, int note) throws InvalidMidiDataException{
		ShortMessage playMessage = new ShortMessage();
		playMessage.setMessage(ShortMessage.NOTE_ON, instrument, note, 127);
		long timeStamp1 = -1;
		getMyReciever().send(playMessage, timeStamp1);
		previousInstrument = instrument;
		previousNote = note;
		System.out.println("Playing note " + note);
	}
	public static void offNote(int instrument, int note) throws InvalidMidiDataException{
		ShortMessage playMessage = new ShortMessage();
		playMessage.setMessage(ShortMessage.NOTE_OFF, instrument, note, 127);
		long timeStamp1 = -1;
		getMyReciever().send(playMessage, timeStamp1);
		System.out.println("Stopping note " + note);
	}
	public static void killLastNote() throws InvalidMidiDataException{
		if(previousNote >0){
			ShortMessage playMessage = new ShortMessage();
			playMessage.setMessage(ShortMessage.NOTE_OFF, previousInstrument, previousNote, 127);
			long timeStamp1 = -1;
			getMyReciever().send(playMessage, timeStamp1);
		}
	}
}
	