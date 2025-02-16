package newpiano;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class newpiano extends Application {

    private final HashMap<String, String> keyToNoteMap = new HashMap<>();
    private final HashMap<String, Rectangle> keyToRectangleMap = new HashMap<>();
    private boolean isRecording = false;
    private TargetDataLine targetDataLine;

    @Override
    public void start(Stage primaryStage) {
        initializeKeyToNoteMap();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #282c34;");
        // Header (Menu Bar)
        MenuBar menuBar = createMenuBar();

        // Piano Layout
        Pane pianoPane = new Pane();
        createKeys(pianoPane);

        // Controls (Record/Stop Buttons)
        HBox controls = new HBox(10);
        controls.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Button recordButton = new Button("Record");
        Button stopButton = new Button("Stop");
        stopButton.setDisable(true); // Disable stop button initially

        recordButton.setStyle("-fx-background-color: #e63946; "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 16px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 10px 20px; "
                + "-fx-border-radius: 8px; "
                + "-fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 3);");

        stopButton.setStyle("-fx-background-color: #457b9d; "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 16px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 10px 20px; "
                + "-fx-border-radius: 8px; "
                + "-fx-background-radius: 8px; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 3);");

        recordButton.setOnAction(event -> {
            if (!isRecording) {
                startRecording(recordButton, stopButton);
            }
        });

        stopButton.setOnAction(event -> {
            if (isRecording) {
                stopRecording(recordButton, stopButton);
            }
        });

        controls.getChildren().addAll(recordButton, stopButton);

        root.setTop(menuBar); // Add menu bar to the top
        root.setCenter(pianoPane);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 400);
        scene.setOnKeyPressed(this::playSound);

       
        // Make the keys responsive
        scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeKeys(pianoPane, newVal.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeKeys(pianoPane, scene.getWidth(), newVal.doubleValue()));

        primaryStage.setTitle("Virtual Piano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // "About" menu
        Menu aboutMenu = new Menu("About");
        MenuItem aboutItem = new MenuItem("About the Piano");
        aboutMenu.setStyle("-fx-background-color: #90CAF9; " // Soft blue, easy on the eyes
                + "-fx-text-fill: white; "
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 5px 20px; " // Increased padding for length
                + "-fx-background-radius: 8px; " // Rounded corners
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 3);");
        aboutItem.setOnAction(event -> showAboutDialog());
        aboutMenu.getItems().add(aboutItem);

        // "Help" menu
        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Piano Help");
        helpMenu.setStyle("-fx-background-color: #90CAF9; " // Soft blue, same as About
                + "-fx-text-fill: white; "
                + "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 5px 20px; " // Increased padding for length
                + "-fx-background-radius: 8px; " // Rounded corners
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 3);");
        helpItem.setOnAction(event -> showAboutHelp());
        helpMenu.getItems().add(helpItem);

        // Adding menus to the menu bar
        menuBar.getMenus().addAll(aboutMenu, helpMenu);

        return menuBar;
    }


    private void showAboutDialog() {
        Alert aboutAlert = new Alert(AlertType.INFORMATION);
        aboutAlert.setTitle("About the Virtual Piano");
        aboutAlert.setHeaderText("Virtual Piano Application");
        aboutAlert.setContentText("This is a virtual piano application created using JavaFX.\n\nFeatures:\n- Play piano notes using the keyboard.\n- Responsive piano keys that adjust to window size.\n- Record and stop functionality.\n\nDeveloped by: Nazeef ul Hassan Qureshi\n                         Abdullah Wali");
        aboutAlert.showAndWait();
    }
    private void showAboutHelp() {
        // Create the help dialog
        StringBuilder helpContent = new StringBuilder();
        helpContent.append("Here are the keys and their corresponding piano sounds:\n\n");
        
        keyToNoteMap.forEach((key, note) -> helpContent.append("Key '").append(key).append("' -> Sound File: ").append(note).append("\n"));

        Alert helpAlert = new Alert(AlertType.INFORMATION);
        helpAlert.setTitle("Piano Help");
        helpAlert.setHeaderText("Piano Key and Sound Mapping");
        helpAlert.setContentText(helpContent.toString());
        helpAlert.showAndWait();
    }
    private void initializeKeyToNoteMap() {
        keyToNoteMap.put("z", "C3.wav"); // C3
        keyToNoteMap.put("x", "D3.wav"); // D3
        keyToNoteMap.put("c", "E3.wav"); // E3
        keyToNoteMap.put("v", "F3.wav"); // F3
        keyToNoteMap.put("b", "G3.wav"); // G3
        keyToNoteMap.put("n", "A3.wav"); // A3
        keyToNoteMap.put("m", "B3.wav"); // B3
        keyToNoteMap.put(",", "C4.wav"); // C4
        keyToNoteMap.put(".", "D4.wav"); // D4
        keyToNoteMap.put("/", "E4.wav"); // E4
        keyToNoteMap.put("q", "CSharp3.wav"); // C#3
        keyToNoteMap.put("w", "DSharp3.wav"); // D#3
        keyToNoteMap.put("e", "FSharp3.wav"); // F#3
        keyToNoteMap.put("r", "GSharp3.wav"); // G#3
        keyToNoteMap.put("t", "ASharp3.wav"); // A#3
        keyToNoteMap.put("y", "CSharp4.wav"); // C#4
        keyToNoteMap.put("u", "DSharp4.wav"); // D#4
    }

    private void createKeys(Pane pane) {
        String[] whiteKeys = {"z", "x", "c", "v", "b", "n", "m", ",", ".", "/"};
        String[] blackKeys = {"q", "w", null, "e", "r", "t", null, "y", "u"};

        double keyWidth = 70;
        double keyHeight = 300;

        for (int i = 0; i < whiteKeys.length; i++) {
            Rectangle whiteKey = new Rectangle(keyWidth, keyHeight);
            whiteKey.setFill(Color.WHITE);
            whiteKey.setStroke(Color.BLACK);
            whiteKey.setX(i * keyWidth);
            pane.getChildren().add(whiteKey);
            keyToRectangleMap.put(whiteKeys[i], whiteKey);
        }

        for (int i = 0; i < blackKeys.length; i++) {
            if (blackKeys[i] != null) {
                Rectangle blackKey = new Rectangle(keyWidth * 0.6, keyHeight * 0.6);
                blackKey.setFill(Color.BLACK);
                blackKey.setStroke(Color.BLACK);
                blackKey.setX((i + 1) * keyWidth - (keyWidth * 0.3));
                blackKey.setY(0);
                pane.getChildren().add(blackKey);
                keyToRectangleMap.put(blackKeys[i], blackKey);
            }
        }
    }

    private void resizeKeys(Pane pane, double width, double height) {
        double whiteKeyWidth = width / 10;
        double whiteKeyHeight = height * 0.75;

        int index = 0;
        for (String key : new String[]{"z", "x", "c", "v", "b", "n", "m", ",", ".", "/"}) {
            Rectangle rect = keyToRectangleMap.get(key);
            if (rect != null) {
                rect.setWidth(whiteKeyWidth);
                rect.setHeight(whiteKeyHeight);
                rect.setX(index * whiteKeyWidth);
                index++;
            }
        }

        index = 0;
        for (String key : new String[]{"q", "w", null, "e", "r", "t", null, "y", "u"}) {
            Rectangle rect = keyToRectangleMap.get(key);
            if (rect != null) {
                rect.setWidth(whiteKeyWidth * 0.6);
                rect.setHeight(whiteKeyHeight * 0.6);
                rect.setX((index + 1) * whiteKeyWidth - (whiteKeyWidth * 0.3));
                rect.setY(0);
            }
            index++;
        }
    }

    private void playSound(KeyEvent event) {
        String key = event.getText().toLowerCase();
        if (keyToNoteMap.containsKey(key)) {
            String soundFile = "C:/Users/Dell/Desktop/newpiano/src/newpiano/sounds/" + keyToNoteMap.get(key);
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

           
        }
    }
    private void startRecording(Button recordButton, Button stopButton) {
        isRecording = true;
        recordButton.setText("Recording...");
        stopButton.setDisable(false);

        File outputFile = new File("piano_recording.wav");

        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        try {
            targetDataLine = AudioSystem.getTargetDataLine(format);
            targetDataLine.open(format);
            targetDataLine.start();

            Thread recordingThread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(targetDataLine)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording(Button recordButton, Button stopButton) {
        isRecording = false;
        targetDataLine.stop();
        targetDataLine.close();

        recordButton.setText("Record");
        stopButton.setText("Stopped");
        stopButton.setDisable(true);
        System.out.println("Recording saved to piano_recording.wav");
    }
    public static void main(String[] args) {
        launch(args);
    }
}