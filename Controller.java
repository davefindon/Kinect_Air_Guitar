
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice.Info;

public class Controller implements Initializable {
	
	
	@FXML
	private ChoiceBox<Object> midiDevice;
	
	@FXML
	private Label fretNo;
	
	@FXML
	private Label chord;
	
	@FXML 
	public static Button connect;
	
	@FXML
	public static Button nextInstrument;
	
	
	@FXML
	public ImageView guitarImage;
	
	@FXML
	private Label instrumentHead;
	
	@FXML 
	ToggleButton kinectPower;
	
	@FXML
	Rectangle fretLocation;
	
	@FXML
	Circle deviceOn;
	TranslateTransition transition = new TranslateTransition();
	
	Image steelGuitar = new Image("steel2.PNG");
	Image electricGuitar = new Image("TELE3.PNG");
	Image bassGuitar = new Image("bass.PNG");
	Image classicalGuitar = new Image("nylon3.PNG");
	
	public Rectangle getFretLocation(){
		return fretLocation;
	}
	
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
        Info[] devices = MidiInterface.getDeviceList();
		for(int i = 0;i < devices.length; i++){
			midiDevice.getItems().add(devices[i]);
		}
		guitarImage.setImage(steelGuitar);
		
    }
    public void connectMidi(){
    	Object selectedDevice = midiDevice.getValue();
		try {
			MidiInterface.connectToDevice(selectedDevice);
			deviceOn.setStyle("-fx-fill:green");
			System.out.println("CONNECTED");
			
		} catch (Exception e1) {
			e1.printStackTrace();
			deviceOn.setStyle("-fx-fill:red");
			ErrorBox.display("Connection Error", "Unable to Connect to MIDI device");
		}
    }
    
    public void startKinect() throws InvalidMidiDataException{
    	if(kinectPower.isSelected()){
    		String[] args = null;
    		GestureControl.playTest(args);
    		fretNo.textProperty().bind(fretValue.asString());
    		chord.textProperty().bind(chordValue);
    		instrumentHead.textProperty().bind(instrumentValue);
    	}
    	else{
    		MidiInterface.killLastNote();
    		GestureControl.stopKinect();
    	}	
    }
    public void nextInstrument() throws InvalidMidiDataException{
    	Instrument.cycleInstrument();
    	changeImage();
    };
    public void showHelp(){
    	HelpMenu.display();
    }
    
    
    static StringProperty instrumentValue = new SimpleStringProperty("Acoustic");
    
    public static void setInstrument (int instrument){
    	Platform.runLater(()->{
    		;
    		switch(instrument){
    		
    		case 0:
    			instrumentValue.setValue("Acoustic"); 			
    			break;
    		case 1:
    			instrumentValue.setValue("Electric");
    			break;
    		case 2:
    			instrumentValue.setValue("Bass");
    			break;
    		case 3:
    			instrumentValue.setValue("Classical");
    			break;
    			
    		}
    	});
    }
	public void changeImage(){
		int instrument = Instrument.getInstrument();
		switch(instrument){
		case 0:
			guitarImage.setImage(steelGuitar);
			break;
		case 1:
			guitarImage.setImage(electricGuitar);
			break;
		case 2:
			guitarImage.setImage(bassGuitar);
			break;
		case 3:
			guitarImage.setImage(classicalGuitar);
			break;
		}
	}
    
    
    static int fretNumber = 0;
    static String chordName = "E";
	static IntegerProperty fretValue = new SimpleIntegerProperty(fretNumber);
	static StringProperty chordValue = new SimpleStringProperty(chordName);
	
	static public void setFret(int fret){
		
		Platform.runLater(()->{
			fretValue.setValue(fret);
			switch(fret){
			case 0: 
				chordValue.setValue("E");
				break;
			case 1: 
				chordValue.setValue("F");
				break;
			case 2: 
				chordValue.setValue("F#");
				break;
			case 3: 
				chordValue.setValue("G");
				break;
			case 4: 
				chordValue.setValue("G#");
				break;
			case 5: 
				chordValue.setValue("A");
				break;
			case 6: 
				chordValue.setValue("A#");
				break;
			case 7: 
				chordValue.setValue("B");
				break;
			case 8: 
				chordValue.setValue("C");
				break;
			case 9: 
				chordValue.setValue("C#");
				break;
			case 10: 
				chordValue.setValue("D");
				break;
			case 11: 
				chordValue.setValue("D#");
				break;
			case 12: 
				chordValue.setValue("E");
				break;
				
				
			}
		});
	}
	
	

	



	
}

