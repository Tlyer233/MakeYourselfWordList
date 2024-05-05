package ui.jumpBox;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class WordListArea extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("I am WordListArea");
        FlowPane root = new FlowPane(label);
        Scene scene = new Scene(root, 200, 100);
        stage.setTitle("WordListArea");
        stage.setScene(scene);
        stage.show();
    }
}
