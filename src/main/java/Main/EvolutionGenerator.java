package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EvolutionGenerator extends Application {


    public EvolutionGenerator() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent Root = FXMLLoader.load(getClass().getResource("/fxml/EvolutionGenerator.fxml"));
        stage.setTitle("Evolution Generator");
        Scene Home = new Scene(Root);
        stage.setScene(Home);
        stage.setX(0);
        stage.setY(0);
        stage.show();
    }

}
