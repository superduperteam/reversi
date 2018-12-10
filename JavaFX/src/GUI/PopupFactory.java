package GUI;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupFactory {
    public static void showPopup (String message) {
        Stage stage = new Stage();
        Button dismiss = new Button("Dismiss");

        dismiss.setOnAction(event -> stage.close());

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(new Label(message), dismiss);
        layout.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(layout,Color.BLACK);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(layout.getScene().getWindow());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.show();
    }
}
