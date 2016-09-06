import java.util.*;

import javax.sound.midi.InvalidMidiDataException;



public class Instrument {
	 /* Instruments:
	 * 0 - Steel String Acoustic (Channel 1 - Default)
	 * 1 - Electric (Channel 2)
	 * 2 - Bass (Channel 3)
	 * 3 - Classical (Channel 4)
	 */
	static int instrument = 0;
	
	public static void cycleInstrument() throws InvalidMidiDataException{
		MidiInterface.killLastNote();
		
		if(instrument==3){
			instrument = 0;
		}
		else{
			instrument = instrument +1;
		}
		Controller.setInstrument(instrument);
		System.out.println("Instrument now"+instrument);
	}
	public static void cycleInstrumentBack() throws InvalidMidiDataException{
		MidiInterface.killLastNote();
		if(instrument==0){
			instrument = 3;
		}
		else{
			instrument = instrument - 1;
		}
		Controller.setInstrument(instrument);
		System.out.println("Instrument now "+instrument);
	}
	public static int getInstrument(){
		return instrument;
	}
	
	//-----------STEEL GUITAR-------------
	
	 /*Steel Guitar Chords
	 * E/fret 0 = note 28
	 * F/fret 1 = note 29
	 * etc.
	*/
	
	static int newSteelChord = -1;
	static int oldSteelChord = -1;
	
	public static void setNewSteelChord (int newChord){
		newSteelChord = newChord;
	}
	public static int getNewSteelChord (){
		return newSteelChord;
	}
	public static void setOldSteelChord (int oldChord){
		oldSteelChord = oldChord;
	}
	public static int getOldSteelChord (){
		return oldSteelChord;
	}

	
	
	//--------ELECTRIC AND CLASSICAL-------
	
	
	//Chord Arrays for Electric and Classical - Chords must be built manually as VST does not provide chords
	static ArrayList<Integer> newChord = new ArrayList<>();

	
	public static ArrayList<Integer> getNewChord(){
		return newChord;
	}

	
	
	public static void setNewChord(int fret, boolean minor) {
			if (newChord.size() == 0){
				newChord.add(fret);
				newChord.add(fret+7);
				newChord.add(fret+12);
				if (minor){
					newChord.add(fret+15);
				}
				else{
					newChord.add(fret+16);
				}
				newChord.add(fret+19);
				newChord.add(fret+24);
			}
			else{
				newChord.set(0,fret);
				newChord.set(1,fret+7);
				newChord.set(2,fret+12);
				if (minor){
					newChord.set(3,fret+15);
				}
				else{
					newChord.set(3,fret+16);
				}
				newChord.set(4,fret+19);
				newChord.set(5,fret+24);
			}	
		
			
	}
	//--------------------BASS GUITAR---------------------------
	static int oldNote = -1;
	static int newNote = -1;
	
	public static int getOldNote(){
		return oldNote;
	}
	public static int getNewNote(){
		return newNote;
	}
	public static void setNewNote(int note){
		oldNote=newNote;
		newNote=note;
	}
	
	


}