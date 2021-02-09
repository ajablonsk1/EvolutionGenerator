package Objects;

import Enums.MoveDirection;
import World.IWorldMap;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Animal implements IObject {

    public Vector2d position;
    public IWorldMap map;
    public MoveDirection direction = randomMoveDirection();

    public int energy;
    public int startEnergy;
    public Genome genome = new Genome();
    public int lengthOfLife = 0;
    public int numberOfChild = 0;

    private List<IPositionChangeObserver> Observers = new LinkedList<>();

    public Animal(Vector2d position, int startEnergy, IWorldMap map) {
        this.position = position;
        this.energy = startEnergy;
        this.startEnergy = startEnergy;
        this.map = map;
    }

    public void addObserver(IPositionChangeObserver observer){
        Observers.add(observer);
    }

    public MoveDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void move() {
        while(true) {
            int rotation;
            Random generator = new Random();
            int help = generator.nextInt(32);
            rotation = this.genome.getGenome().get(help);
            MoveDirection direction1 = this.direction.rotation(rotation);
            Vector2d position1 = this.position.add(direction1.toUnitVector());
            if(this.map.canMoveTo(position1)) {
                addObserver(this.map.getObserver());
                this.positionChanged(this.position, position1, this.map.getObserver());
                this.direction = direction1;
                this.position = position1;
                return;
            }
        }
    }

    public Genome getGenome(){
        return this.genome;
    }

    public MoveDirection randomMoveDirection() {
        Random generator = new Random();
        int help = generator.nextInt(8);
        return switch (help) {
            case 0 -> MoveDirection.NORTH;
            case 1 -> MoveDirection.NORTH_EAST;
            case 2 -> MoveDirection.EAST;
            case 3 -> MoveDirection.SOUTH_EAST;
            case 4 -> MoveDirection.SOUTH;
            case 5 -> MoveDirection.SOUTH_WEST;
            case 6 -> MoveDirection.WEST;
            case 7 -> MoveDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + help);
        };
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IPositionChangeObserver observer) {
        observer.positionChanged(oldPosition, newPosition, this);
    }

    public boolean isDead() {
        return this.energy <= 0;
    }

    public void setGenome(){
        this.genome.animalStartGenome();
        this.genome.genomeSort();
    }

    public Color toColor(){
        if (energy == 0) return Color.web("#ffffff");
        if (energy < 0.2 * startEnergy) return Color.web("#ffeee6");
        if (energy < 0.4 * startEnergy) return Color.web("#ffccb3");
        if (energy < 0.6 * startEnergy) return Color.web("#ffaa80");
        if (energy < 0.8 * startEnergy) return Color.web("#ff884d");
        if (energy < startEnergy) return Color.web("#ff661a");
        if (energy < 2 * startEnergy) return Color.web("#e64d00");
        if (energy < 4 * startEnergy) return Color.web("#b33c00");
        if (energy < 6 * startEnergy) return Color.web("#802b00");
        if (energy < 8 * startEnergy) return Color.web("#4d1a00");
        if (energy < 10 * startEnergy) return Color.web("#1a0900");
        return Color.web("#000000");
    }

}
