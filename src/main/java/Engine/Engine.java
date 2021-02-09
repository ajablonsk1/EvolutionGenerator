package Engine;

import World.IWorldMap;
import controllers.OnMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Engine{
    IWorldMap map;
    int refresh;
    OnMap controller;
    Stage window;
    Parent mapParent;
    Timeline timeline;

    public Engine(IWorldMap map, int refresh){
        this.map = map;
        map.spawnAnimals();
        this.refresh = refresh;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(this.map.getRefresh()), this::run));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }


    public void run(ActionEvent actionEvent) {
        this.map.animalListSorting();
        this.map.removeDeadAnimals();
        this.map.animalsMovement();
        this.map.dinner();
        this.map.copulation();
        this.map.spawnGrass();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OneMap.fxml"));
        try {
            mapParent = loader.load();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        controller = loader.getController();
        Scene scene = new Scene(mapParent);
        controller.setMap(this.map);
        controller.addStats(this.map.getNumberOfAnimals(), this.map.getNumberOfGrass(), this.map.dominantGenotype(), this.map.averageEnergy(), this.map.averageLifeLength(), this.map.averageChildren());
        window.setScene(scene);
    }

    public void pause() {
        this.timeline.pause();
    }

    public void start(Stage stage) {
        this.window = stage;
        window.show();
        this.timeline.play();
    }

    public void unPause(){
        this.timeline.play();
    }
}
