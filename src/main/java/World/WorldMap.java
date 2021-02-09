package World;

import Objects.*;
import org.json.simple.JSONObject;

import java.util.*;

public class WorldMap implements IWorldMap, IPositionChangeObserver {

    int width;
    int height;

    IPositionChangeObserver observer = this;

    int jungle_width;
    int jungle_height;


    Vector2d jungle_upperRight;
    Vector2d jungle_lowerLeft;

    int energyLostPerDay;
    int energyReceivedFromGrass;

    int quantityOfGrassPerDay;
    int quantityOfAnimalsAtStart;
    int startingEnergy;
    int energyToCopulation = startingEnergy / 2;

    int refresh;
    public List<Grass> GrassList = new LinkedList<>();
    public List<Animal> AnimalList = new LinkedList<>();
    public List<Animal> DeadAnimals = new LinkedList<>();

    Map<Vector2d, LinkedList<Animal>> AnimalsAtMap = new HashMap<>();
    Map<Vector2d, Grass> GrassAtMap = new HashMap<>();


    public WorldMap(JSONObject jsonObject) {
        this.width = Integer.parseInt((String) jsonObject.get("width"));
        this.height = Integer.parseInt((String) jsonObject.get("height"));
        this.jungle_width = Integer.parseInt((String) jsonObject.get("jungle-width"));
        this.jungle_height = Integer.parseInt((String) jsonObject.get("jungle-height"));
        this.energyLostPerDay = Integer.parseInt((String) jsonObject.get("energyLostPerDay"));
        this.energyReceivedFromGrass = Integer.parseInt((String) jsonObject.get("energyReceivedFromGrass"));
        this.jungle_upperRight = setJungle_upperRight();
        this.jungle_lowerLeft = setJungle_lowerLeft();
        this.quantityOfGrassPerDay = Integer.parseInt((String) jsonObject.get("quantityOfGrassPerDay"));
        this.quantityOfAnimalsAtStart = Integer.parseInt((String) jsonObject.get("quantityOfAnimalsAtStart"));
        this.startingEnergy = Integer.parseInt((String) jsonObject.get("startingEnergy"));
        this.refresh = Integer.parseInt((String) jsonObject.get("refresh"));

    }


    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getRefresh(){
        return refresh;
    }

    public Vector2d getJungle_upperRight(){
        return this.jungle_upperRight;
    }

    public List<Animal> getAnimals(){
        return this.AnimalList;
    }

    public List<Grass> getGrass(){
        return this.GrassList;
    }

    public Vector2d getJungle_lowerLeft(){
        return this.jungle_lowerLeft;
    }

    public IPositionChangeObserver getObserver(){
        return observer;
    }

    public Vector2d setJungle_upperRight() {
        int width = ((this.width - this.jungle_width) / 2);
        int height = ((this.height - this.jungle_height) / 2);
        return new Vector2d((this.width - 1) - width, (this.height - 1) - height);
    }

    public Vector2d setJungle_lowerLeft() {
        int width = ((this.width - this.jungle_width) / 2);
        int height = ((this.height - this.jungle_height) / 2);
        return new Vector2d(width + ((this.width - this.jungle_width) % 2), height + ((this.height - this.jungle_height) % 2));
    }

    @Override
    public String getCoords() {
        return jungle_lowerLeft + " " + jungle_upperRight;
    }

    @Override
    public List<Grass> getGrassList() {
        return GrassList;
    }

    public Map<Vector2d, LinkedList<Animal>> getAnimals2(){
        return AnimalsAtMap;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal object) {
        AnimalsAtMap.get(oldPosition).remove(object);
        if (AnimalsAtMap.get(oldPosition).size() == 0) AnimalsAtMap.remove(oldPosition);
        if (AnimalsAtMap.get(newPosition) == null) {
            LinkedList<Animal> animals = new LinkedList<>();
            animals.add(object);
            AnimalsAtMap.put(newPosition, animals);
        } else {
            AnimalsAtMap.get(newPosition).add(object);
        }
    }

    @Override
    public boolean place(IObject object) {
        if (canPlace(object)) {
            if (object instanceof Grass) {
                if (!isOccupied(object.getPosition())) {
                    GrassList.add((Grass) object);
                    return true;
                }
            } else {
                AnimalList.add((Animal) object);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        LinkedList<Animal> help = AnimalsAtMap.get(position);
        if (help == null) return GrassAtMap.get(position);
        else if (help.isEmpty()) return GrassAtMap.get(position);
        else return help.getFirst();
    }

    public boolean canMoveTo(Vector2d position){
        return position.x >= 0 && position.x <= width-1 && position.y >= 0 && position.y <= height-1;
    }

    @Override
    public boolean canPlace(IObject object) {
        if (object.getPosition().x >= 0 && object.getPosition().x < this.width && object.getPosition().y >= 0 && object.getPosition().y < this.height) {
            if (object instanceof Grass) {
                return !isOccupiedByAnimal(object.getPosition());
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOccupiedByAnimal(Vector2d position) {
        for (Animal animal : AnimalList) {
            if (animal.getPosition().equals(position)) return true;
        }
        return false;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        for (Animal animal : AnimalList) {
            if (animal.getPosition().equals(position)) return true;
        }
        for (Grass grass : GrassList) {
            if (grass.getPosition().equals(position)) return true;
        }
        return false;
    }

    private boolean isJungleFull() {
        Vector2d vector;
        for (int i = this.jungle_lowerLeft.x; i <= this.jungle_upperRight.x; i++) {
            for (int j = this.jungle_lowerLeft.y; i <= this.jungle_upperRight.y; i++) {
                vector = new Vector2d(i, j);
                if (!isOccupied(vector)) return false;
            }
        }
        return true;
    }

    private boolean isGrassLandFull() {
        Vector2d vector;
        for (int i = 0; i < (this.width - 1); i++) {
            if (i == this.jungle_lowerLeft.x) {
                i = this.jungle_upperRight.x + 1;
            }
            for (int j = 0; j < (this.height - 1); j++) {
                if (j == this.jungle_lowerLeft.y) {
                    j = this.jungle_upperRight.y + 1;
                }
                vector = new Vector2d(i, j);
                if (!isOccupied(vector)) return false;
            }
        }
        return true;
    }

    @Override
    public void spawnGrass() {
        int grassInJungle = (int) (0.6 * quantityOfGrassPerDay);
        int grassInGrassLands = (int) (0.4 * quantityOfGrassPerDay);
        if ((grassInJungle + grassInGrassLands) != quantityOfGrassPerDay) {
            grassInJungle++;
        }
        int xCord;
        int yCord;
        int count = 0;
        while (true) {
            if (isJungleFull()) break;
            Random xGenerator = new Random();
            Random yGenerator = new Random();
            xCord = xGenerator.nextInt(this.jungle_width) + this.jungle_lowerLeft.x;
            yCord = yGenerator.nextInt(this.jungle_height) + this.jungle_lowerLeft.y;
            Vector2d grassVector = new Vector2d(xCord, yCord);
            Grass grass = new Grass(grassVector);
            if (!isOccupied(grassVector)) {
                place(grass);
                GrassAtMap.put(grassVector, grass);
                count += 1;
                if(count == grassInJungle){
                    break;
                }
            }
        }
        count = 0;
        while (true) {
            if (isGrassLandFull()) break;
            Random xGenerator = new Random();
            Random yGenerator = new Random();
            xCord = xGenerator.nextInt(this.width);
            yCord = yGenerator.nextInt(this.height);
            if(xCord >= jungle_lowerLeft.x && yCord >= jungle_lowerLeft.y && xCord <= jungle_upperRight.x && yCord <= jungle_upperRight.y){
                continue;
            }
            Vector2d grassVector = new Vector2d(xCord, yCord);
            Grass grass = new Grass(grassVector);
            if (!isOccupied(grassVector)) {
                place(grass);
                GrassAtMap.put(grassVector, grass);
                count += 1;
                if(count == grassInGrassLands){
                    return;
                }
            }
        }
    }

    public void animalListSorting(){
        EnergyComparator Comparator = new EnergyComparator();
        for(LinkedList<Animal> list: AnimalsAtMap.values()){
            list.sort(Comparator);
        }
    }

    @Override
    public void spawnAnimals(){
        int xCords;
        int yCords;
        int count = 0;
        while(true){
            Random xGenerator = new Random();
            Random yGenerator = new Random();
            xCords = xGenerator.nextInt(this.width);
            yCords = yGenerator.nextInt(this.height);
            Vector2d animalVector = new Vector2d(xCords, yCords);
            Animal animal = new Animal(animalVector, startingEnergy, this);
            animal.setGenome();
            if(!isOccupiedByAnimal(animalVector)){
                place(animal);
                count += 1;
                if(AnimalsAtMap.get(animalVector) == null){
                    LinkedList<Animal> animals = new LinkedList<>();
                    AnimalsAtMap.put(animalVector, animals);
                    AnimalsAtMap.get(animalVector).add(animal);
                }
                else {
                    AnimalsAtMap.get(animalVector).add(animal);
                }
                if(count == quantityOfAnimalsAtStart){
                    return;
                }
            }
        }
    }

    public boolean objectsAround(Vector2d position){
        if(!isOccupied(new Vector2d(position.x+1, position.y))) return false;
        if(!isOccupied(new Vector2d(position.x, position.y+1))) return false;
        if(!isOccupied(new Vector2d(position.x+1, position.y+1))) return false;
        if(!isOccupied(new Vector2d(position.x-1, position.y-1))) return false;
        if(!isOccupied(new Vector2d(position.x-1, position.y))) return false;
        if(!isOccupied(new Vector2d(position.x, position.y-1))) return false;
        if(!isOccupied(new Vector2d(position.x+1, position.y-1))) return false;
        return isOccupied(new Vector2d(position.x - 1, position.y + 1));

    }

    public void update(JSONObject jsonObject){
        this.width = Integer.parseInt((String) jsonObject.get("width"));
        this.height = Integer.parseInt((String) jsonObject.get("height"));
        this.jungle_width = Integer.parseInt((String) jsonObject.get("jungle-width"));
        this.jungle_height = Integer.parseInt((String) jsonObject.get("jungle-height"));
        this.energyLostPerDay = Integer.parseInt((String) jsonObject.get("energyLostPerDay"));
        this.energyReceivedFromGrass = Integer.parseInt((String) jsonObject.get("energyReceivedFromGrass"));
        this.jungle_upperRight = setJungle_upperRight();
        this.jungle_lowerLeft = setJungle_lowerLeft();
        this.quantityOfGrassPerDay = Integer.parseInt((String) jsonObject.get("quantityOfGrassPerDay"));
        this.quantityOfAnimalsAtStart = Integer.parseInt((String) jsonObject.get("quantityOfAnimalsAtStart"));
        this.startingEnergy = Integer.parseInt((String) jsonObject.get("startingEnergy"));
        this.refresh = Integer.parseInt((String) jsonObject.get("refresh"));
    }

    public void copulation() {
        for (LinkedList<Animal> animalList : AnimalsAtMap.values()) {
            if (animalList != null) {
                if (animalList.size() >= 2) {
                    Animal parent_1 = animalList.get(0);
                    Animal parent_2 = animalList.get(1);
                    if (parent_1.energy >= energyToCopulation && parent_2.energy >= energyToCopulation) {
                        int childEnergy = (parent_1.energy / 4) + (parent_2.energy / 4);
                        parent_1.energy = parent_1.energy / 4;
                        parent_2.energy = parent_2.energy / 4;
                        Vector2d childPosition = new Vector2d(0, 0);
                        int xCord;
                        int yCord;
                        Random xGenerator = new Random();
                        Random yGenerator = new Random();
                        Animal child;
                        while (true) {
                            xCord = xGenerator.nextInt(3) + (parent_1.position.x - 1);
                            yCord = yGenerator.nextInt(3) + (parent_1.position.y - 1);
                            if (xCord == parent_1.position.x && yCord == parent_1.position.y) {
                                continue;
                            }
                            if (isOccupied(new Vector2d(xCord, yCord))) {
                                if (objectsAround(parent_1.position)) {
                                    child = new Animal(new Vector2d(xCord, yCord), childEnergy, this);
                                    child.direction = child.randomMoveDirection();
                                    child.genome = child.genome.genomeForChild(parent_1.genome, parent_2.genome);
                                    parent_1.numberOfChild += 1;
                                    parent_2.numberOfChild += 1;
                                    this.AnimalList.add(child);
                                    if (AnimalsAtMap.get(new Vector2d(xCord, yCord)) == null) {
                                        LinkedList<Animal> animals = new LinkedList<>();
                                        AnimalsAtMap.put(new Vector2d(xCord, yCord), animals);
                                        this.AnimalsAtMap.get(new Vector2d(xCord, yCord)).add(child);
                                    } else {
                                        this.AnimalsAtMap.get(new Vector2d(xCord, yCord)).add(child);
                                    }
                                    return;
                                }
                            }
                            else{
                                child = new Animal(new Vector2d(xCord, yCord), childEnergy, this);
                                child.direction = child.randomMoveDirection();
                                child.genome = child.genome.genomeForChild(parent_1.genome, parent_2.genome);
                                parent_1.numberOfChild += 1;
                                parent_2.numberOfChild += 1;
                                this.AnimalList.add(child);
                                if (AnimalsAtMap.get(new Vector2d(xCord, yCord)) == null) {
                                    LinkedList<Animal> animals = new LinkedList<>();
                                    AnimalsAtMap.put(new Vector2d(xCord, yCord), animals);
                                    this.AnimalsAtMap.get(new Vector2d(xCord, yCord)).add(child);
                                } else {
                                    this.AnimalsAtMap.get(new Vector2d(xCord, yCord)).add(child);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void dinner(){
        for(Vector2d vector2d: AnimalsAtMap.keySet()){
            Grass grass = GrassAtMap.get(vector2d);
            if(grass != null){
                AnimalsAtMap.get(vector2d).get(0).energy += energyReceivedFromGrass;
                GrassAtMap.remove(vector2d);
                GrassList.remove(grass);
            }
        }
    }

    public void removeDeadAnimals() {
        Iterator<Animal> animal = AnimalList.iterator();
        while (animal.hasNext()) {
            Animal animal1 = animal.next();
            if(animal1.isDead()){
                DeadAnimals.add(animal1);
                animal.remove();
                AnimalsAtMap.get(animal1.position).remove(animal1);
                if(AnimalsAtMap.get(animal1.position).size() == 0){
                    AnimalsAtMap.remove(animal1.position);
                }
            }
        }
    }


    public void animalsMovement() {
        if (AnimalList != null) {
            for (Animal animal : AnimalList) {
                animal.energy -= energyLostPerDay;
                animal.lengthOfLife += 1;
                animal.move();
            }
        }
    }

    public int getNumberOfAnimals() {
        return AnimalList.size();
    }

    public int getNumberOfGrass(){
        return GrassList.size();
    }

    public int dominantGenotype(){
        int k = 0;
        int help = 0;
        int[] array = new int[8];
        array[0] = 0;
        for(Animal animal: AnimalList){
            for(Integer genome: animal.getGenome().getGenome()){
                array[genome] += 1;
            }
        }
        for(int i = 0; i < array.length; i++){
            if(array[i] > help) {
                help = array[i];
                k = i;
            }
        }
        return k;
    }

    public float averageEnergy(){
        float sum = 0;
        for(Animal animal: AnimalList){
            sum += (float) animal.energy;
        }
        return (sum / AnimalList.size());
    }

    public float averageLifeLength(){
        float sum = 0;
        if(DeadAnimals.size() == 0){
            return sum;
        }
        for(Animal animal: DeadAnimals){
            sum += (float) animal.lengthOfLife;
        }
        return (sum / DeadAnimals.size());
    }

    public float averageChildren(){
        float sum = 0;
        for(Animal animal: AnimalList){
            sum += (float) animal.numberOfChild;
        }
        return (sum / AnimalList.size());
    }
}
