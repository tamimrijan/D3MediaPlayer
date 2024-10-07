package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("D3 Media Player");
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene primaryScene = new Scene(root,1280, 720);
        primaryScene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && primaryStage.isFullScreen() == false) {
                primaryStage.setFullScreen(true);
            } else if (mouseEvent.getClickCount() == 2 && primaryStage.isFullScreen() == true) {
                primaryStage.setFullScreen(false);
            }
        });

        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            if (!primaryStage.isFullScreen()) {
                primaryStage.setX(mouseEvent.getScreenX() - xOffset);
                primaryStage.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
