package controllers;

import Objects.Animal;
import Objects.Grass;
import World.IWorldMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OnMap{

    public AnchorPane important;
    public Label first;
    public Label second;
    public Label third;
    public Label fourth;
    public Label fifth;
    public Label sixth;



    public OnMap() {
    }

    public void setMap(IWorldMap map){
        double squareWidth = (600.0 / map.getWidth());
        double squareHeight = (600.0/ map.getHeight());

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(i * squareWidth);
                rectangle.setY(j * squareHeight);
                rectangle.setWidth(squareWidth);
                rectangle.setHeight(squareHeight);
                rectangle.setFill(Color.web("#80ff80"));
                important.getChildren().add(rectangle);
            }
        }
        for (int i = map.getJungle_lowerLeft().x; i <= map.getJungle_upperRight().x; i++) {
            for (int j = map.getJungle_lowerLeft().y; j <= map.getJungle_upperRight().y; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(i * squareWidth);
                rectangle.setY(j * squareHeight);
                rectangle.setWidth(squareWidth);
                rectangle.setHeight(squareHeight);
                rectangle.setFill(Color.DARKGREEN);
                important.getChildren().add(rectangle);
            }
        }
        for (Grass grass : map.getGrassList()) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(grass.getPosition().x * squareWidth);
            rectangle.setY(grass.getPosition().y * squareHeight);
            rectangle.setWidth(squareWidth);
            rectangle.setHeight(squareHeight);
            rectangle.setFill(Color.web("#00cc00"));
            important.getChildren().add(rectangle);
        }

        for (Animal animal : map.getAnimals()) {
            final EventHandler<javafx.scene.input.MouseEvent> mouseEventEventHandler =
                    new EventHandler<>() {
                        @Override
                        public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                            VBox vbox = new VBox(new Label("Position: " + animal.getPosition()));
                            vbox.getChildren().add(new Label("Move direction: " + animal.getDirection()));
                            vbox.getChildren().add(new Label("Genotype: " + animal.getGenome().getGenome()));
                            vbox.getChildren().add(new Label("Number of children: " + animal.numberOfChild));
                            vbox.getChildren().add(new Label("Energy: " + animal.energy));
                            vbox.getChildren().add(new Label("Length of life: " + animal.lengthOfLife));
                            Button button = new Button("Save");
                            vbox.getChildren().add(button);
                            final EventHandler<ActionEvent> actionEventEventHandler = new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    try{
                                        File file = new File("info" + animal + ".txt");
                                        if(file.createNewFile()){
                                            System.out.println("File created "+ file.getName());
                                        }
                                        else{
                                            System.out.println("File already exists");
                                        }
                                        FileWriter save = new FileWriter(file);
                                        save.write("Position: " + animal.getPosition() + "\n");
                                        save.write("Move direction: " + animal.getDirection() + "\n");
                                        save.write("Genotype: " + animal.getGenome().getGenome() + "\n");
                                        save.write("Number of children: " + animal.numberOfChild + "\n");
                                        save.write("Energy: " + animal.energy + "\n");
                                        save.write("Length of life: " + animal.lengthOfLife + "\n");
                                        save.close();
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            button.addEventFilter(ActionEvent.ACTION, actionEventEventHandler);
                            vbox.setPadding(new Insets(2, 2, 2, 2));
                            Scene scene = new Scene(vbox);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();
                        }
                    };
            Ellipse ellipse = new Ellipse();
            ellipse.setCenterX(animal.getPosition().x * squareWidth + (squareWidth / 2));
            ellipse.setCenterY(animal.getPosition().y * squareHeight + (squareHeight / 2));
            ellipse.setRadiusX(squareWidth / 2);
            ellipse.setRadiusY(squareHeight / 2);
            ellipse.setFill(animal.toColor());
            ellipse.setOnMouseClicked(mouseEventEventHandler);
            important.getChildren().add(ellipse);
        }
    }

    public void addStats(int numberOfAnimals, int numberOfGrass, int dominantGene, float avEnergy, float avLifeLength, float avChildren){
        first.setText(Integer.toString(numberOfAnimals));
        second.setText(Integer.toString(numberOfGrass));
        third.setText(Integer.toString(dominantGene));
        fourth.setText(Float.toString(avEnergy));
        fifth.setText(Float.toString(avLifeLength));
        sixth.setText(Float.toString(avChildren));
    }

}



