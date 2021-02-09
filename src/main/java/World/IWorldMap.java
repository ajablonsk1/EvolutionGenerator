package World;

import Objects.*;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface IWorldMap {

    public boolean place(IObject object);

    public Object objectAt(Vector2d position);

    public boolean canPlace(IObject object);

    public boolean isOccupiedByAnimal(Vector2d position);

    public String getCoords();

    public void spawnGrass();

    public void spawnAnimals();

    public void dinner();

    public void copulation();

    public int getRefresh();

    List<Grass> getGrassList();

    public List<Animal> getAnimals();

     public Vector2d getJungle_lowerLeft();

     public Vector2d getJungle_upperRight();

    public boolean isOccupied(Vector2d position);

    public int getWidth();

    public int getHeight();

    public boolean canMoveTo(Vector2d position);

    public IPositionChangeObserver getObserver();

    public void removeDeadAnimals();

    public void animalsMovement();

    public Map<Vector2d, LinkedList<Animal>> getAnimals2();

    public void animalListSorting();

    public void update(JSONObject jsonObject);

    public int getNumberOfAnimals();

    public int getNumberOfGrass();

    public int dominantGenotype();

    public float averageEnergy();

    public float averageLifeLength();

    public float averageChildren();

}
