package sample;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider seekSlider;

    @FXML
    private ChoiceBox<String> speedChoiceBox;

    @FXML
    private Button buttonPlay;

    @FXML
    private Button buttonLoop;

    @FXML
    private BorderPane aboutSoftwarePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        speedChoiceBox.setValue("1x");
        speedChoiceBox.setItems(FXCollections.observableArrayList("0.75x", "1x", "1.25x"));

        speedChoiceBox.setOnAction(actionEvent -> setSpeedChoiceBoxAction());
        showPauseImageOnButtonPlay();
    }

    private void setSpeedChoiceBoxAction(){

        String value = speedChoiceBox.getValue();

        if(mediaPlayer != null){
            if ("0.5x".equals(value)) {

                mediaPlayer.setRate(1.00);
                mediaPlayer.setRate(0.50);
            } else if ("0.75x".equals(value)) {

                mediaPlayer.setRate(1.00);
                mediaPlayer.setRate(0.75);
            } else if ("1x".equals(value)) {

                mediaPlayer.setRate(1.00);
            } else if ("1.25x".equals(value)) {

                mediaPlayer.setRate(1.00);
                mediaPlayer.setRate(1.25);
            }
        }
    }

    @FXML
    public void handleOpenButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Select a file (*.mp4)", "*.mp4");

        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(null);
        String filePath = file.toURI().toString();

        if (filePath != null) {

            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer( media );

            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                //when player gets ready..
                seekSlider.setMin(0.0);
                seekSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());

                seekSlider.setValue(0);
            });

            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();

            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                }
            });

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    seekSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());  //(t1.getCurrentTime().toSeconds())
                }
            });

            seekSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number o, Number t1) {
                    if(seekSlider.isPressed()) {
                        mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
                    }
                }
            });

            mediaPlayer.play();
        }
    }

    @FXML
    public void handlePlayButtonAction(ActionEvent actionEvent) {
        if(mediaPlayer != null && (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED)) {

            mediaPlayer.play();
            showPauseImageOnButtonPlay();
        }
        else if(mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {

            mediaPlayer.pause();
            showPlayImageOnButtonPlay();
        }
    }

    private void showPlayImageOnButtonPlay() {
        InputStream inputPlayImage = getClass().getResourceAsStream("play.png");
        Image image = new Image(inputPlayImage);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(10.54); // respective height 12.36
        imageView.setFitHeight(14);
        buttonPlay.setGraphic(imageView);
        buttonPlay.setContentDisplay(ContentDisplay.CENTER);
    }

    private void showPauseImageOnButtonPlay() {
        InputStream inputPauseImage = getClass().getResourceAsStream("pause.png");
        Image image = new Image(inputPauseImage);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(15); //respective width 10.54
        imageView.setPreserveRatio(true);
        buttonPlay.setGraphic(imageView);
        buttonPlay.setContentDisplay(ContentDisplay.CENTER);
    }

    @FXML
    private void handleStopButtonAction(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            showPlayImageOnButtonPlay();
        }
    }

    @FXML
    public void handleTopExit(MouseEvent mouseEvent) { System.exit(0); }

    public void handleForwardButtonAction(ActionEvent actionEvent) {
        if(mediaPlayer != null) {
            double duration = mediaPlayer.getCurrentTime().toSeconds();

            duration += 30;
            mediaPlayer.seek(Duration.seconds(duration));
        }
    }

    public void handleBackwardButtonAction(ActionEvent actionEvent) {
        if(mediaPlayer != null) {
            double duration = mediaPlayer.getCurrentTime().toSeconds();

            duration -= 10;
            mediaPlayer.seek(Duration.seconds(duration));
        }
    }

    public void handleLoopButtonAction(ActionEvent actionEvent) {
        if(mediaPlayer != null) {
            if (mediaPlayer.getCycleCount() != MediaPlayer.INDEFINITE) {

                mediaPlayer.setAutoPlay(true);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                buttonLoop.setStyle("-fx-background-color: #a5e333");
            } else {

                mediaPlayer.setAutoPlay(false);
                mediaPlayer.setCycleCount(0);
                buttonLoop.setStyle("-fx-background-color: transparent");
                buttonLoop.setStyle("-fx-shadow-highlight-color: #212121");
            }
        }
    }

    public void handleGoBackIconAction(MouseEvent event) {
        aboutSoftwarePane.toBack();
    }

    public void handleAboutButtonAction(MouseEvent event) {
        aboutSoftwarePane.toFront();
    }
}