
	import javafx.geometry.Insets;
	import javafx.geometry.Pos;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
	import javafx.stage.Stage;

	public class HelpMenu {
		public static void display(){
			Stage window = new Stage();
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("Help");
			window.setMinWidth(600);
			Label Heading = new Label("How to Use Phantom Axe");
			Heading.setTextFill(Color.WHITE);
			Heading.setStyle("-fx-font-size: 15px");
			Heading.setStyle("-fx-font-weight:bold");
			Text helpText = new Text("Welcome to Phantom Axe!\n"
					+ "This guide will show you how to get up and running, as well as tips on playing.");
			helpText.setFill(Color.WHITE);
			Label setMidiHead = new Label("Step 1: Connecting to the MIDI Device.");
			Text setMidi = new Text("In order to connect to the MIDI device, first you will need to select "
					+ "your chosen MIDI device from the drop down list in the top left. "
					+ "By default, the only working device will be the second 'Kinect Air Guitar' Device. "
					+ "Select this then click 'Connect'. The light next to the bar should turn green if successful.");
			setMidi.setFill(Color.WHITE);
			Label connectKinectHead = new Label("Step 2: Connecting to the Kinect");
			Text connectKinect = new Text("Next you'll need to connect to the Kinect device. This is achieved "
					+ "by clicking the power icon. If successful, this should turn green. Once this has "
					+ "ocurred, the fret number should initialize to '0', which means the Kinect is waiting for input. "
					+ "If at any point you wish to stop playing, simply click the power icon again.");
			connectKinect.setFill(Color.WHITE);
			Label playingHead = new Label("Step 3: Playing");
			Text playing = new Text("In order to play the Phantom Axe, stand in front of the Kinect and place your left hand out"
					+ " as if you were holding a guitar. Your current 'fret' and 'note' will be displayed. To change"
					+ " note, simply move your arm closer to, or further from your body."
					+ " To play the note, move your right hand in a strumming motion. Notes will be played on the 'up-strum' "
					+ "as well as the 'down-strum'.");
			playing.setFill(Color.WHITE);
			Label changeInstrumentHead = new Label("Changing Instrument");
			Text changeInstrument = new Text("To change instrument, swipe above your head with your right hand. "
					+ "You may swipe right or left, ensuring that your hand starts and finishes either side of your head.");
			changeInstrument.setFill(Color.WHITE);
			helpText.setWrappingWidth(500);
			setMidi.setWrappingWidth(500);
			connectKinect.setWrappingWidth(500);
			playing.setWrappingWidth(500);
			changeInstrument.setWrappingWidth(500);
			Button okButton = new Button("OK");
			okButton.setId("okButton");
			okButton.setOnAction(e->{
				window.close();
			});
			VBox layout = new VBox(10);
			layout.getChildren().addAll(Heading, helpText, setMidiHead,setMidi,connectKinectHead, connectKinect,playingHead, playing,changeInstrumentHead, changeInstrument, okButton);
			layout.setAlignment(Pos.CENTER);
			layout.setPadding(new Insets(10,10,10,10));
			layout.setId("helpBack");
			
			Scene scene = new Scene(layout);
			scene.getStylesheets().add("Phantom.css");
			window.setScene(scene);
			window.show();
			
		}
	}
