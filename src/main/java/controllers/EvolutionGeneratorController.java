package controllers;

import Engine.Engine;
import World.IWorldMap;
import World.WorldMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;


public class EvolutionGeneratorController {

    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("src\\main\\java\\json\\data.json"));
    IWorldMap map;
    IWorldMap map_2;
    Engine engine;
    Engine engine2;
    boolean flag = false;



    public TextField width;
    public TextField height;
    public TextField jungleWidth;
    public TextField jungleHeight;
    public TextField energyLostPerDay;
    public TextField energyReceivedFromGrass;
    public TextField quantityOfGrassPerDay;
    public TextField quantityOfAnimalsAtStart;
    public TextField startingEnergy;
    public TextField refresh;


    public EvolutionGeneratorController() throws IOException, ParseException{
    }


    public void changeHeight(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = height.getText();
        if(input.equals("")) return;
        jsonObject.replace("height", input);
        update();
    }

    public void changeJungleWidth(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = jungleWidth.getText();
        if(input.equals("")) return;
        jsonObject.replace("jungle-width", input);
        update();
    }

    public void changeJungleHeight(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = jungleHeight.getText();
        if(input.equals("")) return;
        jsonObject.replace("jungle-height", input);
        update();
    }

    public void changeEnergyLostPerDay(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = energyLostPerDay.getText();
        if(input.equals("")) return;
        jsonObject.replace("jungle-height", input);
        update();
    }

    public void changeWidth(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = width.getText();
        if(input.equals("")) return;
        jsonObject.replace("width", input);
        update();
    }


    public void changeEnergyFromGrass(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = energyReceivedFromGrass.getText();
        if(input.equals("")) return;
        jsonObject.replace("energyReceivedFromGrass", input);
        update();
    }

    public void changeGrassPerDay(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = quantityOfGrassPerDay.getText();
        if(input.equals("")) return;
        jsonObject.replace("quantityOfGrassPerDay", input);
        update();
    }

    public void changeAnimalsAtStart(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = quantityOfAnimalsAtStart.getText();
        if(input.equals("")) return;
        jsonObject.replace("quantityOfAnimalsAtStart", input);
        update();
    }

    public void changeStartingEnergy(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = startingEnergy.getText();
        if(input.equals("")) return;
        jsonObject.replace("startingEnergy", input);
        update();
    }

    public void changeRefreshTime(KeyEvent keyEvent) throws ParseException, InterruptedException, IOException {
        String input = refresh.getText();
        if(input.equals("")) return;
        jsonObject.replace("refresh", input);
        update();
    }

    public void update() throws IOException, ParseException {
        this.map = new WorldMap(jsonObject);
        this.engine = new Engine(this.map, this.map.getRefresh());
        this.map_2 = new WorldMap(jsonObject);
        this.engine2 = new Engine(this.map_2, this.map_2.getRefresh());
    }

    public void StartOneMapClicked(javafx.event.ActionEvent actionEvent) throws ParseException, IOException {
        this.map = new WorldMap(jsonObject);
        this.engine = new Engine(map, map.getRefresh());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OneMap.fxml"));
        Parent mapParent = loader.load();
        OnMap controller = loader.getController();
        Scene scene = new Scene(mapParent);
        Stage window = new Stage();
        controller.setMap(map);
        window.setScene(scene);
        window.setX(600);
        window.setY(0);
        engine.start(window);
    }


    public void Pause(ActionEvent actionEvent) {
        if(!flag) {
            flag = true;
            engine.pause();
            if(engine2 != null) engine2.pause();
        }
        else{
            flag = false;
            engine.unPause();
            if(engine2 != null) engine2.unPause();
        }
    }

    public void startTwoMaps(ActionEvent actionEvent) throws IOException, ParseException {
        this.map = new WorldMap(jsonObject);
        this.engine = new Engine(map, map.getRefresh());
        this.map_2 = new WorldMap(jsonObject);
        this.engine2 = new Engine(map_2, map_2.getRefresh());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OneMap.fxml"));
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/fxml/OneMap.fxml"));
        Parent mapParent = loader.load();
        Parent mapParent2 = loader2.load();
        OnMap controller = loader.getController();
        OnMap controller2 = loader2.getController();
        controller.setMap(map);
        controller2.setMap(map_2);
        Scene scene = new Scene(mapParent);
        Scene scene2 = new Scene(mapParent2);
        Stage window = new Stage();
        Stage window2 = new Stage();
        window.setScene(scene);
        window2.setScene(scene2);
        window.setAlwaysOnTop(true);
        window.setX(600);
        window.setY(0);
        window2.setX(800);
        window2.setY(0);
        engine.start(window);
        engine2.start(window2);
    }
}

