
import javax.sound.midi.*;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;




public class GestureControl extends J4KSDK {
	
//This Class Deals with the input from the user through the Kinect.	
	
	//Variable for keeping track of hand position for Gestures
	String oldLocation = null;
	String newLocation = null;
	
	//Required Variable for Kinect Input
	static GestureControl kinect=new GestureControl();
	
	@Override
	//Action event triggered whenever the user moves
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status) {
		
		Skeleton skeleton = null;
		//Obtaining skeleton position data
		for (int i = 0; i < skeleton_tracked.length; i++) {
			if (skeleton_tracked[i]) {
				skeleton = Skeleton.getSkeleton(i, skeleton_tracked, positions, orientations, joint_status, this);
			}
		}
		if (skeleton != null) {
			try {
				//System.out.println("Skeleton Event Recieved");
				//If there is a skeleton tracked, fire bodyPosition method
				bodyPosition(skeleton);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//Obtaining location for each required skeleton joint
	private void bodyPosition(Skeleton skeleton) throws InvalidMidiDataException{
		
		//chest position
		float chest = skeleton.get3DJointX(Skeleton.SPINE_MID);
		
		//Hip Position
		float hipX = skeleton.get3DJointX(Skeleton.HIP_RIGHT);
		float hipY = skeleton.get3DJointY(Skeleton.HIP_RIGHT);
		
		//Right Hand Position
		float rHandX = skeleton.get3DJointX(Skeleton.HAND_RIGHT)-chest;
		float rHandY = skeleton.get3DJointY(Skeleton.HAND_RIGHT);
		
		//Left Hand Position
		float lHandX = skeleton.get3DJointX(Skeleton.HAND_LEFT)-chest;
		float lHandY = skeleton.get3DJointY(Skeleton.HAND_LEFT);
		System.out.print(lHandX);
		
		//Shoulder Position
		float rShoulderY = skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT);
		
		//Head Position
		float headPostionX = skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT);
		
		//Pass all required joints to fretNumber
		fretNumber(lHandX,lHandY,rHandX,rHandY, hipX, hipY, rShoulderY, headPostionX);
		
	}
	
	//Identifying the fret the user is on.
	private void fretNumber(float lHandX, float lHandY, float rHandX, float rHandY, float hipX, float hipY, float rShoulderY, float headPostionX) throws InvalidMidiDataException{
		//Formula for converting input range to output fret between 0 and 12.
		int fret = (int) (((lHandX - (-0.5))*12)/0.4);
		System.out.println(fret);
		
		//If left hand is below hip, use minor chord
		boolean minor = false;
		if (lHandY<hipY){minor = true;};
		if (lHandY>hipY){minor = false;};
		
		if (fret > 12){fret = 12;};
		if (fret <0){fret = 0;};
		
		//Set Fret on controller class to allow display
		Controller.setFret(fret);
		
		//Set Steel guitar chord if required
		if(Instrument.getInstrument()==0){
			//If there is a previous chord, copy it to the old chord
			if(Instrument.getNewSteelChord()>=0){
				Instrument.setOldSteelChord(Instrument.getNewSteelChord());
				Instrument.setNewSteelChord(fret+40);
			}
			//Otherwise just set the new chord
			else{
				Instrument.setNewSteelChord(fret+40);
			}	
		}
		//Passing all to playing method
		playing(rHandX, rHandY, hipX, hipY, fret, minor, rShoulderY, headPostionX);
		
	}
	//Playing method allows notes to be played
	private void playing(float rHandX, float rHandY, float hipX, float hipY, int fret, boolean minor, float rShoulderY, float headPostionX) throws InvalidMidiDataException{
		//storing previous location of right hand for gestures
		oldLocation = newLocation;
		
		//setting new location bases on right hand
		if (rHandY>hipY && rHandY<rShoulderY){
			newLocation = "UP";
		}
		if (rHandY<hipY){
			newLocation = "DOWN";
		}
		if (rHandY>rShoulderY && rHandX<headPostionX){
			newLocation ="UPLEFT";
		}
		if (rHandY>rShoulderY && rHandX>headPostionX){
			newLocation ="UPRIGHT";
		}
		//Gesture - Swiping right
		if ((newLocation == "UPRIGHT")&&(oldLocation == "UPLEFT")){
			
			MidiInterface.killLastNote();
			Instrument.cycleInstrument();
			Controller.setInstrument(Instrument.getInstrument());
			
		}
		//Gesture - Swiping left
		if ((newLocation == "UPLEFT")&&(oldLocation == "UPRIGHT")){
			MidiInterface.killLastNote();
			Instrument.cycleInstrumentBack();
			Controller.setInstrument(Instrument.getInstrument());
			
		}
		//Gesture - Down Strum
		if ((newLocation == "DOWN")&&(oldLocation == "UP")){
			
			int instrument = Instrument.getInstrument();
			switch (instrument){
			//Acoustic Guitar
			case 0:
				//Stop Previous Note
				MidiInterface.killLastNote();
				
				if (Instrument.getOldSteelChord()>=0){
					MidiInterface.offNote(Instrument.getInstrument(), Instrument.getOldSteelChord());
					MidiInterface.offNote(Instrument.getInstrument(), 72);
					MidiInterface.offNote(Instrument.getInstrument(), 76);
				}
				MidiInterface.onNote(Instrument.getInstrument(), Instrument.getNewSteelChord());
				MidiInterface.onNote(Instrument.getInstrument(), 72);
				break;
			//Electric Guitar
			case 1:
				if(Instrument.getNewChord().size()>0){
					for (int i=0; i<Instrument.getNewChord().size();i++){
						MidiInterface.offNote(1, Instrument.getNewChord().get(i)+64);	
					}
				}
				Instrument.setNewChord(fret, minor);
				
				for (int i=0; i<Instrument.getNewChord().size();i++){
					MidiInterface.onNote(1, Instrument.getNewChord().get(i)+64);	
				}
				break;
			//Bass
			case 2:
				Instrument.setNewNote(fret);
				MidiInterface.killLastNote();
				MidiInterface.onNote(Instrument.getInstrument(), (Instrument.getNewNote()+40));
				break;
			//Classical
			case 3:
				Instrument.setNewNote(fret);
				MidiInterface.killLastNote();
				MidiInterface.onNote(Instrument.getInstrument(), (Instrument.getNewNote()+40));
				break;
			}
				
		}
		//Gesture - Up Strum
		if ((newLocation == "UP")&&(oldLocation == "DOWN")){
			
			int instrument = Instrument.getInstrument();
			switch (instrument){
			//Acoustic Guitar
			case 0:
				MidiInterface.killLastNote();
				if (Instrument.getOldSteelChord()>=0){
					MidiInterface.offNote(Instrument.getInstrument(), Instrument.getOldSteelChord());
					MidiInterface.offNote(Instrument.getInstrument(), 72);
					MidiInterface.offNote(Instrument.getInstrument(), 76);
				}
				MidiInterface.onNote(Instrument.getInstrument(), Instrument.getNewSteelChord());
				MidiInterface.onNote(Instrument.getInstrument(), 76);
				break;
			//Electric guitar
			case 1:
				if(Instrument.getNewChord().size()>0){
					for (int i=0; i<Instrument.getNewChord().size();i++){
						MidiInterface.offNote(1, Instrument.getNewChord().get(i)+64);	
					}
				}
				Instrument.setNewChord(fret, minor);
				
				for (int i=0; i<Instrument.getNewChord().size();i++){
					MidiInterface.onNote(1, Instrument.getNewChord().get(i)+64);	
				}
				break;
			//Bass Guitar
			case 2:
				Instrument.setNewNote(fret);
				MidiInterface.killLastNote();
				MidiInterface.onNote(Instrument.getInstrument(), (Instrument.getNewNote()+40));
				break;
			//Classical Guitar
			case 3:
				Instrument.setNewNote(fret);
				MidiInterface.killLastNote();
				MidiInterface.onNote(Instrument.getInstrument(), (Instrument.getNewNote()+40));
				break;
			
			}
			

			
		}
	};

	@Override
	public void onColorFrameEvent(byte[] color_frame) {
		
	}

	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
	}
	
	
	public static void playTest(String[] args)
	{
		kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
		if(!kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON))
		{
			ErrorBox.display("Connection Error", "Unable to Connect to Kinect");
		}
		else{
			System.out.println("Kinect Started");
		};
		
	}
	public static void stopKinect(){
		kinect.stop();
		System.out.println("Kinect Stopped");
	}

	
}