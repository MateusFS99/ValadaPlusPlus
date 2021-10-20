package valadaplusplus;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ValadaPlusPlus extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
        Parent root = null;
        
        root = FXMLLoader.load(getClass().getResource("TelaPrincipal.fxml"));
        stage.resizableProperty().setValue(false);       
        stage.setTitle("Valada++IDE");
        
        Scene scene = new Scene(root);
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/marnop.png")));
        stage.setScene(scene);
        stage.setMaxWidth(800);
        stage.setMaxHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        
        launch(args);
    }
}